import { useState, useEffect } from 'react';
import { Button, TextField } from '@mui/material';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import {login, sendOtp} from "../../../state/slice/AuthSlice.ts";
import { useAppDispatch, useAppSelector } from "../../../state/hooks";
import { useNavigate, useLocation } from 'react-router-dom';

const SellerLoginForm = () => {
    const [isOtpSent, setIsOtpSent] = useState(false);
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const location = useLocation();
    const { isAuthenticated, jwt } = useAppSelector((state) => state.auth);

    useEffect(() => {
        if (isAuthenticated && jwt) {
            const from = (location.state as any)?.from || "/seller";
            navigate(from, { replace: true });
        }
    }, [isAuthenticated, jwt, navigate, location]);

    const formik = useFormik({
        initialValues: { email: "", otp: "" },
        validationSchema: Yup.object({
            email: Yup.string().email("Invalid email").required("Required"),
            otp: isOtpSent ? Yup.string().required("Required") : Yup.string()
        }),
        onSubmit: (values) => {
            const sellerEmail = `seller_${values.email}`;
            dispatch(login({ email: sellerEmail, otp: values.otp }));
        }
    });

    const handleSendOtp = () => {
        if (formik.values.email && !formik.errors.email) {
            const sellerEmail = `seller_${formik.values.email}`;
            dispatch(sendOtp({ email: sellerEmail }));
            setIsOtpSent(true);
        }
    };
    return (
        <form onSubmit={formik.handleSubmit} className='flex flex-col gap-5'>
            <h1 className='text-center font-bold text-xl text-blue-700 pb-5'>Login As Seller</h1>

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

            {!isOtpSent ? (
                <Button fullWidth variant="contained" color="secondary" onClick={handleSendOtp}>
                    Send OTP
                </Button>
            ) : (
                <TextField
                    fullWidth
                    name="otp"
                    label="OTP"
                    value={formik.values.otp}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={formik.touched.otp && Boolean(formik.errors.otp)}
                    helperText={formik.touched.otp && formik.errors.otp}
                />
            )}

            {isOtpSent && (
                <Button type="submit" fullWidth variant="contained" color="primary">
                    Login
                </Button>
            )}
        </form>
    );
};
export default SellerLoginForm;