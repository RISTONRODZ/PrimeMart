import { Box, Button, Divider, Paper, Stack, Typography } from '@mui/material';
import ShoppingBagOutlinedIcon from '@mui/icons-material/ShoppingBagOutlined';

interface PricingCardProps {
    subtotal: number;
    discount: number;
    couponCode?: string;
}

const SHIPPING_THRESHOLD = 499;
const SHIPPING_FEE = 49;

const PricingCard = ({ subtotal, discount, couponCode }: PricingCardProps) => {
    const shipping = subtotal - discount >= SHIPPING_THRESHOLD ? 0 : SHIPPING_FEE;
    const total = Math.max(0, subtotal - discount) + shipping;

    const row = (label: string, value: string, color?: string) => (
        <Stack direction="row" sx={{justifyContent:"space-between",alignItems:"center"}} >
            <Typography variant="body2" sx={{ color: color ?? 'text.secondary' }}>
                {label}
            </Typography>
            <Typography variant="body2" sx={{ fontWeight: 500, color: color ?? 'text.primary' }}>
                {value}
            </Typography>
        </Stack>
    );

    return (
        <Paper
            elevation={0}
            sx={{ border: '1px solid #e0e0e0', borderRadius: 2, overflow: 'hidden' }}
        >
            <Box sx={{ px: 2.5, py: 1.5, borderBottom: '1px solid #e0e0e0', color: '#f8f9ff' }}>
                <Typography variant="body2" sx={{ fontWeight: 700, color: '#1447e6' }}>
                    Price Details
                </Typography>
            </Box>

            <Stack spacing={1.5} sx={{ p: 2.5 }}>
                {row('Price (1 item)', `₹${subtotal.toFixed(2)}`)}

                {discount > 0 &&
                    row(
                        `Coupon${couponCode ? ` (${couponCode})` : ''}`,
                        `−₹${discount.toFixed(2)}`,
                        '#2e7d32'
                    )}

                {row(
                    'Delivery',
                    shipping === 0 ? 'FREE' : `₹${shipping.toFixed(2)}`,
                    shipping === 0 ? '#2e7d32' : undefined
                )}

                <Divider />

                <Stack direction="row" sx={{justifyContent:"space-between", alignItems:"center"}} >
                    <Typography variant="body2" sx={{ fontWeight: 700 }}>
                        Total Amount
                    </Typography>
                    <Typography variant="body1" sx={{ fontWeight: 800, color: '#1447e6' }}>
                        ₹{total.toFixed(2)}
                    </Typography>
                </Stack>

                {discount > 0 && (
                    <Typography
                        variant="caption"
                        sx={{
                            display: 'block',
                            color: '#e8f5e9',
                            borderRadius: 1,
                            px: 1.5,
                            py: 0.75,
                            fontWeight: 600,
                        }}
                    >
                        You save ₹{(discount + (SHIPPING_FEE - shipping)).toFixed(2)} on this order 🎉
                    </Typography>
                )}
            </Stack>

            {shipping > 0 && (
                <Box sx={{ px: 2.5, pb: 1.5 }}>
                    <Stack direction="row" sx={{spacing:0.75,alignItems:"center"}}>
                    </Stack>
                </Box>
            )}

            <Box sx={{ px: 2.5, pb: 2.5 }}>
                <Button
                    fullWidth
                    variant="contained"
                    size="large"
                    startIcon={<ShoppingBagOutlinedIcon />}
                    sx={{
                        color: '#000000',
                        borderRadius: 2,
                        textTransform: 'none',
                        fontWeight: 700,
                        fontSize: 15,
                        py: 1.25,
                        boxShadow: 'none',
                        '&:hover': { color: '#ffff', boxShadow: 'none' },
                    }}
                >
                    Place Order
                </Button>
            </Box>
        </Paper>
    );
};

export default PricingCard;