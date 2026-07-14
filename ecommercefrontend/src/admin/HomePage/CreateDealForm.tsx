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
import {createDeal, clearDealError} from "../../state/admin/DealSlice.ts";
import {fetchHomeCategories} from "../../state/admin/AdminSlice.ts";
import {useEffect} from "react";

const CreateDealForm = () => {
    const dispatch = useAppDispatch();
    const {homeCategory, deal} = useAppSelector(store=>store);

    useEffect(() => {
        dispatch(fetchHomeCategories());
        dispatch(clearDealError());
    }, [dispatch]);

    console.log("Home categories state:", homeCategory);
    console.log("Categories array:", homeCategory.categories);
    console.log("Categories length:", homeCategory.categories?.length);
    console.log("Loading:", homeCategory.loading);
    console.log("Category error:", homeCategory.error);

    const formik = useFormik({
        initialValues:{
            discount:0,
            category:""
        },
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
            dispatch(createDeal(reqData));
        }
    })

    useEffect(() => {
        if (deal.dealCreated) {
            formik.resetForm();
        }
    }, [deal.dealCreated]);

    const categoryAlreadyHasDeal = Boolean(deal.error) && deal.error.toLowerCase().includes("category");

    return (
        <Box
            component="form"
            onSubmit={formik.handleSubmit}
            sx={{ maxWidth: 500, margin: "auto", padding: { xs: 2, sm: 3 } }}
            className="flex flex-col gap-4"
        >
            <Typography className='text-center' variant="h4" gutterBottom sx={{ fontSize: { xs: '1.5rem', sm: '2rem' } }}>
                Create Deal
            </Typography>
            {deal.dealCreated && (
                <Alert severity="success">Deal created successfully</Alert>
            )}
            {deal.error && !categoryAlreadyHasDeal && (
                <Alert severity="error">{deal.error}</Alert>
            )}
            {homeCategory.error && (
                <Alert severity="error">Failed to load categories: {homeCategory.error}</Alert>
            )}
            {!homeCategory.loading && (!homeCategory.categories || homeCategory.categories.length === 0) && (
                <Alert severity="warning">
                    No categories available. Please create categories first before creating deals.
                </Alert>
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
            <Button
                color="primary"
                variant="contained"
                fullWidth
                type="submit"
                disabled={deal.loading || !homeCategory.categories || homeCategory.categories.length === 0}
                sx={{ py: ".9rem" }}
            >
                {deal.loading ? "Submitting..." : "Submit"}
            </Button>
        </Box>
    )
}
export default CreateDealForm