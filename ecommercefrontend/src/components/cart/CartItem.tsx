import {Box, IconButton, Paper, Stack, Typography} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import RemoveIcon from "@mui/icons-material/Remove";
import {Close} from "@mui/icons-material";
import {useAppDispatch} from "../../state/hooks.ts";
import {updateCartItem} from "../../state/customer/CartSlice.ts";
import {deleteCartItem} from "../../state/customer/CartSlice.ts";
interface CartItemType {
    id: number;
    productId: number;
    productTitle: string;
    productImage: string;
    size: string;
    quantity: number;
    mrpPrice: number;
    sellingPrice: number;
    sellerName: string;
}

interface CartItemProps {
    item: CartItemType;
}

const CartItemCard = ({item}: CartItemProps) => {
    const dispatch = useAppDispatch();
    const handleQuantityChange = (newQuantity: number) => {
        dispatch(
            updateCartItem({
                jwt: localStorage.getItem("jwt") || "",
                cartItemId: item.id,
                cartItem: { quantity: newQuantity },
            })
        );
    };

    const handleDeleteItem = (cartItemId: number) => {
        dispatch(
            deleteCartItem({
                jwt: localStorage.getItem("jwt") || "",
                cartItemId,
            })
        );
    };

    return (<Paper
            elevation={0}
            sx={{
                position: "relative",
                display: "flex",
                flexDirection: {xs: "column", sm: "row"},
                gap: 2,
                p: {xs: 2, sm: 2.5},
                mb: 2,
                border: "1px solid #e0e0e0",
                borderRadius: 2,
                alignItems: {xs: "center", sm: "flex-start"},
            }}
        >
            <Box
                component="img"
                src={item.productImage}
                alt={item.productTitle}
                sx={{
                    width: {xs: 120, sm: 90, md: 110},
                    height: {xs: 120, sm: 90, md: 110},
                    borderRadius: 2,
                    objectFit: "cover",
                    flexShrink: 0,
                }}
            />

            <Stack
                spacing={1}
                sx={{
                    flex: 1, width: "100%", textAlign: {xs: "center", sm: "left"},
                }}
            >
                <Typography
                    variant="h6"
                    sx={{
                        fontWeight: 600,
                        fontSize: {
                            xs: "1rem", sm: "1.1rem", md: "1.25rem",
                        },
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                        display: "-webkit-box",
                        WebkitLineClamp: 2,
                        WebkitBoxOrient: "vertical",
                        wordBreak: "break-word",
                    }}
                >
                    {item.productTitle}
                </Typography>

                <Typography variant="caption">
                    Sold by: {item.sellerName}
                </Typography>

                <Typography variant="body2">
                    Size: {item.size}
                </Typography>

                <Stack
                    direction="row"
                    sx={{
                        alignItems: "center", justifyContent: {
                            xs: "center", sm: "flex-start",
                        }, gap: 1, mt: 1, flexWrap: "wrap",
                    }}
                >
                    <Typography variant="body2" sx={{ fontWeight: "bold" }}>
                        Quantity:
                    </Typography>

                    <IconButton size="small" onClick={() => handleQuantityChange(item.quantity - 1)}>
                        <RemoveIcon fontSize="small"/>
                    </IconButton>

                    <Typography
                        sx={{
                            minWidth: 24, textAlign: "center", fontWeight: 500,
                        }}
                    >
                        {item.quantity}
                    </Typography>

                    <IconButton size="small" onClick={() => handleQuantityChange(item.quantity + 1)}>
                        <AddIcon fontSize="small"/>
                    </IconButton>
                </Stack>

                <Typography
                    variant="body1"
                    sx={{
                        mt: 1, fontWeight: 600,
                    }}
                >
                    ₹{(item.sellingPrice * item.quantity).toFixed(2)}
                </Typography>
            </Stack>

            <Box
                sx={{
                    position: "absolute", top: 8, right: 8,
                }}
            >
                <IconButton size="small" onClick={() => handleDeleteItem(item.id)}>
                    <Close sx={{color: "#1447e6"}}/>
                </IconButton>
            </Box>
        </Paper>);
};

export default CartItemCard;