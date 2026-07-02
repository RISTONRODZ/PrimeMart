import {Route, Routes} from "react-router-dom";
import Home from "./features/customer/home/Home.tsx";
import AboutUs from "./components/footer/Aboutus.tsx";
import PrivacyPolicy from "./components/footer/PrivacyPolicy.tsx";
import TermsOfUse from "./components/footer/TermsOfUse.tsx";
import FAQ from "./components/footer/FAQ.tsx";
import ContactSupport from "./components/footer/Contactsupport.tsx";
import ShippingInfo from "./components/footer/Shippinginfo.tsx";
import Account from "./components/account/Account.tsx";
import Product from "./features/customer/home/product/Product.tsx";
import ProductDetails from "./features/customer/home/product/ProductDetails.tsx";
import Review from "./components/review/Review.tsx";
import Cart from "./components/cart/Cart.tsx";
import Checkout from "./components/checkout/Checkout.tsx";
import {Login} from "@mui/icons-material";
import BecomeSeller from "./features/seller/login/BecomeSeller.tsx";
import SellerDashboard from "./features/seller/pages/sellerdashbord/SellerDashboard.tsx";

const AppRoutes = () => {
    return (
        <div>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/about" element={<AboutUs />} />
                <Route path="/privacy-policy" element={<PrivacyPolicy />} />
                <Route path="/terms-of-use" element={<TermsOfUse />} />
                <Route path="/faqs" element={<FAQ />} />
                <Route path="/contact-support" element={<ContactSupport />} />
                <Route path="/shipping-info" element={<ShippingInfo />} />
                <Route path="/account/*" element={<Account />} />
                <Route path="/product/:category" element={<Product />} />
                <Route path="/product-details/:categoryId/:name/:productId" element={<ProductDetails />} />
                <Route path="/reviews/:productId" element={<Review/>}/>
                <Route path="/cart" element={<Cart/>}/>
                <Route path="/checkout" element={<Checkout/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/become-seller" element={<BecomeSeller/>}/>
                <Route path="/seller/*" element={<SellerDashboard/>}/>
            </Routes>
        </div>
    );
};

export default AppRoutes;