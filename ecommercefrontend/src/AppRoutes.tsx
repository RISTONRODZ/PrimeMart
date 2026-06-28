import {Route, Routes} from "react-router-dom";
import Home from "./features/customer/home/Home.tsx";
import AboutUs from "./components/layout/Aboutus.tsx";

const AppRoutes = () => {
    return (
        <div>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/about" element={<AboutUs />} />
            </Routes>
        </div>
    );
};

export default AppRoutes;