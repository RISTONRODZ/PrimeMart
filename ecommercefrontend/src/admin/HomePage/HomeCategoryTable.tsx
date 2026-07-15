import {
    Avatar,
    Box,
    IconButton,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    Snackbar,
    Alert,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import AddIcon from "@mui/icons-material/Add";
import DeleteIcon from "@mui/icons-material/Delete";
import type { HomeCategory } from "../../types/HomeCategory.ts";
import { useState } from "react";
import HomeCategoryFormDialog from "./HomeCategoryFormDialog.tsx";
import { useAppDispatch } from "../../state/hooks.ts";
import { deleteHomeCategory } from "../../state/admin/AdminSlice.ts";
import { fetchHomePageData } from "../../state/customer/CustomerSlice.ts";

const PLACEHOLDER_IMAGES = [
    "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=600&q=80", // Fashion
    "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600&q=80", // Electronics
    "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80", // Headphones
    "https://images.unsplash.com/photo-1512436991641-6745cdb1723f?w=600&q=80", // Clothing
    "https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=600&q=80", // Shopping
    "https://images.unsplash.com/photo-1483985988355-763728e1935b?w=600&q=80", // Lifestyle
];

const HomeCategoryTable = ({ data, section = "GRID" }: { data?: HomeCategory[]; section?: string }) => {
    const dispatch = useAppDispatch();
    const [editingCategory, setEditingCategory] = useState<HomeCategory | undefined>(undefined);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
    const [categoryToDelete, setCategoryToDelete] = useState<HomeCategory | undefined>(undefined);
    const [snackbar, setSnackbar] = useState<{ open: boolean; message: string; severity: 'success' | 'error' }>({
        open: false,
        message: '',
        severity: 'success'
    });

    const handleEdit = (row: HomeCategory) => {
        setEditingCategory(row);
        setDialogOpen(true);
    };

    const handleAdd = () => {
        setEditingCategory(undefined);
        setDialogOpen(true);
    };

    const handleDeleteClick = (row: HomeCategory) => {
        setCategoryToDelete(row);
        setDeleteDialogOpen(true);
    };

    const handleDeleteConfirm = async () => {
        if (categoryToDelete?.id) {
            try {
                await dispatch(deleteHomeCategory(categoryToDelete.id)).unwrap();
                await dispatch(fetchHomePageData());
                setSnackbar({ open: true, message: 'Category deleted successfully', severity: 'success' });
            } catch (error) {
                setSnackbar({ open: true, message: 'Failed to delete category', severity: 'error' });
            }
        }
        setDeleteDialogOpen(false);
        setCategoryToDelete(undefined);
    };

    const handleDeleteCancel = () => {
        setDeleteDialogOpen(false);
        setCategoryToDelete(undefined);
    };

    const realRows = data ?? [];
    const GRID_SLOTS = section === "GRID" ? 6 : realRows.length + 1;
    const displayData = Array.from({ length: GRID_SLOTS }, (_, index) => {
        const real = realRows[index];
        if (real) {
            return { ...real, isPlaceholder: false as const };
        }
        return {
            id: `placeholder-${index}`,
            imageUrl: PLACEHOLDER_IMAGES[index % PLACEHOLDER_IMAGES.length],
            categoryId: "CATEGORY_NAME",
            name: "NAME",
            section,
            isPlaceholder: true as const,
        };
    });

    return (
        <Box sx={{ padding: { xs: 2, sm: 3 } }}>
            <Paper elevation={2}>
                <TableContainer className="overflow-x-auto">
                    <Table sx={{ minWidth: 300 }}>
                        <TableHead>
                            <TableRow
                                sx={{
                                    backgroundColor: "#000",
                                    "& th": {
                                        color: "#fff",
                                        fontWeight: 600,
                                    },
                                }}
                            >
                                <TableCell>No</TableCell>
                                <TableCell>Id</TableCell>
                                <TableCell>Image</TableCell>
                                <TableCell>Category</TableCell>
                                <TableCell>Name</TableCell>
                                <TableCell align="center">Action</TableCell>
                                <TableCell align="center">Delete</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {displayData.map((row, index) => (
                                <TableRow
                                    key={row.id}
                                    hover={!row.isPlaceholder}
                                    sx={{ height: 90 }}
                                >
                                    <TableCell>{index + 1}</TableCell>
                                    <TableCell>
                                        {row.isPlaceholder ? "-" : row.id}
                                    </TableCell>
                                    <TableCell>
                                        <Avatar
                                            variant="rounded"
                                            src={row.imageUrl}
                                            sx={{
                                                width: 50,
                                                height: 70,
                                                borderRadius: 1,
                                            }}
                                        />
                                    </TableCell>
                                    <TableCell>
                                        <Typography variant="body2">
                                            {row.categoryId}
                                        </Typography>
                                    </TableCell>
                                    <TableCell>{row.name || "-"}</TableCell>
                                    <TableCell align="center">
                                        {row.isPlaceholder ? (
                                            <IconButton color="primary" onClick={handleAdd}>
                                                <AddIcon fontSize="small" />
                                            </IconButton>
                                        ) : (
                                            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 1 }}>
                                                <IconButton
                                                    color="warning"
                                                    onClick={() => handleEdit(row as HomeCategory)}
                                                >
                                                    <EditIcon fontSize="small" sx={{ color: 'warning.main' }} />
                                                </IconButton>

                                            </Box>
                                        )}
                                    </TableCell>
                                    <TableCell align="center">
                                        {!row.isPlaceholder && (
                                            <IconButton
                                                color="error"
                                                onClick={() => handleDeleteClick(row as HomeCategory)}
                                            >
                                                <DeleteIcon fontSize="small" sx={{ color: 'error.main' }} />
                                            </IconButton>
                                        )}
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Paper>
            <HomeCategoryFormDialog
                open={dialogOpen}
                onClose={() => setDialogOpen(false)}
                onSaved={() => setDialogOpen(false)}
                category={editingCategory}
                section={section}
            />
            <Dialog open={deleteDialogOpen} onClose={handleDeleteCancel}>
                <DialogTitle>Confirm Delete</DialogTitle>
                <DialogContent>
                    <Typography>
                        Are you sure you want to delete the category "{categoryToDelete?.name || categoryToDelete?.categoryId}"?
                    </Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDeleteCancel}>Cancel</Button>
                    <Button onClick={handleDeleteConfirm} color="error" variant="contained">
                        Delete
                    </Button>
                </DialogActions>
            </Dialog>
            <Snackbar
                open={snackbar.open}
                autoHideDuration={6000}
                onClose={() => setSnackbar({ ...snackbar, open: false })}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
            >
                <Alert
                    onClose={() => setSnackbar({ ...snackbar, open: false })}
                    severity={snackbar.severity}
                    sx={{ width: '100%' }}
                >
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </Box>
    );
};

export default HomeCategoryTable;