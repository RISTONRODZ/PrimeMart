import { Box, Button, Divider } from "@mui/material";
import { useNavigate, useParams } from "react-router-dom";
import OrderStepper from "./OrderStepper.tsx";
import { useAppSelector, useAppDispatch } from "../../state/hooks.ts";
import { useEffect,useState } from "react";
import {cancelOrder, fetchUserOrderHistory} from "../../state/customer/OrderSlice.ts";
import { Snackbar, Alert } from "@mui/material";
const OrderDetails = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const { orderId, orderItemId } = useParams();
    const { orders } = useAppSelector((store) => store.order);
    const [open, setOpen] = useState(false);
    const [message, setMessage] = useState("");
    const [severity, setSeverity] = useState<"success" | "error">("success");
    const handleClose = () => setOpen(false);
    useEffect(() => {
        if (orders.length === 0) {
            const jwt = localStorage.getItem("jwt") || "";
            dispatch(fetchUserOrderHistory(jwt));
        }
    }, [orders.length, dispatch]);

    const currentOrder = orders.find((o) => o.orderId === orderId);
    const orderItem = currentOrder?.orderItems.find((item) => item.id === Number(orderItemId));
    const handleCancelOrder = async () => {
        const jwt = localStorage.getItem("jwt") || "";
        console.log(orderId)
        if (orderId) {
            try {
                await dispatch(cancelOrder(orderId)).unwrap();
                setMessage("Order cancelled successfully!");
                setSeverity("success");
                setOpen(true);
                setTimeout(() => navigate("/account/orders"), 1500);
            } catch (error) {
                console.error("Cancellation error:", error);
                setMessage("Failed to cancel order. Please try again.");
                setSeverity("error");
                setOpen(true);
            }
        }
    };
    if (!currentOrder || !orderItem) {
        return <Box className="p-10 text-center">Loading or order data not found.</Box>;
    }

    return (
        <Box className="space-y-4 sm:space-y-5">
            <section className="border rounded-md p-4 sm:p-5">
                <div className="flex flex-col sm:flex-row items-center sm:items-start gap-4">
                    <img
                        src={orderItem.productImage}
                        alt="Product"
                        className="w-28 h-28 sm:w-36 sm:h-36 object-contain shrink-0 rounded-md"
                    />
                    <div className="flex-1 min-w-0 text-center sm:text-left">
                        <h1 className="font-bold text-base sm:text-lg overflow-hidden break-all">
                            {orderItem.productTitle}
                        </h1>
                        <p className="text-sm mt-1">
                            <strong>Size:</strong> {orderItem.size}
                        </p>
                    </div>
                    <Button
                        variant="outlined"
                        onClick={() => navigate(`/reviews/${orderItem.productId}/create`)}
                        className="w-full sm:w-auto"
                    >
                        Write Review
                    </Button>
                </div>
            </section>

            <section className="border rounded-md p-4 sm:p-5">
                <OrderStepper orderStatus={currentOrder.orderStatus || "PENDING"} />
            </section>

            <section className="border rounded-md p-4 sm:p-5">
                <h1 className="font-bold pb-3 text-base">Delivery Address</h1>
                <div className="flex flex-col sm:flex-row sm:items-center gap-3">
                    <p>{currentOrder.shippingAddress?.address || "N/A"}</p>
                    <Divider orientation="vertical" flexItem className="hidden sm:block" />
                    <p>{currentOrder.shippingAddress?.pinCode || "N/A"}</p>
                </div>
            </section>

            <section className="border rounded-md p-4 sm:p-5">
                <div className="space-y-4">
                    <div className="flex justify-between items-center gap-4">
                        <span className="text-sm sm:text-base">Total Item Price</span>
                        <span className="font-semibold text-sm sm:text-base">₹{orderItem.sellingPrice}</span>
                    </div>
                    <Button
                        variant="contained"
                        color="error"
                        fullWidth
                        onClick={handleCancelOrder}
                    >
                        Cancel Order
                    </Button>
                </div>
            </section>
            <Snackbar
                open={open}
                autoHideDuration={4000}
                onClose={handleClose}
                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
            >
                <Alert
                    onClose={handleClose}
                    severity={severity}
                    variant="filled"
                    sx={{
                        width: '100%',
                        ...(severity === 'success' && { color: '#1d4ed8' })
                    }}
                >
                    {message}
                </Alert>
            </Snackbar>
        </Box>
    );
};

export default OrderDetails;