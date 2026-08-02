import { useState } from "react";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import TextField from '@mui/material/TextField';

import { useAppDispatch, useAppSelector } from "../../../state/hooks.ts";
import { useEffect } from "react";
import { fetchSellerProducts, updateProduct } from "../../../state/seller/SellerProductSlice.ts";
import type {Product} from "../../../types/ProductTypes.ts";
export default function ProductTable() {
    const dispatch = useAppDispatch();
    const { products, loading, error } = useAppSelector((state) => state.sellerProduct);
    const [open, setOpen] = useState(false);
    const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
    const [newQuantity, setNewQuantity] = useState(0);

    useEffect(() => {
        const jwt = localStorage.getItem("jwt");
        dispatch(fetchSellerProducts(jwt));
    }, [dispatch]);

    const handleOpen = (product: Product) => {
        setSelectedProduct(product);
        setNewQuantity(product.quantity);
        setOpen(true);
    };

    const handleClose = () => setOpen(false);

    const handleUpdateStock = () => {
        if (selectedProduct) {
            const jwt = localStorage.getItem("jwt");
            dispatch(updateProduct({
                productId: selectedProduct.id,
                productData: { ...selectedProduct, quantity: newQuantity },
                jwt: jwt || ""
            }));
            handleClose();
        }
    };

    return (
        <>
            <TableContainer component={Paper} className="overflow-x-auto">
                <Table sx={{ minWidth: 300 }} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Images</TableCell>
                            <TableCell align="right">Title</TableCell>
                            <TableCell align="right">MRP</TableCell>
                            <TableCell align="right">Selling Price</TableCell>
                            <TableCell align="right">Color</TableCell>
                            <TableCell align="right">Current Stock</TableCell>
                            <TableCell align="right">Action</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {loading ? (
                            <TableRow><TableCell colSpan={7} align="center">Loading...</TableCell></TableRow>
                        ) : error ? (
                            <TableRow><TableCell colSpan={7} align="center">Error: {error}</TableCell></TableRow>
                        ) : products.length === 0 ? (
                            <TableRow><TableCell colSpan={7} align="center">No products found</TableCell></TableRow>
                        ) : (
                            products.map((product) => (
                                <TableRow key={product.id} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                    <TableCell component="th" scope="row">
                                        {product.images && product.images.length > 0 ? (
                                            <div className="flex gap-1">
                                                {product.images.map((image, index) => (
                                                    <img key={index} src={image} alt={product.title} className="w-10 h-10 object-cover rounded" />
                                                ))}
                                            </div>
                                        ) : <span>No image</span>}
                                    </TableCell>
                                    <TableCell align="right">{product.title}</TableCell>
                                    <TableCell align="right">{product.mrpPrice}</TableCell>
                                    <TableCell align="right">{product.sellingPrice}</TableCell>
                                    <TableCell align="right">{product.colors?.join(", ") || "N/A"}</TableCell>
                                    <TableCell align="right">{product.quantity}</TableCell>
                                    <TableCell align="right">
                                        <Button
                                            variant="contained"
                                            size="small"
                                            onClick={() => handleOpen(product)}
                                        >
                                            Edit Stock
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))
                        )}
                    </TableBody>
                </Table>
            </TableContainer>

            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Update Stock: {selectedProduct?.title}</DialogTitle>
                <DialogContent>
                    <TextField
                        margin="dense"
                        label="Quantity"
                        type="number"
                        fullWidth
                        value={newQuantity}
                        onChange={(e) => setNewQuantity(parseInt(e.target.value) || 0)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleUpdateStock} variant="contained">Save</Button>
                </DialogActions>
            </Dialog>
        </>
    );
}
