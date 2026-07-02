import { useState } from 'react';
import { Box, Grid, Paper, Typography } from '@mui/material';
import CartItem from './CartItem.tsx';
import PricingCard from './PricingCard.tsx';
import Coupon, {type CouponResult} from "../coupon/Coupon.tsx";
import { Link } from "react-router-dom";

const ITEM_PRICE = 19.99;

const Cart = () => {
    const [coupon, setCoupon] = useState<CouponResult | null>(null);

    const subtotal = ITEM_PRICE;

    const discount = coupon
        ? coupon.discountType === 'percentage'
            ? (subtotal * coupon.discountValue) / 100
            : Math.min(coupon.discountValue, subtotal)
        : 0;

    return (
        <Box sx={{ maxWidth: 1100, mx: 'auto', px: { xs: 2, md: 4 }, py: 4 }}>
            <Typography variant="h5" sx={{ fontWeight: 800, mb: 3, color: '#0d1b4b' }}>
                My Cart
            </Typography>

            <Grid sx={{spacing:3,alignItems:"flex-start"}} >
                <Grid sx={{xs:12,md:7}}>
                    <CartItem />
                </Grid>

                <Grid sx={{xs:12,md:5}} >
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                        <Paper
                            elevation={0}
                            sx={{ border: '1px solid #e0e0e0', borderRadius: 2, p: 2 }}
                        >
                            <Coupon
                                onApply={(result) => setCoupon(result)}
                                onRemove={() => setCoupon(null)}
                            />
                        </Paper>
                        <Link to="/checkout">
                            <PricingCard
                                subtotal={subtotal}
                                discount={discount}
                                couponCode={coupon?.code}
                            />
                        </Link>
                    </Box>
                </Grid>
            </Grid>
        </Box>
    );
};

export default Cart;