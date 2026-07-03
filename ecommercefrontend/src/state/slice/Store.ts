import { combineReducers, configureStore } from "@reduxjs/toolkit";
import sellerSlice from "../seller/SellerSlice.ts";
import sellerProductSlice from "../seller/SellerProductSlice.ts";
import authSlice from "./AuthSlice.ts";

const rootReducer = combineReducers({
    seller: sellerSlice,
    sellerProduct:sellerProductSlice,
    product: sellerProductSlice,
    auth: authSlice,
});

const store = configureStore({
    reducer: rootReducer,
});

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof rootReducer>;

export default store;