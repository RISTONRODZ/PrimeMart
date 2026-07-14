import React from "react";
import { TextField } from "@mui/material";
import type {FormikProps} from "formik";
import type {SellerFormValues} from "../login/useSellerForm.tsx";

interface BecomeSellerFormStep3Props {
  formik: FormikProps<SellerFormValues>;
}

const BecomeSellerFormStep3: React.FC<BecomeSellerFormStep3Props> = ({ formik }) => {
  return (
    <div className="flex flex-col gap-5">
       
          <TextField required
            fullWidth
            name="bankDetails.accountNumber"
            label="Account Number"
            value={formik.values.bankDetails.accountNumber}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched.bankDetails?.accountNumber && Boolean(formik.errors.bankDetails?.accountNumber)}
            helperText={formik.touched.bankDetails?.accountNumber && formik.errors.bankDetails?.accountNumber}
          />
          <TextField
              required
            fullWidth
            name="bankDetails.ifscCode"
            label="IFSC Code"
            value={formik.values.bankDetails.ifscCode}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched.bankDetails?.ifscCode && Boolean(formik.errors.bankDetails?.ifscCode)}
            helperText={formik.touched.bankDetails?.ifscCode && formik.errors.bankDetails?.ifscCode}
          />
          <TextField
            required
            fullWidth
            name="bankDetails.accountHolderName"
            label="Account Holder Name"
            value={formik.values.bankDetails.accountHolderName}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched.bankDetails?.accountHolderName && Boolean(formik.errors.bankDetails?.accountHolderName)}
            helperText={formik.touched.bankDetails?.accountHolderName && formik.errors.bankDetails?.accountHolderName}
          />
    </div>
  );
};

export default BecomeSellerFormStep3;
