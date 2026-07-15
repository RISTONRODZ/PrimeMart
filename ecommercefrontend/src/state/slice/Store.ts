import { combineReducers, configureStore } from "@reduxjs/toolkit";
import sellerSlice from "../seller/SellerSlice.ts";
import sellerProductSlice from "../seller/SellerProductSlice.ts";
import productSlice from "../customer/ProductSlice.ts";
import authSlice from "./AuthSlice.ts";
import cartSlice from "../customer/CartSlice.ts";
import orderSlice from "../customer/OrderSlice.ts";
import wishlistSlice from "../customer/WishlistSlice.ts";
import sellerOrderSlice from "../seller/SellerOrderSlice.ts";
import transactionSlice from "./TransactionSlice.ts";
import homeCategorySlice from "../admin/AdminSlice.ts";
import homeSlice from "../customer/CustomerSlice.ts";
import dealSlice from "../admin/DealSlice.ts";
import couponSlice from "../customer/couponSlice.ts";
import adminSellerSlice from "../admin/SellerSlice.ts";
import reviewSlice from "../customer/ReviewSlice.ts";

const rootReducer = combineReducers({
    seller: sellerSlice,
    sellerProduct:sellerProductSlice,
    product: productSlice,
    auth: authSlice,
    cart:cartSlice,
    order:orderSlice,
    wishlist: wishlistSlice,
    sellerOrder: sellerOrderSlice,
    transaction: transactionSlice,
    homeCategory: homeCategorySlice,
    home: homeSlice,
    deal: dealSlice,
    coupon: couponSlice,
    adminSeller: adminSellerSlice,
    review: reviewSlice,
});
const store = configureStore({
    reducer: rootReducer,
});
export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof rootReducer>;
export default store;