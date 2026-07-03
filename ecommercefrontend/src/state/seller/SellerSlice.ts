import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import {api} from "../../config/Api.ts";

interface SellerState {
    sellers: any[];
    selectedSeller: any;
    profile: any;
    report: any;
    loading: boolean;
    error: any;
}
const initialState:SellerState={
    sellers:[],
    selectedSeller:null,
    profile:null,
    report:null,
    loading:false,
    error:null
}
export const fetchSellerProfile = createAsyncThunk(
    "seller/fetchProfile",
    async (jwt:string) => {
        try {
            const response = await api.get(`/seller/profile`, {
                headers: {
                    Authorization: `Bearer ${jwt}`
                }
            });
            console.log("Fetched seller profile:", response.data);
            return response.data;
        } catch (error) {
            console.error("Error fetching seller profile:", error);
            throw error;
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
            });
    },
});

export const {clearSellerProfile} = sellerSlice.actions;
export default sellerSlice.reducer;