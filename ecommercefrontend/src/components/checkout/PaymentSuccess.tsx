import { useEffect, useRef, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Box, Paper, Typography, Button, Stack, Divider, Chip, CircularProgress } from '@mui/material';
import { alpha } from '@mui/material/styles';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import { useAppDispatch } from "../../state/hooks.ts";
import { paymentSuccess } from "../../state/customer/OrderSlice.ts";

interface PaymentSuccessState {
    orderId?: string;
    paymentId?: string;
    amount?: number;
    itemCount?: number;
}

const PaymentSuccess = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const dispatch = useAppDispatch();
    const { orderId: urlOrderId } = useParams();
    const state = (location.state ?? {}) as PaymentSuccessState;

    const getQueryParam = (param: string) => {
        const query = new URLSearchParams(location.search);
        return query.get(param);
    };

    const orderId = urlOrderId ?? getQueryParam('orderId') ?? state.orderId;
    const paymentId = getQueryParam('paymentId') ?? getQueryParam('razorpay_payment_id') ?? state.paymentId;
    const paymentLinkId = getQueryParam('razorpay_payment_link_id') ?? getQueryParam('paymentLinkId');

    const [orderDetails, setOrderDetails] = useState({
        amount: state.amount,
        itemCount: state.itemCount,
        loading: !state.amount || !state.itemCount,
        error: false,
    });
    const [mounted, setMounted] = useState(false);
    const hasFetched = useRef(false);

    useEffect(() => {
        const t = requestAnimationFrame(() => setMounted(true));
        return () => cancelAnimationFrame(t);
    }, []);

    useEffect(() => {
        if (!orderId || !paymentId) {
            navigate('/', { replace: true });
        }
    }, [orderId, paymentId, navigate]);

    useEffect(() => {
        if (!orderId || !paymentId || !paymentLinkId || hasFetched.current) return;
        hasFetched.current = true;

        const verify = async () => {
            try {
                const jwt = localStorage.getItem('jwt') ?? '';
                const res = await dispatch(paymentSuccess({ jwt, paymentId, paymentLinkId })).unwrap();

                // res is ApiResponseDto<PaymentOrder>.data — adjust field access if your
                // paymentSuccess thunk returns response.data directly vs response.data.data
                const paymentOrder = (res as any)?.data ?? res;

                setOrderDetails({
                    amount: paymentOrder?.amount,
                    itemCount: Array.isArray(paymentOrder?.orders)
                        ? paymentOrder.orders.reduce((sum: number, o: any) => sum + (o.totalItem ?? 0), 0)
                        : undefined,
                    loading: false,
                    error: false,
                });
            } catch (err) {
                console.error('Payment verification failed:', err);
                setOrderDetails((prev) => ({ ...prev, loading: false, error: true }));
            }
        };

        void verify();
    }, [orderId, paymentId, paymentLinkId, dispatch]);

    if (!orderId || !paymentId) {
        return (
            <Box sx={{ display: 'flex', minHeight: '100vh', alignItems: 'center', justifyContent: 'center' }}>
                <CircularProgress />
            </Box>
        );
    }

    if (!paymentLinkId) {
        return (
            <Box sx={{ display: 'flex', minHeight: '100vh', alignItems: 'center', justifyContent: 'center', flexDirection: 'column', gap: 2 }}>
                <Typography color="error">Missing payment link reference — cannot verify payment.</Typography>
                <Button variant="contained" onClick={() => navigate('/')}>Go home</Button>
            </Box>
        );
    }

    if (orderDetails.loading) {
        return (
            <Box sx={{ display: 'flex', minHeight: '100vh', alignItems: 'center', justifyContent: 'center' }}>
                <CircularProgress />
            </Box>
        );
    }

    if (orderDetails.error) {
        return (
            <Box sx={{ display: 'flex', minHeight: '100vh', alignItems: 'center', justifyContent: 'center', flexDirection: 'column', gap: 2 }}>
                <Typography color="error">Something went wrong confirming your payment.</Typography>
                <Button variant="contained" onClick={() => navigate('/')}>Go home</Button>
            </Box>
        );
    }

    return (
        <Box sx={(theme) => ({
            minHeight: '100vh',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            px: 2,
            py: 6,
            background: `radial-gradient(circle at 50% 0%, ${alpha(theme.palette.primary.main, 0.08)} 0%, #f8fafc 55%)`,
        })}>
            <Box sx={{
                width: '100%', maxWidth: 440,
                opacity: mounted ? 1 : 0,
                transform: mounted ? 'translateY(0) scale(1)' : 'translateY(16px) scale(0.98)',
                transition: 'opacity 0.5s ease, transform 0.5s cubic-bezier(0.22, 1, 0.36, 1)',
            }}>
                <Stack sx={{ mb: 3, alignItems: 'center' }}>
                    <Box sx={(theme) => ({
                        width: 72, height: 72, borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center',
                        backgroundColor: 'primary.main',
                        boxShadow: `0 8px 24px ${alpha(theme.palette.primary.main, 0.25)}`,
                    })}>
                        <svg width={34} height={34} viewBox="0 0 34 34" fill="none">
                            <path d="M7 17.5L14 24.5L27 10" stroke="white" strokeWidth="3.5" strokeLinecap="round" strokeLinejoin="round" />
                        </svg>
                    </Box>
                </Stack>
                <Stack spacing={0.5} sx={{ mb: 3, textAlign: 'center', alignItems: 'center' }}>
                    <Typography sx={{ fontWeight: 700, color: 'text.primary' }}>Payment successful</Typography>
                    <Typography variant="body2" color="text.secondary">Your order is confirmed — a receipt has been sent.</Typography>
                </Stack>
                <Paper elevation={0} sx={{ border: '1px solid', borderColor: 'divider', borderRadius: 3, p: 3 }}>
                    <Stack sx={{ direction: "row", justifyContent: "space-between", mb: 2 }}>
                        <Box>
                            <Typography variant="caption" color="text.secondary">Order ID</Typography>
                            <Typography sx={{ fontWeight: 700 }}>{orderId}</Typography>
                        </Box>
                        <Chip label="Paid" size="small" sx={{ fontWeight: 600, color: 'primary.dark' }} />
                    </Stack>
                    <Stack spacing={1}>
                        <Stack sx={{ direction: "row", justifyContent: "space-between" }}>
                            <Typography variant="body2" color="text.secondary">Items</Typography>
                            <Typography variant="body2">{orderDetails.itemCount} {orderDetails.itemCount === 1 ? 'item' : 'items'}</Typography>
                        </Stack>
                        <Stack sx={{ direction: "row", justifyContent: "space-between" }}>
                            <Typography variant="body2" color="text.secondary">Payment ID</Typography>
                            <Typography variant="body2" sx={{ fontFamily: 'monospace' }}>{paymentId}</Typography>
                        </Stack>
                    </Stack>
                    <Divider sx={{ my: 2, borderStyle: 'dashed' }} />
                    <Stack sx={{ direction: "row", justifyContent: "space-between" }}>
                        <Typography sx={{ fontWeight: 600 }}>Total paid</Typography>
                        <Typography sx={{ fontWeight: 800 }}>₹{orderDetails.amount?.toLocaleString('en-IN')}</Typography>
                    </Stack>
                </Paper>
                <Stack spacing={1.5} sx={{ mt: 3.5 }}>
                    <Button fullWidth variant="contained" endIcon={<ArrowForwardIcon />} onClick={() => navigate(`/`)}>Shop More</Button>
                </Stack>
            </Box>
        </Box>
    );
};

export default PaymentSuccess;