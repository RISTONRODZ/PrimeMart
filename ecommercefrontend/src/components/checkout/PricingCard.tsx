import { useState } from "react";
import { Snackbar, Alert, CircularProgress } from "@mui/material";
import { useAppDispatch } from "../../state/hooks.ts";
import { createOrder } from "../../state/customer/OrderSlice.ts";
import type { Address } from "../../types/UserTypes.ts";

interface PricingCardProps {
    title: string;
    price: number;
    features: string[];
    isAddressSelected?: boolean;
    selectedAddress?: Address | null;
}

const PricingCard = ({ title, price, features, isAddressSelected = false, selectedAddress }: PricingCardProps) => {
    const dispatch = useAppDispatch();
    const [loading, setLoading] = useState(false);
    const [snackbar, setSnackbar] = useState({ open: false, message: "", severity: "success" as "success" | "error" });

    const handleClose = (_event?: React.SyntheticEvent | Event, reason?: string) => {
        if (reason === "clickaway") return;
        setSnackbar({ ...snackbar, open: false });
    };

    const handleRazorpayPayment = async () => {
        if (!isAddressSelected || !selectedAddress) {
            setSnackbar({ open: true, message: "Please select a delivery address first", severity: "error" });
            return;
        }

        const jwt = localStorage.getItem("jwt");
        if (!jwt) {
            setSnackbar({ open: true, message: "Authentication required", severity: "error" });
            return;
        }

        setLoading(true);

        try {
            const result = await dispatch(createOrder({ address: selectedAddress, jwt, paymentGateway: "RAZORPAY" }));

            if (result.payload?.data?.payment_link_url) {
                setSnackbar({ open: true, message: "Redirecting to payment...", severity: "success" });
                window.location.href = result.payload.data.payment_link_url;
            } else {
                throw new Error("Invalid response format");
            }
        } catch (error) {
            setLoading(false);
            setSnackbar({ open: true, message: "Failed to initiate payment", severity: "error" });
        }
    };

    return (
        <div className="relative overflow-hidden w-full max-w-sm mx-auto p-8 rounded-3xl bg-white border border-slate-100 shadow-[0_20px_50px_rgba(8,112,184,0.1)] transition-all duration-300 hover:shadow-[0_20px_50px_rgba(8,112,184,0.2)]">
            <div className="absolute top-0 left-0 w-full h-2 bg-gradient-to-r from-blue-600 to-indigo-600" />

            <div className="text-center">
                <h2 className="text-sm font-bold tracking-widest uppercase text-slate-400 mb-2">{title}</h2>
                <div className="flex justify-center items-baseline gap-1 my-4">
                    <span className="text-5xl font-extrabold text-slate-900">₹{price}</span>
                </div>
            </div>

            <ul className="text-slate-600 my-8 space-y-4">
                {features.map((feature, index) => (
                    <li key={index} className="flex items-center gap-3 text-sm">
                        <div className="flex-shrink-0 w-5 h-5 rounded-full bg-blue-100 flex items-center justify-center text-blue-600">
                            <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="3" d="M5 13l4 4L19 7" /></svg>
                        </div>
                        {feature}
                    </li>
                ))}
            </ul>

            <button
                onClick={handleRazorpayPayment}
                disabled={!isAddressSelected || loading}
                className={`w-full py-4 px-6 rounded-2xl font-bold transition-all duration-300 transform active:scale-[0.98] shadow-lg flex justify-center items-center ${
                    isAddressSelected && !loading
                        ? "bg-gradient-to-r from-blue-600 to-indigo-600 text-white hover:shadow-blue-500/30 hover:shadow-2xl"
                        : "bg-slate-100 text-slate-400 cursor-not-allowed"
                }`}
            >
                {loading ? (
                    <CircularProgress size={24} color="inherit" />
                ) : (
                    isAddressSelected ? "Pay with Razorpay" : "Select address to proceed"
                )}
            </button>

            <Snackbar
                open={snackbar.open}
                autoHideDuration={4000}
                onClose={handleClose}
                anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
            >
                <Alert
                    onClose={handleClose}
                    severity={snackbar.severity}
                    variant="filled"
                    sx={{ width: "100%", borderRadius: "12px" }}
                >
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </div>
    );
};

export default PricingCard;