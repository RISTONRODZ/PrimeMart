import { useState } from 'react';
import { Box, Typography, IconButton, Paper, Stack } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import { Close } from "@mui/icons-material";

const CartItem = () => {
    const [quantity, setQuantity] = useState<number>(5);
    const handleIncrement = () => setQuantity((prev) => prev + 1);
    const handleDecrement = () => setQuantity((prev) => (prev > 1 ? prev - 1 : 1));

    return (
        <Paper
            elevation={0}
            sx={{
                position: 'relative',   
                display: 'flex',
                gap: 2,
                p: 2,
                border: '1px solid #e0e0e0',
                borderRadius: 2,
                alignItems: 'center'
            }}
        >
            <Box
                component="img"
                src="https://i.pinimg.com/736x/1b/2b/ea/1b2bea8eaeff7b0fe058a34bead3a342.jpg"
                alt="Product"
                sx={{ width: 90, height: 90, borderRadius: 1, objectFit: 'cover' }}
            />
            <Stack spacing={0.5} sx={{ flexGrow: 1 }}>
                <Typography variant="h6" sx={{ fontWeight: "600" }}>Luis Vuitton</Typography>
                <Typography variant="body2" color="text.secondary">
                    Lorem ipsum dolor sit amet consectetur adipisicing elit.
                </Typography>
                <Typography variant="caption" sx={{ display: "block" }}>
                    Sold by: Natural Lifestyle Products Private Limited
                </Typography>
                <Stack direction="row" sx={{ alignItems: 'center', gap: 1, mt: 1 }}>
                    <Typography variant="body2"><strong>Quantity:</strong></Typography>
                    <IconButton size="small" onClick={handleDecrement} disabled={quantity <= 1}>
                        <RemoveIcon fontSize="small" />
                    </IconButton>
                    <Typography sx={{ mx: 1, minWidth: '20px', textAlign: 'center' }}>
                        {quantity}
                    </Typography>
                    <IconButton size="small" onClick={handleIncrement}>
                        <AddIcon fontSize="small" />
                    </IconButton>
                </Stack>
                <Typography variant="body2" sx={{ mt: 1 }}>
                    <strong>Price:</strong> $19.99
                </Typography>
            </Stack>

            <Box sx={{ position: 'absolute', top: 4, right: 4 }}>
                <IconButton size="small">
                    <Close sx={{ color: "#1447e6" }} />
                </IconButton>
            </Box>
        </Paper>
    );
};

export default CartItem;