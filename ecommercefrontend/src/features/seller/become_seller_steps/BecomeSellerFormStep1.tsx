import { Box, TextField } from "@mui/material";
import type {FormikProps} from "formik";
import type {SellerFormValues} from "../login/useSellerForm.tsx";

interface BecomeSellerFormStep1Props {
  formik: FormikProps<SellerFormValues>;
}

const BecomeSellerFormStep1 = ({ formik }: BecomeSellerFormStep1Props) => {



    return (
        <Box  >
            <p className="text-xl font-bold text-center pb-9">Contact Details</p>

            <div className="flex flex-col gap-10">

                <TextField
                    fullWidth
                    name="mobile"
                    label="Mobile"
                    value={formik.values.mobile}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={formik.touched.mobile && Boolean(formik.errors.mobile)}
                    helperText={formik.touched.mobile && formik.errors.mobile}
                />

                {/* <div className="space-y-2">
                    <p className="font-medium text-sm">
                        * Enter OTP sent to your mobile number
                    </p>
                    <OTPInput
                        length={6}
                        onChange={handleOtpChange}
                        error={false}
                    />
                    <p className="text-xs space-x-2">
                        Didn’t receive OTP?{" "}
                        <span onClick={handleResendOTP} className="text-teal-600 cursor-pointer hover:text-teal-800 font-semibold">
                            Resend OTP
                        </span>
                    </p>
                </div> */}

                <TextField
                    fullWidth
                    name="GSTIN"
                    label="GSTIN Number"
                    value={formik.values.gstin}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={formik.touched.gstin && Boolean(formik.errors.gstin)}
                    helperText={formik.touched.gstin && formik.errors.gstin}
                />
            </div>


        </Box>
    );
};

export default BecomeSellerFormStep1;
