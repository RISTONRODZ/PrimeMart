import { createAsyncThunk, createSlice, type PayloadAction } from "@reduxjs/toolkit";
import { api } from "../../config/Api.ts";
import type { Wishlist, WishlistState } from "../../types/WishlistTypes.ts";

const initialState: WishlistState = {
    wishlist: null,
    loading: false,
    updating: false,
    error: null,
};

export const getWishlistByUserId = createAsyncThunk(
    "wishlist/getWishlistByUserId",
    async (_, { rejectWithValue }) => {
        try {
            const response = await api.get(`/wishlist`, {
                headers: { Authorization: `Bearer ${localStorage.getItem("jwt")}` },
            });
            return response.data.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data.message || "Failed to fetch wishlist");
        }
    }
);

export const addProductToWishlist = createAsyncThunk(
    "wishlist/addProductToWishlist",
    async ({ productId }: { productId: number }, { rejectWithValue }) => {
        try {
            const response = await api.post(
                `/wishlist/add-product/${productId}`,
                {},
                { headers: { Authorization: `Bearer ${localStorage.getItem("jwt")}` } }
            );
            return response.data.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data.message || "Failed to add product to wishlist");
        }
    }
);

export const removeProductFromWishlist = createAsyncThunk(
    "wishlist/removeProductFromWishlist",
    async ({ productId }: { productId: number }, { rejectWithValue }) => {
        try {
            const response = await api.delete(
                `/wishlist/remove-product/${productId}`,
                { headers: { Authorization: `Bearer ${localStorage.getItem("jwt")}` } }
            );
            return response.data.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data.message || "Failed to remove product from wishlist");
        }
    }
);

const wishlistSlice = createSlice({
    name: "wishlist",
    initialState,
    reducers: {
        resetWishlistState: (state) => {
            state.wishlist = null;
            state.loading = false;
            state.updating = false;
            state.error = null;
        },
    },
    extraReducers: (builder) => {
        // getWishlistByUserId
        builder.addCase(getWishlistByUserId.pending, (state) => {
            state.loading = true;
            state.error = null;
        });
        builder.addCase(
            getWishlistByUserId.fulfilled,
            (state, action: PayloadAction<Wishlist>) => {
                state.wishlist = action.payload;
                state.loading = false;
            }
        );
        builder.addCase(
            getWishlistByUserId.rejected,
            (state, action: PayloadAction<any>) => {
                state.loading = false;
                state.error = action.payload;
            }
        );

        // addProductToWishlist
        builder.addCase(addProductToWishlist.pending, (state) => {
            state.updating = true;
            state.error = null;
        });
        builder.addCase(
            addProductToWishlist.fulfilled,
            (state, action: PayloadAction<Wishlist>) => {
                state.wishlist = action.payload;
                state.updating = false;
            }
        );
        builder.addCase(
            addProductToWishlist.rejected,
            (state, action: PayloadAction<any>) => {
                state.updating = false;
                state.error = action.payload;
            }
        );

        // removeProductFromWishlist
        builder.addCase(removeProductFromWishlist.pending, (state, action) => {
            state.updating = true;
            state.error = null;
            // optimistic removal — don't wait on the network round trip
            if (state.wishlist) {
                state.wishlist.products = state.wishlist.products.filter(
                    (p) => p.id !== action.meta.arg.productId
                );
            }
        });
        builder.addCase(
            removeProductFromWishlist.fulfilled,
            (state, action: PayloadAction<Wishlist>) => {
                // reconcile with server truth
                state.wishlist = action.payload;
                state.updating = false;
            }
        );
        builder.addCase(
            removeProductFromWishlist.rejected,
            (state, action: PayloadAction<any>) => {
                state.updating = false;
                state.error = action.payload;
            }
        );
    },
});

export const { resetWishlistState } = wishlistSlice.actions;
export default wishlistSlice.reducer;