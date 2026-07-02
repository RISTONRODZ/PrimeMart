import { useFormik } from 'formik';

export interface SellerFormValues {
    mobile: string;
    otp: string;
    gstin: string;
    pickupAddress: {
        name: string;
        mobile: string;
        pincode: string;
        address: string;
        locality: string;
        city: string;
        state: string;
    };
    bankDetails: {
        accountNumber: string;
        ifscCode: string;
        accountHolderName: string;
    };
    sellerName: string;
    email: string;
    businessDetails: {
        businessName: string;
        businessEmail: string;
        businessMobile: string;
        logo: string;
        banner: string;
        businessAddress: string;
    };
    password: string;
}

export const useSellerForm = (onSubmitCallback: (values: SellerFormValues) => void) => {
    const initialValues = {
        mobile: "",
        otp: "",
        gstin: "",
        pickupAddress: {
            name: "",
            mobile: "",
            pincode: "",
            address: "",
            locality: "",
            city: "",
            state: "",
        },
        bankDetails: {
            accountNumber: "",
            ifscCode: "",
            accountHolderName: "",
        },
        sellerName: "",
        email: "",
        businessDetails: {
            businessName: "",
            businessEmail: "",
            businessMobile: "",
            logo: "",
            banner: "",
            businessAddress: ""
        },
        password: ""
    };

    return useFormik({
        initialValues,
        onSubmit: (values) => {
            if (onSubmitCallback) {
                onSubmitCallback(values);
            } else {
                console.log(values, "formik submitted");
            }
        },
    });
};