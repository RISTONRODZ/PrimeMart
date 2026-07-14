import { useState } from 'react';
import { Box, TextField, Button, Grid, Typography, CircularProgress, Snackbar, Alert } from '@mui/material';
import { useFormik } from 'formik';
import dayjs, { Dayjs } from 'dayjs';
import { useAppDispatch, useAppSelector } from '../../../state/hooks.ts';
import { createCoupon } from '../../../state/customer/couponSlice.ts';

interface CouponFormValues {
    code: string;
    discountPercentage: number;
    validityStartDate: Dayjs | null;
    validityEndDate: Dayjs | null;
    minimumOrderValue: number;
}

const AddNewCouponForm = () => {
    const dispatch = useAppDispatch();
    const couponState = useAppSelector((state) => state.coupon);
    const loading = couponState?.loading || false;
    const error = couponState?.error || null;
    const jwt = useAppSelector((state) => state.auth.jwt);

    const [snackbar, setSnackbar] = useState<{ open: boolean; message: string; severity: 'success' | 'error' }>({
        open: false,
        message: '',
        severity: 'success',
    });

    const formik = useFormik<CouponFormValues>({
        initialValues: {
            code: '',
            discountPercentage: 0,
            validityStartDate: null,
            validityEndDate: null,
            minimumOrderValue: 0,
        },
        onSubmit: async (values) => {
            if (!jwt) {
                setSnackbar({ open: true, message: 'Authentication required', severity: 'error' });
                return;
            }

            const formattedValues = {
                ...values,
                validityStartDate: values.validityStartDate ? values.validityStartDate.toISOString() : null,
                validityEndDate: values.validityEndDate ? values.validityEndDate.toISOString() : null,
                isActive: true,
            };

            const result = await dispatch(createCoupon({ coupon: formattedValues, jwt }));

            if (createCoupon.fulfilled.match(result)) {
                setSnackbar({ open: true, message: 'Coupon created successfully!', severity: 'success' });
                formik.resetForm();
            } else {
                setSnackbar({ open: true, message: result.payload as string || 'Failed to create coupon', severity: 'error' });
            }
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
                    <Button
                        type="submit"
                        variant="contained"
                        fullWidth
                        sx={{ mt: 2 }}
                        disabled={loading}
                        startIcon={loading ? <CircularProgress size={20} /> : null}
                    >
                        {loading ? 'CREATING...' : 'CREATE COUPON'}
                    </Button>
                </Grid>
            </Grid>
            <Snackbar
                open={snackbar.open}
                autoHideDuration={4000}
                onClose={() => setSnackbar((s) => ({ ...s, open: false }))}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
                sx={{
                    '& .MuiSnackbar-root': {
                        borderRadius: 2,
                    },
                }}
            >
                <Alert
                    severity={snackbar.severity}
                    variant="filled"
                    onClose={() => setSnackbar((s) => ({ ...s, open: false }))}
                    sx={{
                        borderRadius: 2,
                        fontWeight: 600,
                        boxShadow: 3,
                    }}
                >
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </Box>
    );
};

export default AddNewCouponForm;