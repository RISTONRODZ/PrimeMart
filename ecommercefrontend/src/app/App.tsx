import { ThemeProvider } from "@mui/material";
import customTheme from "./theme/customTheme.tsx";
import Navbar from "../components/layout/Navbar.tsx";
import AppRoutes from "../AppRoutes.tsx";
import Footer from "../components/layout/Footer.tsx";
import {useAppDispatch, useAppSelector} from "../state/hooks.ts";
import {useEffect} from "react";
import {fetchSellerProfile} from "../state/seller/SellerSlice.ts";
import {useNavigate, useLocation} from "react-router-dom";


const App = () => {
    const dispatch = useAppDispatch();
    const seller = useAppSelector(store=>store.seller)
    const navigate = useNavigate();
    const location = useLocation();
    useEffect(() => {
        dispatch(fetchSellerProfile(localStorage.getItem("jwt") || ""));
    }, [dispatch]);
    useEffect(() => {
        if(seller.profile && !location.pathname.startsWith("/seller")){
            navigate("/seller");
        }
    }, [seller.profile, navigate, location.pathname]);
    return (
        <ThemeProvider theme={customTheme}>
            <div className={'text-slate-800'}>
                <Navbar />
                <AppRoutes />
                <Footer/>
            </div>
        </ThemeProvider>
    );
};

export default App;
