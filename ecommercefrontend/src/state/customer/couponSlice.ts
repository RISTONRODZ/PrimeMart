import { createSlice, createAsyncThunk, type PayloadAction } from "@reduxjs/toolkit";
import type { Coupon, CouponState } from "../../types/CouponTypes.ts";
import type { Cart } from "../../types/CartTypes.ts";
import { api, API_URL } from "../../config/Api.ts";

// ---- Async thunks ----

export const createCoupon = createAsyncThunk<
    Coupon,
    { coupon: any; jwt: string },
    { rejectValue: string }
>("coupon/createCoupon", async ({ coupon, jwt }, { rejectWithValue }) => {
    try {
        const response = await api.post(`${API_URL}/coupons/create`, coupon, {
            headers: { Authorization: `Bearer ${jwt}` },
        });
        return response.data;
    } catch (error: any) {
        return rejectWithValue(error.response?.data || "Failed to create coupon");
    }
});

export const deleteCoupon = createAsyncThunk<
    string,
    { id: number; jwt: string },
    { rejectValue: string }
>("coupon/deleteCoupon", async ({ id, jwt }, { rejectWithValue }) => {
    try {
        const response = await api.delete(`${API_URL}/coupons/delete/${id}`, {
            headers: { Authorization: `Bearer ${jwt}` },
        });
        return response.data;
    } catch (error: any) {
        return rejectWithValue(error.response?.data || "Failed to delete coupon");
    }
});

export const fetchAllCoupons = createAsyncThunk<
    Coupon[],
    string,
    { rejectValue: string }
>("coupon/fetchAllCoupons", async (jwt, { rejectWithValue }) => {
    try {
        const response = await api.get(`${API_URL}/coupons/all`, {
            headers: { Authorization: `Bearer ${jwt}` },
        });
        return response.data;
    } catch (error: any) {
        return rejectWithValue(error.response?.data || "Failed to fetch coupons");
    }
});

export const applyCoupon = createAsyncThunk<
    Cart,
    { apply: string; code: string; orderValue: number; jwt: string },
    { rejectValue: string }
>("coupon/applyCoupon", async ({ apply, code, orderValue, jwt }, { rejectWithValue }) => {
    try {
        const response = await api.post(`${API_URL}/coupons/apply`, null, {
            params: { apply, code, orderValue },
            headers: { Authorization: `Bearer ${jwt}` },
        });
        return response.data;
    } catch (error: any) {
        return rejectWithValue(error.response?.data?.error || "Failed to apply coupon");
    }
});

// ---- Initial state ----

const initialState: CouponState = {
    coupons: [],
    cart: null,
    loading: false,
    error: null,
    couponCreated: false,
    couponApplied: false,
};

// ---- Slice ----

const couponSlice = createSlice({
    name: "coupon",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            // createCoupon
            .addCase(createCoupon.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.couponCreated = false;
            })
            .addCase(createCoupon.fulfilled, (state, action: PayloadAction<Coupon>) => {
                state.loading = false;
                state.coupons.push(action.payload);
                state.couponCreated = true;
            })
            .addCase(createCoupon.rejected, (state, action: PayloadAction<string | undefined>) => {
                state.loading = false;
                state.error = action.payload || "Failed to create coupon";
                state.couponCreated = false;
            })
            // deleteCoupon
            .addCase(deleteCoupon.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(deleteCoupon.fulfilled, (state, action) => {
                state.loading = false;
                state.coupons = state.coupons.filter(
                    (coupon) => coupon.id !== parseInt(action.meta.arg.id.toString())
                );
            })
            .addCase(deleteCoupon.rejected, (state, action: PayloadAction<string | undefined>) => {
                state.loading = false;
                state.error = action.payload || "Failed to delete coupon";
            })
            // fetchAllCoupons
            .addCase(fetchAllCoupons.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.couponCreated = false;
            })
            .addCase(fetchAllCoupons.fulfilled, (state, action: PayloadAction<any>) => {
                state.loading = false;
                const couponsData = action.payload.data || action.payload;
                const couponsArray = Array.isArray(couponsData) ? couponsData : [];
                state.coupons = couponsArray.map((coupon: any) => ({
                    ...coupon,
                    active: coupon.isActive,
                    discountPercentage: Number(coupon.discountPercentage),
                }));
                console.log('Fetched coupons:', state.coupons);
            })
            .addCase(fetchAllCoupons.rejected, (state, action: PayloadAction<string | undefined>) => {
                state.loading = false;
                state.error = action.payload || "Failed to fetch coupons";
            })
            // applyCoupon
            .addCase(applyCoupon.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.couponApplied = false;
            })
            .addCase(applyCoupon.fulfilled, (state, action) => {
                state.loading = false;
                state.cart = action.payload;
                if (action.meta.arg.apply === "true") {
                    state.couponApplied = true;
                }
            })
            .addCase(applyCoupon.rejected, (state, action: PayloadAction<string | undefined>) => {
                state.loading = false;
                state.error = action.payload || "Failed to apply coupon";
                state.couponApplied = false;
            });
    },
});

export default couponSlice.reducer;