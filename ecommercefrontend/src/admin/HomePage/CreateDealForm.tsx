import { Box, Button, FormControl, FormHelperText, InputLabel, Select, TextField, Typography } from '@mui/material'
import { useFormik } from 'formik';

const CreateDealForm = () => {
    const formik = useFormik({
        initialValues:{
            discount:0,
            category:""
        },
        onSubmit:()=>{
            console.log("Form submitted with values:", formik.values);
        }
    })
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
                error={formik.touched.category && Boolean(formik.errors.category)}
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
                >

                </Select>
                {formik.touched.category && formik.errors.category && (
                    <FormHelperText>{formik.errors.category}</FormHelperText>
                )}
            </FormControl>

            <Button
                color="primary"
                variant="contained"
                fullWidth
                type="submit"
                sx={{ py: ".9rem" }}
            >
                Submit
            </Button>
        </Box>
    )
}

export default CreateDealForm