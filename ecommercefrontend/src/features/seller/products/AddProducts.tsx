import { useState, type ChangeEvent } from "react";
import { useFormik } from "formik";
import {
    TextField,
    Button,
    MenuItem,
    Select,
    InputLabel,
    FormControl,
    Grid,
    CircularProgress,
    IconButton,
    // Snackbar,
    // Alert,
} from "@mui/material";
import AddPhotoAlternateIcon from "@mui/icons-material/AddPhotoAlternate";
import CloseIcon from "@mui/icons-material/Close";

import { electronicsLevelThree } from "../../customer/data/category/level three/electronicsLevelThree";
import { furnitureLevelThree } from "../../customer/data/category/level three/furnitureLevelThree";
import { womenLevelThree } from "../../customer/data/category/level three/womenLevelThree";
import { menLevelThree } from "../../customer/data/category/level three/menLevelThree";
import { menLevelTwo } from "../../customer/data/category/level two/menLevelTwo";
import { womenLevelTwo } from "../../customer/data/category/level two/womenLevelTwo";
import { furnitureLevelTwo } from "../../customer/data/category/level two/furnitureLevleTwo";
import { electronicsLevelTwo } from "../../customer/data/category/level two/electronicsLavelTwo";
import { uploadToCloudinary } from "../../../util/UploadToCloudinary";
import { mainCategory } from "../../customer/data/category/mainCategory";


interface CategoryItem {
    categoryId: string;
    name: string;
    parentCategoryId?: string;
}

const colors = [
    { name: "White", hex: "#FFFFFF" },
    { name: "Black", hex: "#000000" },
];

const categoryTwo: Record<string, CategoryItem[]> = {
    men: menLevelTwo,
    women: womenLevelTwo,
    kids: [],
    home_furniture: furnitureLevelTwo,
    beauty: [],
    electronics: electronicsLevelTwo,
};

const categoryThree: Record<string, CategoryItem[]> = {
    men: menLevelThree,
    women: womenLevelThree,
    kids: [],
    home_furniture: furnitureLevelThree,
    beauty: [],
    electronics: electronicsLevelThree,
};

