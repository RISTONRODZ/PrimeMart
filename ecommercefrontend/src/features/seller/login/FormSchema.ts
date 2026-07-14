import * as Yup from "yup";

const mobileRegex = /^[6-9]\d{9}$/;
const mobileField = (label = "Mobile number") =>
    Yup.string()
        .trim()
        .required(`${label} is required`)
        .matches(mobileRegex, "Enter a valid 10-digit mobile number");

export const step1Schema = Yup.object({
    mobile: mobileField(),
    gstin: Yup.string()
        .trim()
        .required("GSTIN is required")
        .uppercase()
        .matches(
            /^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][1-9A-Z]Z[0-9A-Z]$/,
            "Enter a valid 15-character GSTIN"
        ),
});

export const step2Schema = Yup.object({
    pickupAddress: Yup.object({
        name: Yup.string().trim().required("Name is required"),
        mobile: mobileField(),
        pincode: Yup.string()
            .trim()
            .required("Pincode is required")
            .matches(/^[1-9][0-9]{5}$/, "Enter a valid 6-digit pincode"),
        address: Yup.string().trim().required("Address is required"),
        locality: Yup.string().trim().required("Locality is required"),
        city: Yup.string().trim().required("City is required"),
        state: Yup.string().trim().required("State is required"),
    }),
});

export const step3Schema = Yup.object({
    bankDetails: Yup.object({
        accountHolderName: Yup.string().trim().required("Account holder name is required"),
        accountNumber: Yup.string()
            .trim()
            .required("Account number is required")
            .matches(/^\d{9,18}$/, "Enter a valid account number"),
        ifscCode: Yup.string()
            .trim()
            .uppercase()
            .required("IFSC code is required")
            .matches(/^[A-Z]{4}0[A-Z0-9]{6}$/, "Enter a valid IFSC code"),
    }),
});

export const step4Schema = Yup.object({
    sellerName: Yup.string().trim().required("Seller name is required").min(2, "Seller name must be at least 2 characters").max(100, "Seller name must not exceed 100 characters"),
    email: Yup.string().trim().email("Enter a valid email").required("Email is required"),
    password: Yup.string().required("Password is required").min(8, "Password must be at least 8 characters"),
    businessDetails: Yup.object({
        businessName: Yup.string().trim().required("Business name is required"),
        businessEmail: Yup.string().trim().email("Enter a valid business email").required("Business email is required"),
        businessMobile: mobileField("Business mobile"),
        businessAddress: Yup.string().trim().required("Business address is required"),
        logo: Yup.string().nullable(),
        banner: Yup.string().nullable(),
    }),
});

export const fullFormSchema = step1Schema.concat(step2Schema).concat(step3Schema).concat(step4Schema);