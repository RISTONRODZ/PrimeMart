import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import {api} from "../../config/Api.ts";

interface AuthState {
    isAuthenticated: boolean;
    user: any;
    jwt: string | null;
    loading: boolean;
    error: string | null;
}

const initialState: AuthState = {
    isAuthenticated: !!localStorage.getItem("jwt"),
    user: null,
    jwt: localStorage.getItem("jwt"),
    loading: false,
    error: null,
};

export const sendOtp = createAsyncThunk(
    "auth/sendOtp",
    async (
        {email}: { email: string },
        {rejectWithValue}
    ) => {
        try {
            const response = await api.post("/auth/send-otp", {email});

            console.log("OTP sent:", response.data);
            return response.data;
        } catch (error: any) {
            console.error("Error sending OTP:", error);
            return rejectWithValue(
                error.response?.data || error.message
            );
        }
    }
);
// 2. User Signup
export const signup = createAsyncThunk(
    "auth/signup",
    async (userData: any, {rejectWithValue}) => {
        try {
            const response = await api.post("/auth/signup", userData);
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data || error.message);
        }
    }
);

// 3. Login (Handles both User and Seller via email prefix)
export const login = createAsyncThunk(
    "auth/login",
    async (credentials: { email: string; otp: string }, {rejectWithValue}) => {
        try {
            const response = await api.post("/auth/login", credentials);
            console.log(response.data);
            const jwt = response.data.jwt;
            if (jwt) {
                localStorage.setItem("jwt", jwt);
                console.log("JWT stored successfully");
            } else {
                console.error("JWT not found in response");
            }
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data || error.message);
        }
    }
);

// 4. Seller Signup
export const registerSeller = createAsyncThunk(
    "auth/registerSeller",
    async (sellerData: any, {rejectWithValue}) => {
        try {
            const response = await api.post("/auth/signup/seller", sellerData);
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data || error.message);
        }
    }
);
//logout functionality
export const logout = createAsyncThunk<any,any>(
    "auth/logout",
    async (_, {rejectWithValue}) => {
        try {
            localStorage.removeItem("jwt");
            return {message: "Logged out successfully"};
        } catch (error: any) {
            return rejectWithValue(error.response?.data || error.message);
        }
    }
);

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        clearError: (state) => {
            state.error = null;
        },
    },
    extraReducers: (builder) => {
        builder
            // Login
            .addCase(login.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(login.fulfilled, (state, action) => {
                state.loading = false;
                state.isAuthenticated = true;
                state.jwt = action.payload.jwt;
                state.user = action.payload.user;
            })
            .addCase(login.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
                state.isAuthenticated = false;
            })
            // Logout
            .addCase(logout.fulfilled, (state) => {
                state.isAuthenticated = false;
                state.user = null;
                state.jwt = null;
                state.error = null;
            })
            // Signup
            .addCase(signup.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(signup.fulfilled, (state) => {
                state.loading = false;
            })
            .addCase(signup.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            });
    },
});

export const {clearError} = authSlice.actions;
export default authSlice.reducer;