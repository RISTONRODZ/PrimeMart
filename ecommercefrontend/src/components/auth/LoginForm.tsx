import { useFormik } from "formik";
import * as Yup from "yup";
import { login, sendOtp } from "../../state/slice/AuthSlice.ts";
import {Button, CircularProgress, TextField} from "@mui/material";
import { useState, useEffect } from "react";
import {useAppDispatch, useAppSelector} from "../../state/hooks.ts";
import { useNavigate, useLocation } from "react-router-dom";

const LoginForm = () => {
    const dispatch = useAppDispatch();
    const [isOtpSent, setIsOtpSent] = useState(false);
    const {auth} = useAppSelector(store => store)
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        if (auth.isAuthenticated && auth.jwt) {
            const from = (location.state as any)?.from || "/";
            navigate(from, { replace: true });
        }
    }, [auth.isAuthenticated, auth.jwt, navigate, location]);

    const formik = useFormik({
        initialValues: { email: "", otp: "" },
        validationSchema: Yup.object({
            email: Yup.string().email("Invalid email").required("Required"),
            otp: isOtpSent ? Yup.string().required("Required") : Yup.string()
        }),
        onSubmit: (values) => {
            const sellerEmail = `${values.email}`;
            dispatch(login({ email: sellerEmail, otp: values.otp }));
        }
    });

    const handleSendOtp = () => {
        if (formik.values.email && !formik.errors.email) {
            const sellerEmail = `${formik.values.email}`;
            dispatch(sendOtp({ email: sellerEmail }));
            setIsOtpSent(true);
        }
    };

    return (
        <form onSubmit={formik.handleSubmit} className={'flex flex-col gap-4'}>
            <div className={'text-center font-bold text-2xl mt-5 text-blue-700'}>
                Login Form
            </div>

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
                <Button fullWidth variant="contained" color="secondary" onClick={handleSendOtp} disabled={auth.loading}>
                    {auth.loading ? <CircularProgress size={24} color="inherit" /> : "Send OTP"}
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
                <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    color="primary"
                    disabled={auth.loading}
                >
                    {auth.loading ? <CircularProgress size={24} color="inherit" /> : "Login"}
                </Button>
            )}
        </form>
    );
};

export default LoginForm;