import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import {api} from "../../config/Api.ts";

export interface ReviewUser {
    id: number;
    email: string;
    fullName: string;
    mobile: string;
    role: string;
}

export interface Review {
    id: number;
    reviewText: string;
    rating: number;
    productImages: string[];
    user: ReviewUser;
    createdAt: string;
}

export interface CreateReviewRequest {
    reviewText: string;
    reviewRating: number;
    productImages: string[];
}

interface ReviewState {
    reviews: Review[];
    loading: boolean;
    error: string | null;
    actionLoading: boolean;
    actionError: string | null;
}

const initialState: ReviewState = {
    reviews: [],
    loading: false,
    error: null,
    actionLoading: false,
    actionError: null,
};

export const fetchReviewsByProductId = createAsyncThunk(
    "review/fetchReviewsByProductId",
    async (productId: number, { rejectWithValue }) => {
        try {
            const { data } = await api.get<Review[]>(`/reviews/products/${productId}/reviews`);
            return data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || "Failed to fetch reviews");
        }
    }
);

export const createReview = createAsyncThunk(
    "review/createReview",
    async (
        { productId, jwt, request }: { productId: number; jwt: string; request: CreateReviewRequest },
        { rejectWithValue }
    ) => {
        try {
            const { data } = await api.post<Review>(
                `/reviews/products/${productId}/reviews`,
                request,
                { headers: { Authorization: jwt } }
            );
            return data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || "Failed to submit review");
        }
    }
);

export const updateReview = createAsyncThunk(
    "review/updateReview",
    async (
        { reviewId, jwt, request }: { reviewId: number; jwt: string; request: CreateReviewRequest },
        { rejectWithValue }
    ) => {
        try {
            const { data } = await api.put<Review>(
                `/reviews/${reviewId}`,
                { reviewText: request.reviewText, reviewRating: request.reviewRating },
                {
                    headers: { Authorization: jwt },
                }
            );
            return data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || "Failed to update review");
        }
    }
);

export const deleteReview = createAsyncThunk(
    "review/deleteReview",
    async ({ reviewId, jwt }: { reviewId: number; jwt: string }, { rejectWithValue }) => {
        try {
            await api.delete(`/reviews/${reviewId}`, { headers: { Authorization: jwt } });
            return reviewId;
        } catch (error: any) {
            return rejectWithValue(error.response?.data?.message || "Failed to delete review");
        }
    }
);

const ReviewSlice = createSlice({
    name: "review",
    initialState,
    reducers: {
        clearReviewActionError: (state) => {
            state.actionError = null;
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchReviewsByProductId.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchReviewsByProductId.fulfilled, (state, action) => {
                state.loading = false;
                state.reviews = action.payload;
            })
            .addCase(fetchReviewsByProductId.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
            .addCase(createReview.pending, (state) => {
                state.actionLoading = true;
                state.actionError = null;
            })
            .addCase(createReview.fulfilled, (state, action) => {
                state.actionLoading = false;
                state.reviews.unshift(action.payload);
            })
            .addCase(createReview.rejected, (state, action) => {
                state.actionLoading = false;
                state.actionError = action.payload as string;
            })
            .addCase(updateReview.pending, (state) => {
                state.actionLoading = true;
                state.actionError = null;
            })
            .addCase(updateReview.fulfilled, (state, action) => {
                state.actionLoading = false;
                const index = state.reviews.findIndex((r) => r.id === action.payload.id);
                if (index !== -1) {
                    state.reviews[index] = action.payload;
                }
            })
            .addCase(updateReview.rejected, (state, action) => {
                state.actionLoading = false;
                state.actionError = action.payload as string;
            })
            .addCase(deleteReview.pending, (state) => {
                state.actionLoading = true;
                state.actionError = null;
            })
            .addCase(deleteReview.fulfilled, (state, action) => {
                state.actionLoading = false;
                state.reviews = state.reviews.filter((r) => r.id !== action.payload);
            })
            .addCase(deleteReview.rejected, (state, action) => {
                state.actionLoading = false;
                state.actionError = action.payload as string;
            });
    },
});

export const { clearReviewActionError } = ReviewSlice.actions;
export default ReviewSlice.reducer;