import {  useEffect } from "react";
import { useFormik } from "formik";
import { Button, TextField, Box, Typography, Paper, Grid } from "@mui/material";
import { useDispatch, useSelector } from "react-redux";
import { updateSellerProfile, fetchSellerProfile } from "../../../state/seller/SellerSlice";

const Profile = () => {
    const dispatch = useDispatch();
    const { profile, loading, error } = useSelector((state: any) => state.seller);
    const jwt = localStorage.getItem("jwt");

    const formik = useFormik({
        initialValues: {
            sellerName: "",
            email: "",
            mobile: "",
            gstin: "",
            accountStatus: "",
            emailVerified: false,
            businessName: "",
            businessEmail: "",
            businessMobile: "",
            businessAddress: "",
            logo: "",
            banner: "",
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
        },
        onSubmit: async (values) => {
            console.log("Form values before submission:", values);
            const profileData = {
                sellerName: values.sellerName,
                email: values.email,
                mobile: values.mobile,
                gstin: values.gstin,
                accountStatus: values.accountStatus,
                emailVerified: values.emailVerified,
                businessDetails: {
                    businessName: values.businessName,
                    businessEmail: values.businessEmail,
                    businessMobile: values.businessMobile,
                    businessAddress: values.businessAddress,
                    logo: values.logo,
                    banner: values.banner,
                },
                pickupAddress: values.pickupAddress,
                bankDetails: values.bankDetails,
            };
            console.log("Data being sent to API:", profileData);
            console.log("JWT Token:", jwt);
            await dispatch(updateSellerProfile(profileData) as any);
        },
    });

    useEffect(() => {
        if (jwt) {
            dispatch(fetchSellerProfile(jwt) as any);
        }
    }, [dispatch, jwt]);

    useEffect(() => {
        if (profile) {
            console.log("Fetched profile data:", profile);
            formik.setValues({
                sellerName: profile.sellerName || "",
                email: profile.email || "",
                mobile: profile.mobile || "",
                gstin: profile.gstin || "",
                accountStatus: profile.accountStatus || "",
                emailVerified: profile.emailVerified || false,
                businessName: profile.businessDetails?.businessName || "",
                businessEmail: profile.businessDetails?.businessEmail || "",
                businessMobile: profile.businessDetails?.businessMobile || "",
                businessAddress: profile.businessDetails?.businessAddress || "",
                logo: profile.businessDetails?.logo || "",
                banner: profile.businessDetails?.banner || "",
                pickupAddress: profile.pickupAddress || {
                    name: "",
                    mobile: "",
                    pincode: "",
                    address: "",
                    locality: "",
                    city: "",
                    state: "",
                },
                bankDetails: profile.bankDetails || {
                    accountNumber: "",
                    ifscCode: "",
                    accountHolderName: "",
                },
            });
        }
    }, [profile]);

    return (
        <div className="p-6">
            <Typography variant="h4" gutterBottom>
                Seller Profile
            </Typography>
            <Paper elevation={3} sx={{ p: 4 }}>
                <form onSubmit={formik.handleSubmit}>
                    <Grid container spacing={3}>
                        <Grid sx={{ gridColumn: 'span 12' }}>
                            <Typography variant="h6" gutterBottom>
                                Basic Information
                            </Typography>
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Seller Name"
                                name="sellerName"
                                value={formik.values.sellerName}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Email"
                                name="email"
                                value={formik.values.email}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Mobile"
                                name="mobile"
                                value={formik.values.mobile}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="GSTIN"
                                name="gstin"
                                value={formik.values.gstin}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Account Status"
                                name="accountStatus"
                                value={formik.values.accountStatus}
                                onChange={formik.handleChange}
                                margin="normal"
                                disabled
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: 'span 12' }}>
                            <TextField
                                fullWidth
                                label="Email Verified"
                                name="emailVerified"
                                value={formik.values.emailVerified ? "Verified" : "Not Verified"}
                                onChange={formik.handleChange}
                                margin="normal"
                                disabled
                            />
                        </Grid>

                        <Grid sx={{ gridColumn: 'span 12' }}>
                            <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
                                Business Details
                            </Typography>
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Business Name"
                                name="businessName"
                                value={formik.values.businessName}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Business Email"
                                name="businessEmail"
                                value={formik.values.businessEmail}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Business Mobile"
                                name="businessMobile"
                                value={formik.values.businessMobile}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: 'span 12' }}>
                            <TextField
                                fullWidth
                                label="Business Address"
                                name="businessAddress"
                                value={formik.values.businessAddress}
                                onChange={formik.handleChange}
                                margin="normal"
                                multiline
                                rows={3}
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Logo URL"
                                name="logo"
                                value={formik.values.logo}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Banner URL"
                                name="banner"
                                value={formik.values.banner}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>

                        <Grid sx={{ gridColumn: 'span 12' }}>
                            <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
                                Pickup Address
                            </Typography>
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Contact Name"
                                name="pickupAddress.name"
                                value={formik.values.pickupAddress.name}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Contact Mobile"
                                name="pickupAddress.mobile"
                                value={formik.values.pickupAddress.mobile}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Pincode"
                                name="pickupAddress.pincode"
                                value={formik.values.pickupAddress.pincode}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: 'span 12' }}>
                            <TextField
                                fullWidth
                                label="Address"
                                name="pickupAddress.address"
                                value={formik.values.pickupAddress.address}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Locality"
                                name="pickupAddress.locality"
                                value={formik.values.pickupAddress.locality}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="City"
                                name="pickupAddress.city"
                                value={formik.values.pickupAddress.city}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="State"
                                name="pickupAddress.state"
                                value={formik.values.pickupAddress.state}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>

                        <Grid sx={{ gridColumn: 'span 12' }}>
                            <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
                                Bank Details
                            </Typography>
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="Account Number"
                                name="bankDetails.accountNumber"
                                value={formik.values.bankDetails.accountNumber}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                            <TextField
                                fullWidth
                                label="IFSC Code"
                                name="bankDetails.ifscCode"
                                value={formik.values.bankDetails.ifscCode}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>
                        <Grid sx={{ gridColumn: 'span 12' }}>
                            <TextField
                                fullWidth
                                label="Account Holder Name"
                                name="bankDetails.accountHolderName"
                                value={formik.values.bankDetails.accountHolderName}
                                onChange={formik.handleChange}
                                margin="normal"
                            />
                        </Grid>

                        <Grid sx={{ gridColumn: 'span 12' }}>
                            <Box sx={{ mt: 3 }}>
                                <Button
                                    type="submit"
                                    variant="contained"
                                    color="primary"
                                    disabled={loading}
                                    size="large"
                                >
                                    {loading ? "Saving..." : "Save Profile"}
                                </Button>
                            </Box>
                        </Grid>

                        {error && (
                            <Grid sx={{ gridColumn: 'span 12' }}>
                                <Typography color="error" sx={{ mt: 2 }}>
                                    {typeof error === 'object' ? (error as any).message || JSON.stringify(error) : error}
                                </Typography>
                            </Grid>
                        )}
                    </Grid>
                </form>
            </Paper>
        </div>
    );
};

export default Profile;