import { Box, TextField } from "@mui/material";
import type { FormikProps } from "formik";
import type { SellerFormValues } from "../login/useSellerForm.tsx";

interface BecomeSellerFormStep1Props {
    formik: FormikProps<SellerFormValues>;
}

const BecomeSellerFormStep1 = ({ formik }: BecomeSellerFormStep1Props) => {
    return (
        <Box>
            <p className="text-xl font-bold text-center pb-9">Contact Details</p>

            <div className="flex flex-col gap-10">
                <TextField
                    required
                    fullWidth
                    name="mobile"
                    label="Mobile"
                    value={formik.values.mobile}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    // Validation Logic
                    error={formik.touched.mobile && Boolean(formik.errors.mobile)}
                    helperText={formik.touched.mobile && formik.errors.mobile}
                />

                <TextField
                    required
                    fullWidth
                    name="gstin"
                    label="GSTIN Number"
                    value={formik.values.gstin}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    // Validation Logic
                    error={formik.touched.gstin && Boolean(formik.errors.gstin)}
                    helperText={formik.touched.gstin && formik.errors.gstin}
                />
            </div>
        </Box>
    );
};

export default BecomeSellerFormStep1;