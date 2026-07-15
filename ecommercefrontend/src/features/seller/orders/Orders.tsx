import { useEffect, useState } from "react";
import type { MouseEvent } from "react";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { Box, Button, Menu, MenuItem } from '@mui/material';
import { useAppDispatch, useAppSelector } from "../../../state/hooks";
import { fetchSellerOrders, updateOrderStatus } from "../../../state/seller/SellerOrderSlice.ts";
import {OrderStatus} from "../../../types/OrderTypes.ts";

interface SellerOrder {
    id: number;
    orderId: string;
    orderStatus: OrderStatus;
    totalItem: number;
    totalSellingPrice: number;
    orderDate: string;
    shippingAddressCity: string;
    productName: string;
    productImage: string;
    distinctProductCount: number;
}

const orderStatusOptions = Object.values(OrderStatus);

const orderStatusColor: Record<string, string> = {
    PENDING: '#FFA500',
    PLACED: '#9370DB',
    CONFIRMED: '#20B2AA',
    SHIPPED: '#1E90FF',
    DELIVERED: '#32CD32',
    CANCELED: '#FF0000',
};

const Orders = () => {
    const dispatch = useAppDispatch();
    const orderState = useAppSelector((store) => store.sellerOrder);
    const [anchorEl, setAnchorEl] = useState<{ [key: string]: HTMLElement | null }>({});

    useEffect(() => {
        const jwt = localStorage.getItem("jwt");
        if (jwt) {
            dispatch(fetchSellerOrders(jwt));
        }
    }, [dispatch]);

    const handleClick = (event: MouseEvent<HTMLElement>, orderId: string) => {
        setAnchorEl((prev) => ({ ...prev, [orderId]: event.currentTarget }));
    };

    const handleClose = (orderId: string) => {
        setAnchorEl((prev) => ({ ...prev, [orderId]: null }));
    };

    const handleUpdateOrder = (orderId: string, newStatus: OrderStatus) => {
        const jwt = localStorage.getItem("jwt");
        if (jwt) {
            dispatch(updateOrderStatus({ jwt, orderId, orderStatus: newStatus }));
        }
        handleClose(orderId);
    };

    if (!orderState) {
        return <div>Loading...</div>;
    }

    const { loading, error, orders } = orderState;

    return (
        <div>
            <h1 className="font-bold mb-4 sm:mb-5 text-lg sm:text-xl">All Orders</h1>
            {loading && <div>Loading orders...</div>}
            {error && (
                <div className="p-3 bg-red-100 border border-red-400 text-red-700 rounded mb-4">
                    Error: {typeof error === "string" ? error : JSON.stringify(error)}
                </div>
            )}
            {!loading && orders.length === 0 && !error && (
                <div>No orders found</div>
            )}
            {!loading && orders.length > 0 && (
                <TableContainer component={Paper} className="overflow-x-auto">
                    <Table sx={{ minWidth: 650 }} aria-label="seller orders table">
                        <TableHead>
                            <TableRow>
                                <TableCell>Order Id</TableCell>
                                <TableCell align="right">Total Items</TableCell>
                                <TableCell align="right">Total Price</TableCell>
                                <TableCell align="right">Shipping Address</TableCell>
                                <TableCell align="right">Order Status</TableCell>
                                <TableCell align="right">Order Date</TableCell>
                                <TableCell align="right">Update</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {(orders as unknown as SellerOrder[]).map((orderItem: SellerOrder) => (
                                <TableRow
                                    key={orderItem.orderId}
                                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                >
                                    <TableCell component="th" scope="row">
                                        {orderItem.orderId}
                                    </TableCell>
                                    <TableCell align="right">{orderItem.totalItem}</TableCell>
                                    <TableCell align="right">
                                        ₹{orderItem.totalSellingPrice.toLocaleString()}
                                    </TableCell>
                                    <TableCell align="right">{orderItem.shippingAddressCity}</TableCell>
                                    <TableCell align="right">
                                        <Box
                                            sx={{
                                                display: 'inline-block',
                                                px: 1.5,
                                                py: 0.5,
                                                borderRadius: '999px',
                                                border: `1px solid ${orderStatusColor[orderItem.orderStatus] ?? '#999'}`,
                                                color: orderStatusColor[orderItem.orderStatus] ?? '#999',
                                                fontSize: '0.75rem',
                                            }}
                                        >
                                            {orderItem.orderStatus}
                                        </Box>
                                    </TableCell>
                                    <TableCell align="right">
                                        {new Date(orderItem.orderDate).toLocaleDateString()}
                                    </TableCell>
                                    <TableCell align="right">
                                        <Button
                                            size="small"
                                            color="primary"
                                            onClick={(e) => handleClick(e, orderItem.orderId)}
                                        >
                                            Status
                                        </Button>
                                        <Menu
                                            anchorEl={anchorEl[orderItem.orderId]}
                                            open={Boolean(anchorEl[orderItem.orderId])}
                                            onClose={() => handleClose(orderItem.orderId)}
                                        >
                                            {orderStatusOptions.map((status) => (
                                                <MenuItem
                                                    key={status}
                                                    onClick={() => handleUpdateOrder(orderItem.orderId, status)}
                                                >
                                                    {status}
                                                </MenuItem>
                                            ))}
                                        </Menu>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}
        </div>
    );
};

export default Orders;