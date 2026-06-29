import { ThemeProvider } from "@mui/material";
import customTheme from "./theme/customTheme.tsx";
import Navbar from "./components/layout/Navbar.tsx";
import ProductCard from "./features/customer/home/product/ProductCard.tsx";
import Home from "./features/customer/home/Home.tsx";
import { BrowserRouter } from "react-router";
import ProductDetails from "./components/product/ProductDetails.tsx";

const App = () => {
    return (
        <BrowserRouter>
            <ThemeProvider theme={customTheme}>
                <div>
                    <Navbar />
                    <Home />
                    <ProductCard/>
                    <ProductDetails/>
                </div>
            </ThemeProvider>
        </BrowserRouter>
    );
};

export default App;