const ProductForm = () => {
    const [uploadImage, setUploadingImage] = useState(false);


    const formik = useFormik({
        initialValues: {
            title: "",
            description: "",
            mrpPrice: "",
            sellingPrice: "",
            quantity: "",
            color: "",
            images: [] as string[],
            category: "",
            category2: "",
            category3: "",
            sizes: "",
        },
        onSubmit: async (values) => {

        },
    });

    const handleImageChange = async (event: ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (!file) return;
        setUploadingImage(true);
        const image = await uploadToCloudinary(file);
        await formik.setFieldValue("images", [...formik.values.images, image]);
        setUploadingImage(false);
    };

    const handleRemoveImage = (index: number) => {
        const updatedImages = [...formik.values.images];
        updatedImages.splice(index, 1);
        formik.setFieldValue("images", updatedImages);
    };

    const childCategory = (category: CategoryItem[], parentCategoryId: string) => {
        return category.filter((child) => child.parentCategoryId === parentCategoryId);
    };


    return (
        <div>
            <form onSubmit={formik.handleSubmit} className="space-y-4 p-4">
                <Grid container spacing={2}>
                    <Grid size={12} className="flex flex-wrap gap-5">
                        <input
                            type="file"
                            accept="image/*"
                            id="fileInput"
                            style={{ display: "none" }}
                            onChange={(e) => {
                                void handleImageChange(e);
                            }}
                        />
                        <label className="relative" htmlFor="fileInput">
              <span className="w-24 h-24 cursor-pointer flex items-center justify-center p-3 border rounded-md border-gray-400">
                <AddPhotoAlternateIcon className="text-gray-700" />
              </span>
                            {uploadImage && (
                                <div className="absolute left-0 right-0 top-0 bottom-0 w-24 h-24 flex justify-center items-center">
                                    <CircularProgress />
                                </div>
                            )}
                        </label>
                        <div className="flex flex-wrap gap-2">
                            {formik.values.images.map((image, index) => (
                                <div key={index} className="relative">
                                    <img className="w-24 h-24 object-cover" src={image} alt="Product" />
                                    <IconButton
                                        onClick={() => handleRemoveImage(index)}
                                        size="small"
                                        color="error"
                                        sx={{ position: "absolute", top: 0, right: 0 }}
                                    >
                                        <CloseIcon sx={{ fontSize: "1rem" }} />
                                    </IconButton>
                                </div>
                            ))}
                        </div>
                    </Grid>
                    <Grid size={12}>
                        <TextField
                            fullWidth
                            id="title"
                            name="title"
                            label="Title"
                            value={formik.values.title}
                            onChange={formik.handleChange}
                            required
                        />
                    </Grid>
                    <Grid size={12}>
                        <TextField
                            multiline
                            rows={4}
                            fullWidth
                            id="description"
                            name="description"
                            label="Description"
                            value={formik.values.description}
                            onChange={formik.handleChange}
                            required
                        />
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6, md: 3 }}>
                        <TextField
                            fullWidth
                            id="mrpPrice"
                            name="mrpPrice"
                            label="MRP Price"
                            type="number"
                            value={formik.values.mrpPrice}
                            onChange={formik.handleChange}
                            required
                        />
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6, md: 3 }}>
                        <TextField
                            fullWidth
                            id="sellingPrice"
                            name="sellingPrice"
                            label="Selling Price"
                            type="number"
                            value={formik.values.sellingPrice}
                            onChange={formik.handleChange}
                            required
                        />
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6, md: 3 }}>
                        <FormControl fullWidth required>
                            <InputLabel>Color</InputLabel>
                            <Select
                                name="color"
                                value={formik.values.color}
                                onChange={formik.handleChange}
                                label="Color"
                            >
                                {colors.map((color) => (
                                    <MenuItem key={color.name} value={color.name}>
                                        <div className="flex gap-3">
                                            <span style={{ backgroundColor: color.hex }} className="h-5 w-5 rounded-full border"></span>
                                            <p>{color.name}</p>
                                        </div>
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6, md: 3 }}>
                        <FormControl fullWidth required>
                            <InputLabel>Sizes</InputLabel>
                            <Select
                                name="sizes"
                                value={formik.values.sizes}
                                onChange={formik.handleChange}
                                label="Sizes"
                            >
                                {["FREE", "S", "M", "L", "XL"].map((size) => (
                                    <MenuItem key={size} value={size}>{size}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6, md: 4 }}>
                        <FormControl fullWidth required>
                            <InputLabel>Category</InputLabel>
                            <Select
                                name="category"
                                value={formik.values.category}
                                onChange={formik.handleChange}
                                label="Category"
                            >
                                {mainCategory.map((item) => (
                                    <MenuItem key={item.categoryId} value={item.categoryId}>{item.name}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6, md: 4 }}>
                        <FormControl fullWidth required>
                            <InputLabel>Second Category</InputLabel>
                            <Select
                                name="category2"
                                value={formik.values.category2}
                                onChange={formik.handleChange}
                                label="Second Category"
                            >
                                {categoryTwo[formik.values.category]?.map((item) => (
                                    <MenuItem key={item.categoryId} value={item.categoryId}>{item.name}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6, md: 4 }}>
                        <FormControl fullWidth required>
                            <InputLabel>Third Category</InputLabel>
                            <Select
                                name="category3"
                                value={formik.values.category3}
                                onChange={formik.handleChange}
                                label="Third Category"
                            >
                                {childCategory(categoryThree[formik.values.category] || [], formik.values.category2).map((item) => (
                                    <MenuItem key={item.categoryId} value={item.categoryId}>{item.name}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid size={12}>
                        <Button
                            sx={{ p: "14px" }}
                            variant="contained"
                            fullWidth
                            type="submit"
                            // disabled={sellerProduct.loading}
                        >
                            {false? <CircularProgress size={24} /> : "Add Product"}
                        </Button>
                    </Grid>
                </Grid>
            </form>
            {/*<Snackbar*/}
            {/*    anchorOrigin={{ vertical: "top", horizontal: "right" }}*/}
            {/*    open={snackbarOpen}*/}
            {/*    autoHideDuration={6000}*/}
            {/*    onClose={handleCloseSnackbar}*/}
            {/*>*/}
            {/*    <Alert onClose={handleCloseSnackbar} severity={true? "error" : "success"} variant="filled">*/}
            {/*        {sellerProduct.error ? sellerProduct.error : "Product created successfully"}*/}
            {/*    </Alert>*/}
            {/*</Snackbar>*/}
        </div>
    );
};

export default ProductForm;