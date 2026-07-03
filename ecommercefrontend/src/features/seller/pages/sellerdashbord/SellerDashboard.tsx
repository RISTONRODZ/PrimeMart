
import {Outlet} from "react-router-dom";
import {useState} from "react";
import {Menu} from "@mui/icons-material";
import {IconButton} from "@mui/material";
import SellerDrawerList from "../../components/SellerDrawerList.tsx";


const SellerDashboard = () => {
    const [mobileOpen, setMobileOpen] = useState(false);

    const toggleDrawer = () => {
        setMobileOpen(!mobileOpen);
    };

    return (
        <div className="min-h-screen text-slate-800">
            <section className="lg:flex lg:h-[90vh]">
                <div className={`lg:block fixed inset-y-0 left-0 z-50 w-64 bg-white transform transition-transform duration-300 ease-in-out ${mobileOpen ? 'translate-x-0' : '-translate-x-full'} lg:translate-x-0 lg:static lg:inset-auto`}>
                    <SellerDrawerList toggleDrawer={toggleDrawer}/>
                </div>
                {mobileOpen && (
                    <div 
                        className="fixed inset-0 bg-black/20 backdrop-blur-sm z-40 lg:hidden"
                        onClick={toggleDrawer}
                    />
                )}
                <div className="p-4 sm:p-6 lg:p-10 w-full lg:w-[80%] overflow-y-auto">
                    <div className="lg:hidden mb-4">
                        <IconButton onClick={toggleDrawer} edge="start" color="inherit">
                            <Menu />
                        </IconButton>
                    </div>
                    <Outlet />
                </div>
            </section>
        </div>
    );
};

export default SellerDashboard;