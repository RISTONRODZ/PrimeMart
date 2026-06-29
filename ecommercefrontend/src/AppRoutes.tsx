import {Route, Routes} from "react-router-dom";
import Home from "./features/customer/home/Home.tsx";
import AboutUs from "./components/footer/Aboutus.tsx";
import PrivacyPolicy from "./components/footer/PrivacyPolicy.tsx";
import TermsOfUse from "./components/footer/TermsOfUse.tsx";
import FAQ from "./components/footer/FAQ.tsx";
import ContactSupport from "./components/footer/Contactsupport.tsx";
import ShippingInfo from "./components/footer/Shippinginfo.tsx";

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
            </Routes>
        </div>
    );
};

export default AppRoutes;