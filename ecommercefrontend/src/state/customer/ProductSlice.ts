import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { Product } from "../../types/ProductTypes.ts";
import { api, API_URL } from "../../config/Api.ts";

interface ProductState {
    product: Product | null;
    products: Product[];
    paginatedProducts: any;
    totalPages: number;
    loading: boolean;
    error: string | null;
    searchProduct: Product[];
}

const initialState: ProductState = {
    product: null,
    products: [],
    paginatedProducts: null,
    totalPages: 1,
    loading: false,
    error: null,
    searchProduct: []
};

export const fetchProductById = createAsyncThunk<Product, number>(
    "products/fetchProductById",
    async (productId, { rejectWithValue }) => {
        try {
            const response = await api.get<Product>(`${API_URL}/products/${productId}`);
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response.data);
        }
    }
);

export const searchProduct = createAsyncThunk<Product[], string>(
    "products/searchProduct",
    async (query, { rejectWithValue }) => {
        try {
            const response = await api.get<Product[]>(`${API_URL}/products/search`, {
                params: { query },
            });
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response.data);
        }
    }
);

export const getAllProducts = createAsyncThunk(
    "products/getAllProducts",
    async (params: any, { rejectWithValue }) => {
        try {
            const apiParams = { ...params };
            if (apiParams.sort === 'price') {
                apiParams.sort = 'sellingPrice';
            }

            const response = await api.get(`${API_URL}/products`, {
                params: {
                    ...apiParams,
                    pageNumber: apiParams.pageNumber || 0,
                },
            });
            console.log("Fetched products:", response.data);
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response.data);
        }
    }
);

const productSlice = createSlice({
    name: "products",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchProductById.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchProductById.fulfilled, (state, action: PayloadAction<Product>) => {
                state.product = action.payload;
                state.loading = false;
            })
            .addCase(fetchProductById.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || "Failed to fetch product";
            })
            .addCase(searchProduct.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(searchProduct.fulfilled, (state, action: PayloadAction<Product[]>) => {
                state.searchProduct = action.payload;
                state.loading = false;
            })
            .addCase(searchProduct.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || "Failed to search products";
            })
            .addCase(getAllProducts.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(getAllProducts.fulfilled, (state, action: PayloadAction<any>) => {
                state.paginatedProducts = action.payload;
                state.products = action.payload.content;
                state.totalPages = action.payload.totalPages;
                state.loading = false;
            })
            .addCase(getAllProducts.rejected, (state, action) => {
                state.loading = false;
                state.error = (action.payload as any)?.message || action.error.message || "Failed to fetch products";
            });
    },
});

export default productSlice.reducer;