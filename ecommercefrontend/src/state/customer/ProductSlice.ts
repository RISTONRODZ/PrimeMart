import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { Product } from "../../types/ProductTypes.ts";
import { api, API_URL } from "../../config/Api.ts";
import axios from "axios";

interface PaginatedProductResponse {
    content: Product[];
    page: {
        size: number;
        number: number;
        totalElements: number;
        totalPages: number;
    };
}

interface SearchProductParams {
    query: string;
    category?: string;
    brand?: string;
    color?: string;
    minPrice?: number | string | null;
    maxPrice?: number | string | null;
    minDiscount?: string;
    sort?: string;
    pageNumber?: number;
    pageSize?: number;
}

interface ProductState {
    product: Product | null;
    products: Product[];
    paginatedProducts: PaginatedProductResponse | null;
    totalPages: number;
    loading: boolean;
    error: string | null;
    searchProducts: Product[];
    searchTotalPages: number;
}

const initialState: ProductState = {
    product: null,
    products: [],
    paginatedProducts: null,
    totalPages: 1,
    loading: false,
    error: null,
    searchProducts: [],
    searchTotalPages: 1
};
export const fetchProductById = createAsyncThunk<Product, number>(
    "products/fetchProductById",
    async (productId, { rejectWithValue }) => {
        try {
            const response = await api.get<Product>(`${API_URL}/products/${productId}`);
            return response.data;
        } catch (error: unknown) {
            const message = axios.isAxiosError(error)
                ? (error.response?.data as { message?: string } | undefined)?.message || error.message
                : "Failed to fetch product";
            return rejectWithValue(message);
        }
    }
);

export const searchProduct = createAsyncThunk<PaginatedProductResponse, SearchProductParams>(
    "products/searchProduct",
    async (params, { rejectWithValue }) => {
        try {
            const response = await api.get<PaginatedProductResponse>(`${API_URL}/products/search`, {
                params: {
                    ...params,
                    pageNumber: params.pageNumber ?? 0,
                    pageSize: params.pageSize ?? 20,
                },
            });
            return response.data;
        } catch (error: unknown) {
            const message = axios.isAxiosError(error)
                ? (error.response?.data as { message?: string } | undefined)?.message || error.message
                : "Failed to search products";
            return rejectWithValue(message);
        }
    }
);
export const getAllProducts = createAsyncThunk<PaginatedProductResponse, SearchProductParams, { rejectValue: string }>(
    "products/getAllProducts",
    async (params, { rejectWithValue }) => {
        try {
            const apiParams = { ...params };
            if (apiParams.sort === 'price') {
                apiParams.sort = 'sellingPrice';
            }

            const response = await api.get(`${API_URL}/products`, {
                params: {
                    ...apiParams,
                    pageNumber: apiParams.pageNumber || 0,
                    pageSize: apiParams.pageSize || 9,
                },
            });
            console.log("Fetched products:", response.data);
            return response.data;
        } catch (error: unknown) {
            const message = axios.isAxiosError(error)
                ? (error.response?.data as { message?: string } | undefined)?.message || error.message
                : "Failed to fetch products";
            return rejectWithValue(message);
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
            .addCase(searchProduct.fulfilled, (state, action: PayloadAction<PaginatedProductResponse>) => {
                state.searchProducts = action.payload.content;
                state.searchTotalPages = action.payload.page.totalPages;
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
            .addCase(getAllProducts.fulfilled, (state, action: PayloadAction<PaginatedProductResponse>) => {
                state.paginatedProducts = action.payload;
                state.products = action.payload.content;
                state.totalPages = action.payload.page.totalPages;
                state.loading = false;
            })
            .addCase(getAllProducts.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || action.error.message || "Failed to fetch products";
            });
    },
});

export default productSlice.reducer;