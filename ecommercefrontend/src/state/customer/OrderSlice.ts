import {createAsyncThunk, createSlice, type PayloadAction} from "@reduxjs/toolkit";
import {api} from "../../config/Api.ts";
import type {Address} from "../../types/UserTypes.ts";
import type {Order, OrderItem, OrderState} from "../../types/OrderTypes.ts";
import axios from "axios";

const initialState: OrderState = {
    orders: [],
    orderItem:null,
    currentOrder: null,
    paymentOrder: null,
    loading: false,
    error: null,
    orderCanceled:false
};

const API_URL = "/orders";

export const fetchUserOrderHistory = createAsyncThunk<Order[], string>(
    "orders/fetchUserOrderHistory",
    async (jwt, { rejectWithValue }) => {
        try {
            const response = await api.get<any>(`${API_URL}/history`, {
                headers: { Authorization: `Bearer ${jwt}` },
            });
            console.log("order history fetched ", response.data);

            return response.data.data || response.data;
        } catch (error: any) {
            console.log("error ", error.response);
            return rejectWithValue(
                error.response.data.error || "Failed to fetch order history"
            );
        }
    }
);

export const fetchOrderById = createAsyncThunk<
    Order,
    { orderId: string; jwt: string }
>("orders/fetchOrderById", async ({ orderId, jwt }, { rejectWithValue }) => {
    try {
        const response = await api.get<Order>(`${API_URL}/${orderId}`, {
            headers: { Authorization: `Bearer ${jwt}` },
        });
        return response.data;
    } catch (error: any) {
        return rejectWithValue("Failed to fetch order");
    }
});
// Create a new order
export const createOrder = createAsyncThunk<
    any,
    { address: Address; jwt: string, paymentGateway: string}
>("orders/createOrder", async ({ address, jwt , paymentGateway}, { rejectWithValue }) => {
    try {
        const response = await api.post<any>(API_URL, address, {
            headers: { Authorization: `Bearer ${jwt}` },
            params:{paymentMethod:paymentGateway}
        });
        console.log("order created ", response.data);
        return response.data;
    } catch (error: any) {
        console.log("error ", error.response);
        return rejectWithValue("Failed to create order");
    }
});

export const fetchOrderItemById = createAsyncThunk<
    OrderItem,
    { orderItemId: number; jwt: string }
>("orders/fetchOrderItemById", async ({ orderItemId, jwt }, { rejectWithValue }) => {
    try {
        const response = await api.get(`/orders/item/${orderItemId}`, {
            headers: { Authorization: `Bearer ${jwt}` },
        });
        return response.data.data;
    } catch (error: any) {
        return rejectWithValue("Failed to fetch order item");
    }
});


export const paymentSuccess = createAsyncThunk<
    any,
    { paymentId: string; jwt: string,paymentLinkId:string },
    { rejectValue: string }
>('orders/paymentSuccess', async ({ paymentId, jwt, paymentLinkId }, { rejectWithValue }) => {
    try {
        const response = await api.get(`/payment/${paymentId}`, {
            headers: {
                Authorization: `Bearer ${jwt}`,
            },
            params:{paymentLinkId}
        });

        console.log("payment success ",response.data)

        return response.data;
    } catch (error: any) {
        console.log("error ",error.response)
        if (error.response) {
            return rejectWithValue(error.response.data.message);
        }
        return rejectWithValue('Failed to process payment');
    }
});

export const cancelOrder = createAsyncThunk(
    'orders/cancel',
    async (orderId: string, { rejectWithValue }) => {
        const jwt = localStorage.getItem("jwt") || "";
        try {
            const res = await api.put(
                `${API_URL}/${orderId}/cancel`,
                {},
                { headers: { Authorization: `Bearer ${jwt}` } }
            );
            return res.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.error || "Failed to cancel order");
        }
    }
);

export const deleteOrder = createAsyncThunk(
    'orders/delete',
    async (orderId: string, { rejectWithValue }) => {
        const jwt = localStorage.getItem("jwt") || "";
        try {
            const res = await api.delete(
                `${API_URL}/${orderId}`,
                { headers: { Authorization: `Bearer ${jwt}` } }
            );
            return orderId;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.error || "Failed to delete order");
        }
    }
);

export const fetchSellerOrders = createAsyncThunk<Order[], string>(
    "orders/fetchSellerOrders",
    async (jwt, { rejectWithValue }) => {
        try {
            const response = await api.get<any>(`/seller/orders`, {
                headers: { Authorization: `Bearer ${jwt}` },
            });
            console.log("seller orders fetched ", response.data);

            return response.data.data || response.data;
        } catch (error: any) {
            console.log("error fetching seller orders ", error.response);
            return rejectWithValue(
                error.response.data.error || "Failed to fetch seller orders"
            );
        }
    }
);
const orderSlice = createSlice({
    name: "orders",
    initialState,
    reducers: {
        clearPaymentOrder: (state) => {
            state.paymentOrder = null;
        },
    },
    extraReducers: (builder) => {
        builder
            // Fetch user order history
            .addCase(fetchUserOrderHistory.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.orderCanceled = false;
            })
            .addCase(
                fetchUserOrderHistory.fulfilled,
                (state, action: PayloadAction<Order[]>) => {
                    state.orders = action.payload;
                    state.loading = false;
                }
            )
            .addCase(fetchUserOrderHistory.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })

            // Fetch order by ID
            .addCase(fetchOrderById.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(
                fetchOrderById.fulfilled,
                (state, action: PayloadAction<Order>) => {
                    state.currentOrder = action.payload;
                    state.loading = false;
                }
            )
            .addCase(fetchOrderById.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })

            // Create a new order
            .addCase(createOrder.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(createOrder.fulfilled, (state, action: PayloadAction<any>) => {
                state.paymentOrder = action.payload;
                state.loading = false;
            })
            .addCase(createOrder.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })

            // Fetch Order Item by ID
            .addCase(fetchOrderItemById.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchOrderItemById.fulfilled, (state, action) => {
                state.loading = false;
                state.orderItem = action.payload.data;
            })
            .addCase(fetchOrderItemById.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })

            // payment success handler
            .addCase(paymentSuccess.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(paymentSuccess.fulfilled, (state, action) => {
                state.loading = false;
                console.log('Payment successful:', action.payload);
            })
            .addCase(paymentSuccess.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            .addCase(cancelOrder.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.orderCanceled = false;
            })
            .addCase(cancelOrder.fulfilled, (state, action) => {
                state.loading = false;
                state.orders = state.orders.map((order) =>
                    order.id === action.payload.id ? action.payload : order
                );
                state.orderCanceled = true;
                state.currentOrder = action.payload
            })
            .addCase(cancelOrder.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            .addCase(deleteOrder.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(deleteOrder.fulfilled, (state, action) => {
                state.loading = false;
                state.orders = state.orders.filter((order) => order.id !== action.payload);
            })
            .addCase(deleteOrder.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            .addCase(fetchSellerOrders.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchSellerOrders.fulfilled, (state, action: PayloadAction<Order[]>) => {
                state.orders = action.payload;
                state.loading = false;
            })
            .addCase(fetchSellerOrders.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            });
    },
});

export default orderSlice.reducer;
