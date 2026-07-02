import { Box, TextField, Button, Grid, Typography } from '@mui/material';
import { useFormik } from 'formik';
import dayjs, { Dayjs } from 'dayjs';

interface CouponFormValues {
    code: string;
    discountPercentage: number;
    validityStartDate: Dayjs | null;
    validityEndDate: Dayjs | null;
    minimumOrderValue: number;
}

const AddNewCouponForm = () => {
    const formik = useFormik<CouponFormValues>({
        initialValues: {
            code: '',
            discountPercentage: 0,
            validityStartDate: null,
            validityEndDate: null,
            minimumOrderValue: 0,
        },
        onSubmit: (values) => {
            const formattedValues = {
                ...values,
                validityStartDate: values.validityStartDate ? values.validityStartDate.toISOString() : null,
                validityEndDate: values.validityEndDate ? values.validityEndDate.toISOString() : null,
            };
            console.log('Form Submitted:', formattedValues);
        },
    });

    return (
        <Box component="form" onSubmit={formik.handleSubmit} sx={{ p: { xs: 2, sm: 3 }, maxWidth: 600, mx: 'auto' }}>
            <Typography variant="h6" gutterBottom>Create Coupon</Typography>

            <Grid container spacing={2}>
                <Grid size={{ xs: 12, sm: 6 }}>
                    <TextField
                        fullWidth
                        name="code"
                        label="Coupon Code"
                        value={formik.values.code}
                        onChange={formik.handleChange}
                    />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                    <TextField
                        fullWidth
                        name="discountPercentage"
                        label="Discount Percentage"
                        type="number"
                        value={formik.values.discountPercentage}
                        onChange={formik.handleChange}
                    />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                    <TextField
                        fullWidth
                        name="validityStartDate"
                        label="Validity Start Date"
                        type="date"
                        slotProps={{ inputLabel: { shrink: true } }}
                        value={formik.values.validityStartDate ? formik.values.validityStartDate.format('YYYY-MM-DD') : ''}
                        onChange={(e) => formik.setFieldValue('validityStartDate', dayjs(e.target.value))}
                    />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                    <TextField
                        fullWidth
                        name="validityEndDate"
                        label="Validity End Date"
                        type="date"
                        slotProps={{ inputLabel: { shrink: true } }}
                        value={formik.values.validityEndDate ? formik.values.validityEndDate.format('YYYY-MM-DD') : ''}
                        onChange={(e) => formik.setFieldValue('validityEndDate', dayjs(e.target.value))}
                    />
                </Grid>
                <Grid size={12}>
                    <TextField
                        fullWidth
                        name="minimumOrderValue"
                        label="Minimum Order Value"
                        type="number"
                        value={formik.values.minimumOrderValue}
                        onChange={formik.handleChange}
                    />
                </Grid>
                <Grid size={12}>
                    <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>
                        CREATE COUPON
                    </Button>
                </Grid>
            </Grid>
        </Box>
    );
};

export default AddNewCouponForm;