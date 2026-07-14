import { createAsyncThunk, createSlice, type PayloadAction } from "@reduxjs/toolkit";
import { api } from "../../config/Api.ts";
import { jwtDecode } from "jwt-decode";

interface UserProfile {
    id: number;
    userName: string;
    email: string;
    mobileNo: string;
}

interface AuthState {
    isAuthenticated: boolean;
    user: UserProfile | null;
    jwt: string | null;
    role: string | null;
    loading: boolean;
    error: string | null;
}

const initialState: AuthState = {
    isAuthenticated: (() => {
        const jwt = localStorage.getItem("jwt");
        if (!jwt) return false;
        try {
            const decoded: { exp: number } = jwtDecode(jwt);
            const currentTime = Date.now() / 1000;
            if (decoded.exp < currentTime) {
                localStorage.removeItem("jwt");
                return false;
            }
            return true;
        } catch {
            localStorage.removeItem("jwt");
            return false;
        }
    })(),
    user: null,
    jwt: (() => {
        const jwt = localStorage.getItem("jwt");
        if (!jwt) return null;
        try {
            const decoded: { exp: number } = jwtDecode(jwt);
            const currentTime = Date.now() / 1000;
            if (decoded.exp < currentTime) {
                localStorage.removeItem("jwt");
                return null;
            }
            return jwt;
        } catch {
            localStorage.removeItem("jwt");
            return null;
        }
    })(),
    loading: false,
    error: null,
    role: (() => {
        const jwt = localStorage.getItem("jwt");
        if (!jwt) return null;
        try {
            const decoded: { exp: number; authorities: string } = jwtDecode(jwt);
            const currentTime = Date.now() / 1000;
            if (decoded.exp < currentTime) return null;
            return decoded.authorities;
        } catch {
            return null;
        }
    })(),
};

export const sendOtp = createAsyncThunk(
    "auth/sendOtp",
    async ({ email }: { email: string }, { rejectWithValue }) => {
        try {
            const response = await api.post("/auth/send-otp", { email });
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data || error.message);
        }
    }
);

export const signup = createAsyncThunk(
    "auth/signup",
    async (userData: any, { rejectWithValue }) => {
        try {
            const response = await api.post("/auth/signup", userData);
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data || error.message);
        }
    }
);

export const login = createAsyncThunk(
    "auth/login",
    async (credentials: { email: string; otp: string }, { rejectWithValue }) => {
        try {
            const response = await api.post("/auth/login", credentials);
            const jwt = response.data.jwt;
            console.log("EXTRACTED JWT:", jwt);
            if (jwt) {
                localStorage.setItem("jwt", jwt);
            }
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data || error.message);
        }
    }
);

export const registerSeller = createAsyncThunk(
    "auth/registerSeller",
    async (sellerData: any, { rejectWithValue }) => {
        try {
            const response = await api.post("/auth/signup/seller", sellerData);
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data || error.message);
        }
    }
);

export const logout = createAsyncThunk("auth/logout", async () => {
    localStorage.removeItem("jwt");
    return { message: "Logged out successfully" };
});

export const fetchUserProfile = createAsyncThunk(
    "auth/fetchUserProfile",
    async (_, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt");
            const response = await api.get("/users/profile", {
                headers: { Authorization: `Bearer ${jwt}` },
            });
            return response.data;
        } catch (error: any) {
            return rejectWithValue(error.response?.data || error.message);
        }
    }
);

export const updateUserProfile = createAsyncThunk(
    "auth/updateUserProfile",
    async (userData: any, { rejectWithValue }) => {
        try {
            const jwt = localStorage.getItem("jwt");
            const response = await api.put("/users/profile", userData, {
                headers: { Authorization: `Bearer ${jwt}` },
            });
            return response.data;
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
            .addCase(sendOtp.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(sendOtp.fulfilled, (state) => {
                state.loading = false;
            })
            .addCase(sendOtp.rejected, (state, action) => {
                state.loading = false;
                state.error = typeof action.payload === 'object' && action.payload !== null && 'message' in action.payload
                    ? (action.payload as any).message
                    : action.payload as string;
            })
            .addCase(login.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(login.fulfilled, (state, action) => {
                state.loading = false;
                state.isAuthenticated = true;
                state.jwt = action.payload.jwt;
                state.user = action.payload.user;
                try {
                    const decoded: { authorities: string } = jwtDecode(action.payload.jwt);
                    state.role = decoded.authorities;
                } catch {
                    state.role = null;
                }
            })
            .addCase(login.rejected, (state, action) => {
                state.loading = false;
                state.error = typeof action.payload === 'object' && action.payload !== null && 'message' in action.payload
                    ? (action.payload as any).message
                    : action.payload as string;
                state.isAuthenticated = false;
            })
            .addCase(logout.fulfilled, (state) => {
                state.isAuthenticated = false;
                state.user = null;
                state.jwt = null;
                state.role = null;
                state.error = null;
            })
            .addCase(signup.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(signup.fulfilled, (state) => {
                state.loading = false;
            })
            .addCase(signup.rejected, (state, action) => {
                state.loading = false;
                state.error = typeof action.payload === 'object' && action.payload !== null && 'message' in action.payload
                    ? (action.payload as any).message
                    : action.payload as string;
            })
            .addCase(registerSeller.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(registerSeller.fulfilled, (state, action) => {
                state.loading = false;
                state.jwt = action.payload.jwt;
                state.isAuthenticated = true;
                try {
                    const decoded: { authorities: string } = jwtDecode(action.payload.jwt);
                    state.role = decoded.authorities;
                } catch {
                    state.role = null;
                }
            })
            .addCase(registerSeller.rejected, (state, action) => {
                state.loading = false;
                state.error = typeof action.payload === 'object' && action.payload !== null && 'message' in action.payload
                    ? (action.payload as any).message
                    : action.payload as string;
            })
            .addCase(fetchUserProfile.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchUserProfile.fulfilled, (state, action: PayloadAction<UserProfile>) => {
                state.loading = false;
                state.isAuthenticated = true;
                state.user = action.payload;
            })
            .addCase(fetchUserProfile.rejected, (state, action) => {
                state.loading = false;
                state.error = typeof action.payload === 'object' && action.payload !== null && 'message' in action.payload
                    ? (action.payload as any).message
                    : action.payload as string;
            })
            .addCase(updateUserProfile.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(updateUserProfile.fulfilled, (state, action: PayloadAction<UserProfile>) => {
                state.loading = false;
                state.user = action.payload;
            })
            .addCase(updateUserProfile.rejected, (state, action) => {
                state.loading = false;
                state.error = typeof action.payload === 'object' && action.payload !== null && 'message' in action.payload
                    ? (action.payload as any).message
                    : action.payload as string;
            });
    },
});

export const { clearError } = authSlice.actions;
export default authSlice.reducer;