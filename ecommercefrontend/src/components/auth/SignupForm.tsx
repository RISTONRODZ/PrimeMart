import {useAppDispatch} from "../../state/hooks.ts";
import {useState} from "react";
import {useFormik} from "formik";
import * as Yup from "yup";
import {login, sendOtp} from "../../state/slice/AuthSlice.ts";
import {Button, TextField} from "@mui/material";

const SignupForm = () => {
    const dispatch = useAppDispatch();
    const [isOtpSent, setIsOtpSent] = useState(true);

    const formik = useFormik({
        initialValues: { email: "", otp: "",
            fullName: ""
        },
        validationSchema: Yup.object({
            email: Yup.string().email("Invalid email").required("Required"),
            otp: isOtpSent ? Yup.string().required("Required") : Yup.string(),
            fullName: Yup.string().required("Required")
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
        <form onSubmit={formik.handleSubmit} className={'flex flex-col gap-4'}>
            <div className={'text-center font-bold text-2xl mt-5 text-blue-700'}>
                Signup Form
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
                disabled={isOtpSent}
            />
            <TextField
                fullWidth
                name="fullName"
                label="Full Name"
                value={formik.values.fullName}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                error={formik.touched.fullName && Boolean(formik.errors.fullName)}
                helperText={formik.touched.fullName && formik.errors.fullName}
                disabled={isOtpSent}
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
                    Signup
                </Button>
            )}
        </form>
    );
};

export default SignupForm;