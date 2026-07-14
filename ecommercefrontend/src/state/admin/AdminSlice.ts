import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {api, API_URL} from "../../config/Api.ts";
import type {HomeCategory} from "../../types/HomeCategory.ts";


export const updateHomeCategory = createAsyncThunk<HomeCategory, {
    id: number;
    data: HomeCategory
}>('homeCategory/updateHomeCategory', async ({id, data}, {rejectWithValue}) => {
    try {
        const response = await api.patch(`${API_URL}/home-categories/${id}`, data);
        console.log("category updated ", response)
        return response.data.data;
    } catch (error: any) {
        console.error("SAVE ERROR:", error);
        // setError(typeof error === "string" ? error : "Save failed.");
        if (error.response && error.response.data) {

            return rejectWithValue(error.response.data);
        } else {
            return rejectWithValue('An error occurred while updating the category.');
        }
    }
});

export const fetchHomeCategories = createAsyncThunk<HomeCategory[]>('homeCategory/fetchHomeCategories', async (_, {rejectWithValue}) => {
    try {
        const response = await api.get(`${API_URL}/home-categories`);
        console.log(" categories ", response.data)
        return response.data.data;
    } catch (error: any) {
        console.log("error ", error.response)
        return rejectWithValue(error.response?.data?.message || 'Failed to fetch categories');
    }
});
export const createHomeCategory = createAsyncThunk<unknown, HomeCategory>('homeCategory/createHomeCategory', async (data, {rejectWithValue}) => {
    try {
        const response = await api.post(`${API_URL}/home-categories`, [data]);
        return response.data.data;
    } catch (error: any) {
        if (error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        }
        return rejectWithValue('An error occurred while creating the category.');
    }
});

interface HomeCategoryState {
    categories: HomeCategory[];
    loading: boolean;
    error: string | null;
    categoryUpdated: boolean;
}

const initialState: HomeCategoryState = {
    categories: [], loading: false, error: null, categoryUpdated: false,
};

// Create the slice
export const homeCategorySlice = createSlice({
    name: 'homeCategory', initialState, reducers: {}, extraReducers: (builder) => {
        // Handle the pending state for updateHomeCategory
        builder.addCase(updateHomeCategory.pending, (state) => {
            state.loading = true;
            state.error = null;
            state.categoryUpdated = false;
        });

        // Handle the fulfilled state for updateHomeCategory
        builder.addCase(updateHomeCategory.fulfilled, (state, action) => {
            state.loading = false;
            state.categoryUpdated = true;
            const index = state.categories.findIndex((category) => category.id === action.payload.id);
            if (index !== -1) {
                state.categories[index] = action.payload;
            } else {
                state.categories.push(action.payload);
            }
        });

        // Handle the rejected state for updateHomeCategory
        builder.addCase(updateHomeCategory.rejected, (state, action) => {
            state.loading = false;
            state.error = action.payload as string;
        });

        // fetch home category
        builder.addCase(fetchHomeCategories.pending, (state) => {
            state.loading = true;
            state.error = null;
            state.categoryUpdated = false;
        })
            .addCase(fetchHomeCategories.fulfilled, (state, action) => {
                state.loading = false;
                state.categories = action.payload;
            })
            .addCase(fetchHomeCategories.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
        builder.addCase(createHomeCategory.pending, (state) => {
            state.loading = true;
            state.error = null;
            state.categoryUpdated = false;
        });
        builder.addCase(createHomeCategory.fulfilled, (state) => {
            state.loading = false;
            state.categoryUpdated = true;
        });
        builder.addCase(createHomeCategory.rejected, (state, action) => {
            state.loading = false;
            state.error = action.payload as string;
        });
    },
});
export default homeCategorySlice.reducer;