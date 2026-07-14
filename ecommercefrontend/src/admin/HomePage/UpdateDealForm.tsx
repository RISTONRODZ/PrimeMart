import {
    Box,
    Button,
    FormControl,
    FormHelperText,
    InputLabel,
    MenuItem,
    Select,
    TextField,
    Typography,
    Alert
} from '@mui/material'
import { useFormik } from 'formik';
import {useAppDispatch, useAppSelector} from "../../state/hooks.ts";
import {updateDeal, clearDealError} from "../../state/admin/DealSlice.ts";
import {fetchHomeCategories} from "../../state/admin/AdminSlice.ts";
import {useEffect} from "react";
import type {Deal} from "../../types/DealTypes.ts";

interface UpdateDealFormProps {
    id: number;
    onClose: () => void;
}

const UpdateDealForm = ({ id, onClose }: UpdateDealFormProps) => {
    const dispatch = useAppDispatch();
    const {homeCategory, deal} = useAppSelector(store=>store);
    
    // Find the existing deal
    const existingDeal = deal.deals?.find(d => d.id === id);

    useEffect(() => {
        dispatch(fetchHomeCategories());
        dispatch(clearDealError());
    }, [dispatch]);

    const formik = useFormik({
        initialValues:{
            discount: existingDeal?.discount || 0,
            category: existingDeal?.homeCategoryId || ""
        },
        enableReinitialize: true,
        validate:(values)=>{
            const errors:any={};
            if(!values.category){
                errors.category="Category is required";
            }
            return errors;
        },
        onSubmit:(values)=>{
            const reqData={
                discount:values.discount,
                homeCategoryId:values.category
            }
            dispatch(updateDeal({id, deal: reqData}));
            setTimeout(() => {
                if (!deal.error) {
                    onClose();
                }
            }, 500);
        }
    })

    const categoryAlreadyHasDeal = Boolean(deal.error) && deal.error.toLowerCase().includes("category");

    return (
        <Box
            component="form"
            onSubmit={formik.handleSubmit}
            sx={{ maxWidth: 500, margin: "auto", padding: { xs: 2, sm: 3 } }}
            className="flex flex-col gap-4"
        >
            <Typography className='text-center' variant="h5" gutterBottom>
                Update Deal
            </Typography>
            {deal.dealUpdated && (
                <Alert severity="success">Deal updated successfully</Alert>
            )}
            {deal.error && !categoryAlreadyHasDeal && (
                <Alert severity="error">{deal.error}</Alert>
            )}
            {homeCategory.error && (
                <Alert severity="error">Failed to load categories: {homeCategory.error}</Alert>
            )}
            <TextField
                fullWidth
                id="discount"
                name="discount"
                label="Discount"
                type="number"
                value={formik.values.discount}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                error={formik.touched.discount && Boolean(formik.errors.discount)}
                helperText={formik.touched.discount && formik.errors.discount}
            />
            <FormControl
                fullWidth
                error={(formik.touched.category && Boolean(formik.errors.category)) || categoryAlreadyHasDeal}
                required
            >
                <InputLabel id="category-label">Category</InputLabel>
                <Select
                    labelId="category-label"
                    id="category"
                    name="category"
                    value={formik.values.category}
                    onChange={formik.handleChange}
                    label="Category"
                    disabled={homeCategory.loading}
                >
                    {homeCategory.categories?.length > 0 ? (
                        homeCategory.categories.map((category) => (
                            <MenuItem key={category.id} value={category.id}>
                                {category.name}
                            </MenuItem>
                        ))
                    ) : (
                        <MenuItem disabled value="">
                            {homeCategory.loading ? "Loading categories..." : "No categories available"}
                        </MenuItem>
                    )}
                </Select>
                {formik.touched.category && formik.errors.category && (
                    <FormHelperText>{formik.errors.category}</FormHelperText>
                )}
                {categoryAlreadyHasDeal && (
                    <FormHelperText>{deal.error}</FormHelperText>
                )}
            </FormControl>
            <div className="flex gap-2">
                <Button
                    color="primary"
                    variant="contained"
                    fullWidth
                    type="submit"
                    disabled={deal.loading}
                    sx={{ py: ".9rem" }}
                >
                    {deal.loading ? "Updating..." : "Update"}
                </Button>
                <Button
                    color="secondary"
                    variant="outlined"
                    fullWidth
                    onClick={onClose}
                    sx={{ py: ".9rem" }}
                >
                    Cancel
                </Button>
            </div>
        </Box>
    )
}
export default UpdateDealForm
