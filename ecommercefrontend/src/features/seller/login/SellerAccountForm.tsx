import {Button, Step, StepLabel, Stepper} from "@mui/material";
import {useState} from "react";
import BecomeSellerFormStep1 from "../become_seller_steps/BecomeSellerFormStep1.tsx";
import {useFormik} from "formik";
import BecomeSellerFormStep2 from "../become_seller_steps/BecomeSellerFormStep2.tsx";
import BecomeSellerFormStep3 from "../become_seller_steps/BecomeSellerFormStep3.tsx";
import BecomeSellerFormStep4 from "../become_seller_steps/BecomeSellerFormStep4.tsx";

const steps = [
    "Tax Details & Mobile",
    "Pickup Address",
    "Bank Details",
    "Supplier Details",
];

const SellerAccountForm = () => {
    const [activeStep, setActiveStep] = useState(0);const handleStep = (value: number) => {
        setActiveStep((prev) => {
            const nextStep = prev + value;
            if (nextStep >= 0 && nextStep < steps.length) {
                return nextStep;
            }

            return prev;
        });
    };
    const formik = useFormik({
        initialValues: {
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
                businessEmail:"",
                businessMobile:"",
                logo:"",
                banner:"",
                businessAddress:""
            },
            password: ""
        },
        // validationSchema: FormSchema,
        onSubmit: (values) => {
            console.log(values, "formik submitted");
        },
    });

    return (
        <div>
            <Stepper activeStep={activeStep} alternativeLabel>
                {steps.map((label,) => (
                    <Step key={label}>
                        <StepLabel>{label}</StepLabel>
                    </Step>
                ))}
            </Stepper>
            <section className="pb-10">
                {
                    activeStep === 0 ? <BecomeSellerFormStep1 formik={formik}/>:
                    activeStep === 1 ? <BecomeSellerFormStep2 formik={formik}/>:
                    activeStep === 2 ? <BecomeSellerFormStep3 formik={formik}/>:
                    activeStep === 3 ? <BecomeSellerFormStep4 formik={formik}/>:
                    null
                }
            </section>
            <div className={'flex items-center justify-between'}>
                <Button onClick={() => handleStep(-1)} variant={'contained'} disabled={activeStep === 0}>
                    Back
                </Button>
                <Button onClick={() => handleStep(1)} variant={'contained'} >
                    {activeStep===(steps.length-1)?"Create Account":"continue"}
                </Button>
            </div>
        </div>
    );
};
export default SellerAccountForm;