import { createSlice, createAsyncThunk, type PayloadAction } from "@reduxjs/toolkit";
import { api } from "../../config/Api.ts";
import type { Seller } from "../../types/SellerTypes.ts";

interface SellersState {
    sellers: Seller[];
    loading: boolean;
    error: string | null;
    statusUpdated: boolean;
    sellerDeleted: boolean;
}

const initialState: SellersState = {
    sellers: [],
    loading: false,
    error: null,
    statusUpdated: false,
    sellerDeleted: false,
};

export const fetchSellers = createAsyncThunk(
    "adminSellers/fetchSellers",
    async (status: string = "ACTIVE", { rejectWithValue }) => {
        try {
            const url = status ? `/admin?status=${status}` : "/admin";
            const response = await api.get(url, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("jwt")}`,
                },
            });
            console.log("Fetched sellers:", response.data);
            return response.data.data || response.data;
        } catch (error: any) {
            console.log("Error fetching sellers:", error.response);
            return rejectWithValue(
                error.response?.data?.message || "Failed to fetch sellers"
            );
        }
    }
);

export const updateSellerStatus = createAsyncThunk(
    "adminSellers/updateSellerStatus",
    async ({ sellerId, status }: { sellerId: number; status: string }, { rejectWithValue }) => {
        try {
            const response = await api.patch(`/admin/seller/${sellerId}/status/${status}`, {}, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("jwt")}`,
                },
            });
            console.log("Updated seller status:", response.data);
            return { sellerId, status, data: response.data };
        } catch (error: any) {
            console.log("Error updating seller status:", error.response);
            return rejectWithValue(
                error.response?.data?.message || "Failed to update seller status"
            );
        }
    }
);

export const deleteSeller = createAsyncThunk(
    "adminSellers/deleteSeller",
    async (sellerId: number, { rejectWithValue }) => {
        try {
            const response = await api.delete(`/admin/seller/${sellerId}`, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("jwt")}`,
                },
            });
            console.log("Deleted seller:", response.data);
            return { sellerId, data: response.data };
        } catch (error: any) {
            console.log("Error deleting seller:", error.response);
            return rejectWithValue(
                error.response?.data?.message || "Failed to delete seller"
            );
        }
    }
);

const adminSellerSlice = createSlice({
    name: "adminSellers",
    initialState,
    reducers: {
        clearSellerError: (state) => {
            state.error = null;
        },
        clearStatusUpdated: (state) => {
            state.statusUpdated = false;
        },
        clearSellerDeleted: (state) => {
            state.sellerDeleted = false;
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchSellers.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchSellers.fulfilled, (state, action: PayloadAction<Seller[]>) => {
                state.loading = false;
                state.sellers = Array.isArray(action.payload) ? action.payload : [];
            })
            .addCase(fetchSellers.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            .addCase(updateSellerStatus.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.statusUpdated = false;
            })
            .addCase(updateSellerStatus.fulfilled, (state, action) => {
                state.loading = false;
                state.statusUpdated = true;
                const index = state.sellers.findIndex((seller) => seller.id === action.payload.sellerId);
                if (index !== -1) {
                    state.sellers[index].accountStatus = action.payload.status;
                }
            })
            .addCase(updateSellerStatus.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            .addCase(deleteSeller.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.sellerDeleted = false;
            })
            .addCase(deleteSeller.fulfilled, (state, action) => {
                state.loading = false;
                state.sellerDeleted = true;
                state.sellers = state.sellers.filter((seller) => seller.id !== action.payload.sellerId);
            })
            .addCase(deleteSeller.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            });
    },
});

export const { clearSellerError, clearStatusUpdated, clearSellerDeleted } = adminSellerSlice.actions;
export default adminSellerSlice.reducer;
