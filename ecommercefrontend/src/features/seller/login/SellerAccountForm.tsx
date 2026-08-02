import {Button, Step, StepLabel, Stepper, Alert, AlertTitle} from "@mui/material";
import {useState} from "react";
import BecomeSellerFormStep1 from "../become_seller_steps/BecomeSellerFormStep1.tsx";
import {useFormik} from "formik";
import BecomeSellerFormStep2 from "../become_seller_steps/BecomeSellerFormStep2.tsx";
import BecomeSellerFormStep3 from "../become_seller_steps/BecomeSellerFormStep3.tsx";
import BecomeSellerFormStep4 from "../become_seller_steps/BecomeSellerFormStep4.tsx";
import {
    step1Schema,
    step2Schema,
    step3Schema,
    step4Schema,
    fullFormSchema,
} from "./FormSchema";
import {useDispatch, useSelector} from "react-redux";
import {registerSeller} from "../../../state/slice/AuthSlice";
import {useNavigate} from "react-router-dom";

const stepSchemas = [
    step1Schema,
    step2Schema,
    step3Schema,
    step4Schema,
];
const steps = [
    "Tax Details & Mobile",
    "Pickup Address",
    "Bank Details",
    "Supplier Details",
];

const SellerAccountForm = () => {
    const [activeStep, setActiveStep] = useState(0);
    const [validationError, setValidationError] = useState<string | null>(null);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { loading, error } = useSelector((state: any) => state.auth);

    const handleStep = (value: number) => {
        setActiveStep((prev) => {
            const nextStep = prev + value;
            if (nextStep >= 0 && nextStep < steps.length) {
                return nextStep;
            }

            return prev;
        });
    };

    const handleNext = async () => {
        setValidationError(null);
        
        if (activeStep === steps.length - 1) {
            const errors = await fullFormSchema.validate(formik.values, { abortEarly: false })
                .then(() => ({}))
                .catch((err: any) => {
                    const validationErrors: any = {};
                    err.inner.forEach((error: any) => {
                        validationErrors[error.path] = error.message;
                    });
                    return validationErrors;
                });

            console.log("FULL FORM VALIDATION ERRORS BLOCKING SUBMIT:", errors);

            if (Object.keys(errors).length > 0) {
                const touchedFields = Object.keys(errors).reduce(
                    (acc, key) => ({ ...acc, [key]: true }),
                    {}
                );
                formik.setTouched(touchedFields, true);
                setValidationError("Please fix all validation errors before submitting.");
                return;
            }

            formik.handleSubmit();
        } else {
            const stepErrors = await formik.validateForm();
            console.log("VALIDATION ERRORS IN BETWEEN:", stepErrors);
            if (Object.keys(stepErrors).length > 0) {
                const touchedFields = Object.keys(stepErrors).reduce(
                    (acc, key) => ({ ...acc, [key]: true }),
                    {}
                );
                formik.setTouched(touchedFields, true);
                setValidationError("Please fix the validation errors before proceeding.");
                return;
            }
            handleStep(1);
        }
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
        validationSchema: stepSchemas[activeStep],
        onSubmit: async (values) => {
            console.log("Form submitted with values:", values);
            const sellerData = {
                mobile: values.mobile,
                email: values.email,
                gstin: values.gstin,
                pickupAddress: values.pickupAddress,
                bankDetails: values.bankDetails,
                sellerName: values.sellerName,
                businessDetails: values.businessDetails,
                password: values.password
            };
            console.log("Dispatching registerSeller with data:", sellerData);
            const result = await dispatch(registerSeller(sellerData) as any);
            console.log("Register seller result:", result);
            if (result.payload && !result.error) {
                console.log("Registration successful, navigating to dashboard");
                navigate("/seller");
            } else {
                console.log("Registration failed:", result.error);
            }
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
                {activeStep === steps.length - 1 ? (
                    <Button onClick={handleNext} variant={'contained'} disabled={loading} color="primary">
                        {loading ? "Creating Account..." : "Create Account"}
                    </Button>
                ) : (
                    <Button onClick={handleNext} variant={'contained'}>
                        Continue
                    </Button>
                )}
            </div>
            {validationError && (
                <Alert severity="warning" className="mt-4" onClose={() => setValidationError(null)}>
                    <AlertTitle>Validation Error</AlertTitle>
                    {validationError}
                </Alert>
            )}
            {error && (
                <Alert severity="error" className="mt-4">
                    <AlertTitle>Registration Error</AlertTitle>
                    {error.includes("duplicate key value violates unique constraint") && error.includes("seller_email_key")
                        ? "This email is already registered. Please use a different email or login to your existing account."
                        : error.includes("duplicate key value violates unique constraint")
                        ? "A record with this information already exists."
                        : error}
                </Alert>
            )}
        </div>
    );
};
export default SellerAccountForm;