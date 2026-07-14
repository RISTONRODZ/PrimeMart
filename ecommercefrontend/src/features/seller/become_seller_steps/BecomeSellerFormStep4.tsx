import { TextField } from '@mui/material'
import type {FormikProps} from "formik";
import type {SellerFormValues} from "../login/useSellerForm.tsx";

interface BecomeSellerFormStep4Props {
  formik: FormikProps<SellerFormValues>;
}

const BecomeSellerFormStep4 = ({ formik }: BecomeSellerFormStep4Props) => {
  return (
    <div className='flex flex-col gap-5'>

      <TextField
        required
        fullWidth
        name="businessDetails.businessName"
        label="Business Name"
        value={formik.values.businessDetails.businessName}
        onChange={formik.handleChange}
        onBlur={formik.handleBlur}
        error={formik.touched?.businessDetails?.businessName && Boolean(formik.errors?.businessDetails?.businessName)}
        helperText={formik.touched?.businessDetails?.businessName && formik.errors?.businessDetails?.businessName}
      />

      <TextField
        required={true}
        fullWidth
        name="sellerName"
        label="Seller Name"
        value={formik.values.sellerName}
        onChange={formik.handleChange}
        onBlur={formik.handleBlur}
        error={formik.touched.sellerName && Boolean(formik.errors.sellerName)}
        helperText={formik.touched.sellerName && formik.errors.sellerName}
      />

      <TextField
        required
        fullWidth
        name="email"
        label="Email"
        value={formik.values.email}
        onChange={formik.handleChange}
        onBlur={formik.handleBlur}
        error={formik.touched.email && Boolean(formik.errors.email)}
        helperText={formik.touched.email && formik.errors.email}
      />
        <TextField
            required
            fullWidth
            name="businessDetails.businessEmail"
            label="Business Email"
            value={formik.values.businessDetails.businessEmail}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched?.businessDetails?.businessEmail && Boolean(formik.errors?.businessDetails?.businessEmail)}
            helperText={formik.touched?.businessDetails?.businessEmail && formik.errors?.businessDetails?.businessEmail}
        />

        <TextField
            required
            fullWidth
            name="businessDetails.businessMobile"
            label="Business Mobile"
            value={formik.values.businessDetails.businessMobile}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched?.businessDetails?.businessMobile && Boolean(formik.errors?.businessDetails?.businessMobile)}
            helperText={formik.touched?.businessDetails?.businessMobile && formik.errors?.businessDetails?.businessMobile}
        />

        <TextField
            required
            fullWidth
            multiline
            rows={2}
            name="businessDetails.businessAddress"
            label="Business Address"
            value={formik.values.businessDetails.businessAddress}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched?.businessDetails?.businessAddress && Boolean(formik.errors?.businessDetails?.businessAddress)}
            helperText={formik.touched?.businessDetails?.businessAddress && formik.errors?.businessDetails?.businessAddress}
        />
      <TextField
        required
        fullWidth
        name="password"
        label="Password"
        value={formik.values.password}
        onChange={formik.handleChange}
        onBlur={formik.handleBlur}
        error={formik.touched?.password && Boolean(formik.errors?.password)}
        helperText={formik.touched?.password && formik.errors?.password}
      />





    </div>
  )
}

export default BecomeSellerFormStep4