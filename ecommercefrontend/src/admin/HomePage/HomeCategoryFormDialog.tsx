import { useState } from "react";
import {
    Dialog, DialogTitle, DialogContent, DialogActions,
    TextField, Button, Avatar, Box, CircularProgress, Typography,
} from "@mui/material";
import type { HomeCategory } from "../../types/HomeCategory.ts";
import { useAppDispatch } from "../../state/hooks.ts";
import { fetchHomePageData } from "../../state/customer/CustomerSlice.ts";
import { api } from "../../config/Api.ts";
import {createHomeCategory, fetchHomeCategories, updateHomeCategory} from "../../state/admin/AdminSlice.ts";

interface Props {
    open: boolean;
    onClose: () => void;
    onSaved: () => void;
    category?: HomeCategory;
    section: string;
}

const HomeCategoryFormDialog = ({ open, onClose, onSaved, category, section }: Props) => {
    const dispatch = useAppDispatch();
    const [name, setName] = useState(category?.name ?? "");
    const [imageUrl, setImageUrl] = useState(category?.imageUrl ?? "");
    const [categoryId, setCategoryId] = useState(category?.categoryId ?? (section === "ELECTRIC_CATEGORIES" ? "ELECTRIC_CATEGORIES" : ""));
    const [uploading, setUploading] = useState(false);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const isEdit = Boolean(category);

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;
        setUploading(true);
        setError(null);
        try {
            const formData = new FormData();
            formData.append("file", file);
            formData.append("folder", section.toLowerCase());
            const res = await api.post<string>("/upload/image", formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });
            setImageUrl(res.data);
        } catch {
            setError("Image upload failed. Try again.");
        } finally {
            setUploading(false);
        }
    };

    const handleSave = async () => {
        if (!name.trim() || !imageUrl) {
            setError("Name and image are required.");
            return;
        }
        setSaving(true);
        setError(null);
        try {
            if (isEdit && category) {
                await dispatch(updateHomeCategory({
                    id: category.id,
                    data: { 
                        name, 
                        imageUrl,
                        categoryId: category.categoryId,
                        section: category.section,
                    },
                })).unwrap();
            } else {
                if (!categoryId.trim()) {
                    setError("Category ID is required.");
                    setSaving(false);
                    return;
                }
                await dispatch(createHomeCategory({
                    name,
                    imageUrl,
                    categoryId,
                    section,
                } as HomeCategory)).unwrap();
            }
            await Promise.all([
                dispatch(fetchHomeCategories()),
                dispatch(fetchHomePageData()),
            ]);
            onSaved();
            onClose();
        } catch (err: any) {
            setError(typeof err === "string" ? err : "Save failed.");
        } finally {
            setSaving(false);
        }
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
            <DialogTitle>{isEdit ? "Edit Category" : `Add ${section.replace('_', ' ')} Category`}</DialogTitle>
            <DialogContent sx={{ display: "flex", flexDirection: "column", gap: 2, pt: 1 }}>
                <TextField
                    label="Name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    fullWidth
                />
                {!isEdit && section !== "ELECTRIC_CATEGORIES" && (
                    <TextField
                        label="Category Name"
                        value={categoryId}
                        onChange={(e) => setCategoryId(e.target.value)}
                        fullWidth
                    />
                )}
                <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
                    <Avatar variant="rounded" src={imageUrl} sx={{ width: 60, height: 80 }} />
                    <Button component="label" variant="outlined" disabled={uploading}>
                        {uploading ? <CircularProgress size={20} /> : "Upload Image"}
                        <input type="file" accept="image/*" hidden onChange={handleFileChange} />
                    </Button>
                </Box>
                {error && <Typography color="error" variant="body2">{error}</Typography>}
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Cancel</Button>
                <Button onClick={handleSave} disabled={saving || uploading} variant="contained">
                    {saving ? <CircularProgress size={20} /> : "Save"}
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default HomeCategoryFormDialog;