import { createSlice, createAsyncThunk, type PayloadAction } from '@reduxjs/toolkit';
import type {Order, OrderStatus} from "../../types/OrderTypes.ts";
import {api} from "../../config/Api.ts";

interface SellerOrderState {
    orders: Order[];
    loading: boolean;
    error: string | null;
}

const initialState: SellerOrderState = {
    orders: [],
    loading: false,
    error: null,
};

// Thunks for async actions
export const fetchSellerOrders = createAsyncThunk<Order[], string>(
    'sellerOrders/fetchSellerOrders',
    async (jwt, { rejectWithValue }) => {
        try {
            const response = await api.get('/seller/orders', {
                headers: { Authorization: `Bearer ${jwt}` },
            });

            console.log("fetch seller orders",response.data)
            return response.data.data;
        } catch (error: any) {
            console.log("error",error.response)
            return rejectWithValue(error.response.data);
        }
    }
);

export const updateOrderStatus = createAsyncThunk<Order,
    { jwt: string,
        orderId: string,
        orderStatus: OrderStatus
    }>(
    'sellerOrders/updateOrderStatus',
    async ({ jwt, orderId, orderStatus }, { rejectWithValue }) => {
        try {
            const response = await api.patch(`/seller/orders/${orderId}/status/${orderStatus}`,
                null, {
                    headers: { Authorization: `Bearer ${jwt}` },
                });
            console.log("order status updated", response.data)
            return response.data.data;
        } catch (error: any) {
            return rejectWithValue(error.response.data);
        }
    }
);


export const deleteOrder = createAsyncThunk<any, { jwt: string, orderId: string }>(
    'sellerOrders/deleteOrder',
    async ({ jwt, orderId }, { rejectWithValue }) => {
        try {
            const response = await api.delete(`/seller/orders/${orderId}/delete`, {
                headers: { Authorization: `Bearer ${jwt}` },
            });
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response.data);
        }
    }
);

const sellerOrderSlice = createSlice({
    name: 'sellerOrders',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchSellerOrders.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchSellerOrders.fulfilled, (state, action: PayloadAction<Order[]>) => {
                state.loading = false;
                state.orders = action.payload;
            })
            .addCase(fetchSellerOrders.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            .addCase(updateOrderStatus.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(updateOrderStatus.fulfilled, (state, action: PayloadAction<Order>) => {
                state.loading = false;
                state.orders = state.orders.map((o) =>
                    o.orderId === action.payload.orderId
                        ? { ...o, orderStatus: action.payload.orderStatus }
                        : o
                );
            })
            .addCase(updateOrderStatus.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            .addCase(deleteOrder.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(deleteOrder.fulfilled, (state, action) => {
                state.loading = false;
                state.orders = state.orders.filter(order => order.orderId !== action.meta.arg.orderId);
            })
            .addCase(deleteOrder.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            });
    },
});

export default sellerOrderSlice.reducer;