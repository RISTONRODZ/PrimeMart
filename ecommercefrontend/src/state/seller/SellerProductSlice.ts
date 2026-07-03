import { createAsyncThunk, createSlice, type PayloadAction } from "@reduxjs/toolkit";
import { api, API_URL } from "../../config/Api.ts";
import type { Product } from "../../types/ProductTypes.ts";

export const fetchSellerProducts = createAsyncThunk<Product[], any>(
    "sellerProduct/fetchSellerProducts",
    async (jwt, { rejectWithValue }) => {
        try {
            const response = await api.get("/seller/products", {
                headers: { Authorization: `Bearer ${jwt}` },
            });
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.message);
        }
    }
);

export const createProduct = createAsyncThunk<Product, { request: any; jwt: string | null }>(
    'sellerProduct/createProduct',
    async ({ request, jwt }, { rejectWithValue }) => {
        try {
            const response = await api.post<Product>("/seller/products", request, {
                headers: { Authorization: `Bearer ${jwt}` },
            });
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response.data);
        }
    }
);

export const updateProduct = createAsyncThunk<Product, { productId: number; productData: any; jwt: string }>(
    "sellerProduct/updateProduct",
    async ({ productId, productData, jwt }, { rejectWithValue }) => {
        try {
            const response = await api.put(`/seller/products/${productId}`, productData, {
                headers: { Authorization: `Bearer ${jwt}` },
            });
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data || "Failed to update product");
        }
    }
);

interface SellerProductState {
    products: Product[];
    loading: boolean;
    error: string | null;
    productCreated: boolean;
}

const initialState: SellerProductState = {
    products: [],
    loading: false,
    error: null,
    productCreated: false,
};

const sellerProductSlice = createSlice({
    name: 'sellerProduct',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchSellerProducts.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.productCreated = false;
            })
            .addCase(fetchSellerProducts.fulfilled, (state, action: PayloadAction<Product[]>) => {
                state.products = action.payload;
                state.loading = false;
            })
            .addCase(fetchSellerProducts.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch products';
            })
            .addCase(createProduct.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.productCreated = false;
            })
            .addCase(createProduct.fulfilled, (state, action: PayloadAction<Product>) => {
                state.products.push(action.payload);
                state.loading = false;
                state.productCreated = true;
            })
            .addCase(createProduct.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to create product';
                state.productCreated = false;
            })
            .addCase(updateProduct.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(updateProduct.fulfilled, (state, action: PayloadAction<Product>) => {
                state.products = state.products.map((product) =>
                    product.id === action.payload.id ? action.payload : product
                );
                state.loading = false;
            })
            .addCase(updateProduct.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string || 'Failed to update product';
            });
    },
});

export default sellerProductSlice.reducer;