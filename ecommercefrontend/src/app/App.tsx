import { ThemeProvider } from "@mui/material";
import customTheme from "./theme/customTheme.tsx";
import Navbar from "../components/layout/Navbar.tsx";
// import Checkout from "../components/checkout/Checkout.tsx";
// import Account from "../components/account/Account.tsx";
// import Cart from "../components/cart/Cart.tsx";
import AppRoutes from "../AppRoutes.tsx";
import Footer from "../components/layout/Footer.tsx";
// import ProductDetails from "../features/customer/home/product/ProductDetails.tsx";
// import Review from "../components/review/Review.tsx";


const App = () => {
    return (
        <ThemeProvider theme={customTheme}>
            <div className={'text-slate-800'}>
                <Navbar />
                <AppRoutes />
                {/*<ProductDetails/>*/}
                {/*<Review/>*/}
                {/*<Cart/>*/}
                {/*<Checkout/>*/}
                {/*<Account/>*/}
                <Footer/>
            </div>
        </ThemeProvider>
    );
};

export default App;
