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
import BecomeSeller from "./features/seller/login/BecomeSeller.tsx";
import Dashboard from "./features/seller/pages/sellerdashbord/Dashboard.tsx";
import Products from "./features/seller/products/Products.tsx";
import Orders from "./features/seller/orders/Orders.tsx";
import Payment from "./features/seller/payment/Payment.tsx";
import Transaction from "./features/seller/payment/TransactionTable.tsx";
import Profile from "./features/seller/account/Profile.tsx";
import AddProducts from "./features/seller/products/AddProducts.tsx";
import {Login} from "@mui/icons-material";
import SellerDashboard from "./features/seller/pages/sellerdashbord/SellerDashboard.tsx";
import AdminDashboard from "./admin/pages/dashboard/AdminDashboard.tsx";
import AdminDashboardContent from "./admin/pages/dashboard/AdminDashboardContent.tsx";
import Coupon from "./admin/pages/coupon/Coupon.tsx";
import AddNewCouponForm from "./admin/pages/coupon/AddNewCouponForm.tsx";
import GridTable from "./admin/HomePage/GridTable.tsx";
import Deal from "./admin/HomePage/Deal.tsx";
import ShopByCategory from "./admin/HomePage/ShopByCategory.tsx";
import SellerTable from "./admin/pages/seller/SellerTable.tsx";
import ElectronicCategory from "./admin/HomePage/ElectronicCategory.tsx";

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
                <Route path="/seller" element={<SellerDashboard />}>
                    <Route index element={<Dashboard />} />
                    <Route path="products" element={<Products />} />
                    <Route path="products/add" element={<AddProducts />} />
                    <Route path="orders" element={<Orders />} />
                    <Route path="payments" element={<Payment />} />
                    <Route path="transactions" element={<Transaction />} />
                    <Route path="profile" element={<Profile />} />
                </Route>
                <Route path="/admin" element={<AdminDashboard />}>
                    <Route index element={<AdminDashboardContent />} />
                    <Route path="sellers" element={<SellerTable />} />
                    <Route path="coupon" element={<Coupon />} />
                    <Route path="add-coupon" element={<AddNewCouponForm />} />
                    <Route path="home-grid" element={<GridTable />} />
                    <Route path="electronics-category" element={< ElectronicCategory/>} />
                    <Route path="shop-by-category" element={<ShopByCategory />} />
                    <Route path="deals" element={<Deal />} />
                    <Route path="account" element={<Account />} />
                </Route>
            </Routes>
        </div>
    );
};

export default AppRoutes;