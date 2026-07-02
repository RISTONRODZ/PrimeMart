
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { Box, TextField, Grid, Button } from '@mui/material';

interface AddressFormValues {
    name: string;
    mobile: string;
    pinCode: string;
    address: string;
    city: string;
    state: string;
    locality: string;
}

const AddressFormSchema = Yup.object().shape({
    name: Yup.string().required("Name is required"),
    mobile: Yup.string().required("Mobile number is required").matches(/^[6-9]\d{9}$/, "Invalid mobile number"),
    pinCode: Yup.string().required("Pin code is required").matches(/^[1-9][0-9]{5}$/, "Invalid pin code"),
    address: Yup.string().required("Address is required"),
    city: Yup.string().required("City is required"),
    state: Yup.string().required("State is required"),
    locality: Yup.string().required("Locality is required"),
});

const AddressForm = () => {
    const formik = useFormik<AddressFormValues>({
        initialValues: {
            name: '',
            mobile: '',
            pinCode: '',
            address: '',
            city: '',
            state: '',
            locality: '',
        },
        validationSchema: AddressFormSchema,
        onSubmit: (values) => {
            console.log(values);
        },
    });

    const renderTextField = (name: keyof AddressFormValues, label: string) => (
        <TextField
            fullWidth
            name={name}
            label={label}
            value={formik.values[name]}
            onChange={formik.handleChange}
            error={formik.touched[name] && Boolean(formik.errors[name])}
            helperText={formik.touched[name] && formik.errors[name]}
        />
    );

    return (
        <Box sx={{ width: '100%' }}>
            <p className='text-xl font-bold text-center pb-5'>Contact Details</p>
            <form onSubmit={formik.handleSubmit}>
                <Grid container spacing={2}>
                    <Grid size={{ xs: 12 }}>{renderTextField('name', 'Name')}</Grid>
                    <Grid size={{ xs: 6 }}>{renderTextField('mobile', 'Mobile Number')}</Grid>
                    <Grid size={{ xs: 6 }}>{renderTextField('pinCode', 'Pin Code')}</Grid>
                    <Grid size={{ xs: 12 }}>{renderTextField('address', 'Address')}</Grid>
                    <Grid size={{ xs: 6 }}>{renderTextField('city', 'City')}</Grid>
                    <Grid size={{ xs: 6 }}>{renderTextField('state', 'State')}</Grid>
                    <Grid size={{ xs: 12 }}>{renderTextField('locality', 'Locality')}</Grid>
                    <Grid size={{ xs: 12 }}>
                        <Button color="primary" variant="contained" fullWidth type="submit">
                            Submit
                        </Button>
                    </Grid>
                </Grid>
            </form>
        </Box>
    );
};

export default AddressForm;