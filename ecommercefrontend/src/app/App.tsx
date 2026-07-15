import { ThemeProvider } from "@mui/material";
import customTheme from "./theme/customTheme.tsx";
import Navbar from "../components/layout/Navbar.tsx";
import AppRoutes from "../AppRoutes.tsx";
import Footer from "../components/layout/Footer.tsx";
import { useAppDispatch, useAppSelector } from "../state/hooks.ts";
import { useEffect } from "react";
import { fetchSellerProfile } from "../state/seller/SellerSlice.ts";
import { fetchUserProfile, logout } from "../state/slice/AuthSlice.ts";
import { jwtDecode } from "jwt-decode";
import { fetchHomePageData} from "../state/customer/CustomerSlice.ts";
import {fetchHomeCategories} from "../state/admin/AdminSlice.ts";
import ChatWidget from "../components/Chatwidget.tsx";
import { SnackbarProvider } from "../components/ui/Snackbar.tsx";

interface JwtPayload {
    authorities: string;
    exp: number;
}

const App = () => {
    const dispatch = useAppDispatch();
    const jwt = useAppSelector((store) => store.auth.jwt);

    useEffect(() => {
        if (jwt) {
            try {
                const decoded = jwtDecode<JwtPayload>(jwt);
                const currentTime = Date.now() / 1000;
                if (decoded.exp < currentTime) {
                    dispatch(logout());
                    return;
                }
                if (decoded.authorities === "ROLE_SELLER") {
                    dispatch(fetchSellerProfile(jwt));
                }
                // else {
                //     dispatch(fetchUserProfile());
                // }
            } catch {
                dispatch(logout());
            }
        }
    }, [dispatch, jwt]);
    useEffect(() => {
        dispatch(fetchHomeCategories());
    }, [dispatch]);
    useEffect(() => {
            dispatch(fetchHomePageData())
    }, [dispatch, jwt]);
    return (
        <ThemeProvider theme={customTheme}>
            <SnackbarProvider>
                <div className={"text-slate-800"}>
                    <Navbar />
                    <AppRoutes />
                    <Footer />
                    <ChatWidget />
                </div>
            </SnackbarProvider>
        </ThemeProvider>
    );
};

export default App;