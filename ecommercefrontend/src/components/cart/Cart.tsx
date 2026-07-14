import {useEffect, useState} from 'react';
import { Box, Grid, Paper, Typography } from '@mui/material';
import CartItem from './CartItem.tsx';
import PricingCard from './PricingCard.tsx';
import Coupon, {type CouponResult} from "../coupon/Coupon.tsx";
import { Link } from "react-router-dom";
import {useAppDispatch, useAppSelector} from "../../state/hooks.ts";
import {fetchUserCart} from "../../state/customer/CartSlice.ts";

const Cart = () => {
    const [coupon, setCoupon] = useState<CouponResult | null>(null);
    const dispatch = useAppDispatch();

    useEffect(() => {
        dispatch(fetchUserCart(localStorage.getItem("jwt") || ""));
    }, [dispatch]);

    const cart = useAppSelector(store => store.cart.cart);

    const subtotal = cart?.cartItems?.reduce((acc, item) => acc + (item.sellingPrice * item.quantity), 0) || 0;

    const discount = coupon && cart
        ? coupon.discountType === 'percentage'
            ? (subtotal * coupon.discountValue) / 100
            : Math.min(coupon.discountValue, subtotal)
        : 0;

    return (
        <Box sx={{ maxWidth: 1100, mx: 'auto', px: { xs: 2, md: 4 }, py: 4 }}>
            <Typography variant="h5" sx={{ fontWeight: 800, mb: 3, color: '#0d1b4b' }}>
                My Cart
            </Typography>

            <Grid container sx={{ gap: 3, alignItems: "flex-start" }}>
                <Grid sx={{ flexGrow: 1, flexBasis: { xs: '100%', md: '58%' } }}>
                    {cart?.cartItems?.map((item: any) => (
                        <CartItem key={item.id} item={item} />
                    ))}
                </Grid>

                <Grid sx={{ flexGrow: 1, flexBasis: { xs: '100%', md: '35%' } }}>
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
                        <Link to="/checkout" style={{ textDecoration: 'none' }}>
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