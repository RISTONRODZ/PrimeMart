import {createAsyncThunk, createSlice, type PayloadAction} from '@reduxjs/toolkit';
import {api} from "../../config/Api.ts";
import type {HomeCategory, HomeData} from "../../types/HomeCategory.ts";
import axios from "axios";


// Async thunk to fetch home page data with try-catch for error handling
export const fetchHomePageData = createAsyncThunk<HomeData, void, { rejectValue: string }>(
    'home/fetchHomePageData',
    async (_, { rejectWithValue }) => {
        try {
            const response = await api.get('/home-page');
            console.log("home page ",response.data)
            return response.data.data;
        } catch (error: unknown) {
            const errorMessage = axios.isAxiosError(error)
                ? error.message || 'Failed to fetch home page data'
                : 'Failed to fetch home page data';
            console.log("errr ",errorMessage,error)
            return rejectWithValue(errorMessage);
        }
    }
);
interface HomeState {
    homePageData: HomeData | null;
    homeCategories: HomeCategory[];
    loading: boolean;
    error: string | null;
}

const initialState: HomeState = {
    homePageData:null,
    homeCategories: [],
    loading: false,
    error: null,
};

const homeSlice = createSlice({
    name: 'home',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        // Handle fetchHomePageData lifecycle
        builder.addCase(fetchHomePageData.pending, (state) => {
            state.loading = true;
            state.error = null;
        });
        builder.addCase(fetchHomePageData.fulfilled, (state, action: PayloadAction<HomeData>) => {
            state.loading = false;
            state.homePageData = action.payload;
        });
        builder.addCase(fetchHomePageData.rejected, (state, action) => {
            state.loading = false;
            state.error = action.payload || action.error.message || 'Failed to load home page data';
        });

        // Handle createHomeCategories lifecycle
        builder.addCase(createHomeCategories.pending, (state) => {
            state.loading = true;
            state.error = null;
        });
        builder.addCase(createHomeCategories.fulfilled, (state, action) => {
            state.loading = false;
            state.homePageData = action.payload;
        });
        builder.addCase(createHomeCategories.rejected, (state, action) => {
            state.loading = false;
            state.error = action.error.message || 'Failed to create home categories';
        });
    },
});

export default homeSlice.reducer;

export const createHomeCategories = createAsyncThunk<HomeData, HomeCategory[]>(
    'home/createHomeCategories',
    async (homeCategories, { rejectWithValue }) => {
        try {
            const response = await api.post('/home-categories', homeCategories);
            console.log("home categories ",response.data)
            return response.data;
        } catch (error: any) {
            // Handle the error and return it to be used in rejected action
            const errorMessage = error.response?.data?.message || error.message || 'Failed to create home categories';
            console.log("errr ",errorMessage,error)
            return rejectWithValue(errorMessage);
        }
    }
);