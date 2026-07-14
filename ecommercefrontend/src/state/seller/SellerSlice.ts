import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { api } from "../../config/Api.ts";

interface SellerState {
    sellers: any[];
    selectedSeller: any;
    profile: any;
    report: any;
    loading: boolean;
    error: string | null;
}

const initialState: SellerState = {
    sellers: [],
    selectedSeller: null,
    profile: null,
    report: null,
    loading: false,
    error: null,
};

export const fetchSellerProfile = createAsyncThunk(
    "seller/fetchProfile",
    async (jwt: string, { rejectWithValue }) => {
        try {
            const response = await api.get(`/seller/profile`, {
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            });
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data || error.message);
        }
    }
);

export const updateSellerProfile = createAsyncThunk(
    "seller/updateProfile",
    async (profileData: any, { rejectWithValue }) => {
        try {
            console.log("Sending seller profile data to API:", profileData);
            const response = await api.patch(`/seller`, profileData);
            console.log("API response:", response.data);
            return response.data;
        } catch (error: any) {
            console.error("Error updating seller profile:", error);
            const errorMessage = error.response?.data?.message || error.response?.data || error.message;
            return rejectWithValue(errorMessage);
        }
    }
);

const sellerSlice = createSlice({
    name: "seller",
    initialState,
    reducers: {
        clearSellerProfile: (state) => {
            state.profile = null;
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchSellerProfile.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchSellerProfile.fulfilled, (state, action) => {
                state.loading = false;
                state.profile = action.payload;
            })
            .addCase(fetchSellerProfile.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            .addCase(updateSellerProfile.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(updateSellerProfile.fulfilled, (state, action) => {
                state.loading = false;
                state.profile = action.payload;
            })
            .addCase(updateSellerProfile.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            });
    },
});

export const { clearSellerProfile } = sellerSlice.actions;
export default sellerSlice.reducer;