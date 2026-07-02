import { TextField } from '@mui/material';
import { useFormik } from 'formik';

const SellerLoginForm = () => {
    const formik = useFormik({
        initialValues: {
            email: "",
            otp: ""
        },
        onSubmit: (values) => {
            console.log("form data ", values);
        }
    });

    return (
        <div>
            <h1 className='text-center font-bold text-xl text-blue-700 pb-5'>Login As Seller</h1>
            <div className='flex flex-col gap-5'>
                <TextField
                    fullWidth
                    name="email"
                    label="Email"
                    value={formik.values.email}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={formik.touched.email && Boolean(formik.errors.email)}
                    helperText={formik.touched.email && formik.errors.email}
                />

                {(
                    <div className='space-y-2'>
                        <p className={'font medium text-sm opacity-70'}>Enter OTP sent to your email</p>
                        <TextField
                            fullWidth
                            name="otp"
                            label="Otp"
                            value={formik.values.otp}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            error={formik.touched.otp && Boolean(formik.errors.otp)}
                            helperText={formik.touched.otp && formik.errors.otp}
                        />
                    </div>
                )}
            </div>
        </div>
    );
};

export default SellerLoginForm;