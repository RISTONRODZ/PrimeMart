import { useFormik } from "formik";
import * as Yup from "yup";
import {
    Box,
    TextField,
    Grid,
    Button,
    MenuItem,
} from "@mui/material";
import type {Address} from "../../types/UserTypes.ts";

interface AddressFormValues {
    fullName: string;
    mobile: string;
    pinCode: string;
    house: string;
    area: string;
    landmark: string;
    city: string;
    state: string;
}

const indianStates = [
    "Andhra Pradesh",
    "Arunachal Pradesh",
    "Assam",
    "Bihar",
    "Chhattisgarh",
    "Goa",
    "Gujarat",
    "Haryana",
    "Himachal Pradesh",
    "Jharkhand",
    "Karnataka",
    "Kerala",
    "Madhya Pradesh",
    "Maharashtra",
    "Manipur",
    "Meghalaya",
    "Mizoram",
    "Nagaland",
    "Odisha",
    "Punjab",
    "Rajasthan",
    "Sikkim",
    "Tamil Nadu",
    "Telangana",
    "Tripura",
    "Uttar Pradesh",
    "Uttarakhand",
    "West Bengal",
    "Delhi",
    "Jammu & Kashmir",
    "Ladakh",
    "Chandigarh",
    "Dadra & Nagar Haveli and Daman & Diu",
    "Lakshadweep",
    "Puducherry",
    "Andaman & Nicobar Islands",
];

const validationSchema = Yup.object({
    fullName: Yup.string()
        .trim()
        .matches(
            /^[A-Za-z\s.'-]+$/,
            "Name can only contain letters"
        )
        .min(3, "Name is too short")
        .max(60, "Maximum 60 characters")
        .required("Full name is required"),

    mobile: Yup.string()
        .matches(/^[6-9]\d{9}$/, "Enter a valid 10-digit mobile number")
        .required("Mobile number is required"),

    pinCode: Yup.string()
        .matches(/^[1-9][0-9]{5}$/, "Enter a valid 6-digit PIN code")
        .required("PIN code is required"),

    house: Yup.string()
        .trim()
        .min(3, "Enter house/flat details")
        .max(120)
        .required("House / Flat / Building is required"),

    area: Yup.string()
        .trim()
        .min(3, "Enter area or street")
        .max(150)
        .required("Area / Street is required"),

    landmark: Yup.string()
        .trim()
        .max(100),

    city: Yup.string()
        .trim()
        .matches(/^[A-Za-z\s.-]+$/, "Invalid city")
        .max(60)
        .required("City is required"),

    state: Yup.string().required("State is required"),
});

interface AddressFormProps {
    onClose?: (address?: Address) => void;
}

const AddressForm = ({ onClose }: AddressFormProps) => {
    const formik = useFormik<AddressFormValues>({
        initialValues: {
            fullName: "",
            mobile: "",
            pinCode: "",
            house: "",
            area: "",
            landmark: "",
            city: "",
            state: "",
        },
        validationSchema,
        onSubmit: (values) => {
            const address: Address = {
                name: values.fullName,
                mobile: values.mobile,
                pinCode: values.pinCode,
                address: `${values.house}, ${values.area}${values.landmark ? ', ' + values.landmark : ''}`,
                locality: values.area,
                city: values.city,
                state: values.state,
            };
            if (onClose) onClose(address);
        },
    });

    const renderTextField = (
        name: keyof AddressFormValues,
        label: string,
        textFieldProps: any = {},
        inputProps?: any
    ) => {
        return (
            <TextField
                fullWidth
                name={name}
                label={label}
                value={formik.values[name]}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                error={formik.touched[name] && Boolean(formik.errors[name])}
                helperText={formik.touched[name] && formik.errors[name]}
                inputProps={inputProps}
                {...textFieldProps}
            />
        );
    };

    return (
        <Box sx={{ width: "100%" }}>
            <p className="text-2xl font-bold text-center mb-6">
                Delivery Address
            </p>

            <form onSubmit={formik.handleSubmit}>
                <Grid container spacing={2}>
                    <Grid size={{ xs: 12 }}>
                        {renderTextField("fullName", "Full Name", undefined, {
                            maxLength: 60,
                        })}
                    </Grid>

                    <Grid size={{ xs: 12, sm: 6 }}>
                        {renderTextField("mobile", "Mobile Number", undefined, {
                            maxLength: 10,
                            inputMode: "numeric",
                        })}
                    </Grid>

                    <Grid size={{ xs: 12, sm: 6 }}>
                        {renderTextField("pinCode", "PIN Code", undefined, {
                            maxLength: 6,
                            inputMode: "numeric",
                        })}
                    </Grid>

                    <Grid size={{ xs: 12 }}>
                        {renderTextField(
                            "house",
                            "House No., Flat, Building Name",
                            undefined,
                            {
                                maxLength: 120,
                            }
                        )}
                    </Grid>

                    <Grid size={{ xs: 12 }}>
                        {renderTextField(
                            "area",
                            "Area, Street, Sector, Village",
                            {
                                multiline: true,
                                rows: 3,
                            },
                            {
                                maxLength: 150,
                            }
                        )}
                    </Grid>

                    <Grid size={{ xs: 12 }}>
                        {renderTextField("landmark", "Landmark (Optional)", undefined, {
                            maxLength: 100,
                        })}
                    </Grid>

                    <Grid size={{ xs: 12, sm: 6 }}>
                        {renderTextField("city", "City")}
                    </Grid>

                    <Grid size={{ xs: 12, sm: 6 }}>
                        <TextField
                            select
                            fullWidth
                            name="state"
                            label="State"
                            value={formik.values.state}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            error={
                                formik.touched.state &&
                                Boolean(formik.errors.state)
                            }
                            helperText={
                                formik.touched.state &&
                                formik.errors.state
                            }
                        >
                            {indianStates.map((state) => (
                                <MenuItem key={state} value={state}>
                                    {state}
                                </MenuItem>
                            ))}
                        </TextField>
                    </Grid>

                    <Grid size={{ xs: 12 }}>
                        <Button
                            type="submit"
                            variant="contained"
                            fullWidth
                            size="large"
                        >
                            Save Address
                        </Button>
                    </Grid>
                </Grid>
            </form>
        </Box>
    );
};

export default AddressForm;