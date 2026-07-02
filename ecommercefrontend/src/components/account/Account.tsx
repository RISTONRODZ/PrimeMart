import { Divider } from "@mui/material";
import { Link, Route, Routes } from "react-router-dom";
import { useState } from "react";
import { Menu } from "@mui/icons-material";
import { IconButton } from "@mui/material";

import Address from "./Address";
import Order from "./Order";
import OrderDetails from "./OrderDetails";
import UserDetails from "./UserDetails";

const menu = [
    { name: "Orders", path: "/account/orders" },
    { name: "Profile", path: "/account/profile" },
    { name: "Addresses", path: "/account/addresses" },
    { name: "Logout", path: "/" },
];

const Account = () => {
    const [mobileOpen, setMobileOpen] = useState(false);

    const toggleDrawer = () => {
        setMobileOpen(!mobileOpen);
    };

    return (
        <div className="px-4 sm:px-5 lg:px-52 min-h-screen mt-10">
            <div>
                <h1 className="text-xl font-bold pb-5 text-blue-700">Riston</h1>
            </div>

            <Divider />

            <div className="grid grid-cols-1 lg:grid-cols-3 lg:min-h-[78vh]">
                <section className="col-span-1 lg:border-r lg:pr-5 py-5 h-full">
                    <div className="lg:hidden mb-4">
                        <IconButton onClick={toggleDrawer} edge="start" color="inherit">
                            <Menu />
                        </IconButton>
                    </div>
                    <div className={`lg:block fixed inset-y-0 left-0 z-50 w-64 bg-white transform transition-transform duration-300 ease-in-out ${mobileOpen ? 'translate-x-0' : '-translate-x-full'} lg:translate-x-0 lg:static lg:inset-auto lg:w-full`}>
                        <div className="lg:hidden mb-4 p-4 border-b">
                            <IconButton onClick={toggleDrawer} edge="start" color="inherit">
                                <Menu />
                            </IconButton>
                        </div>
                        {menu.map((item) => (
                            <Link to={item.path} onClick={toggleDrawer} key={item.name}>
                                <div
                                    className="py-3 px-2 cursor-pointer hover:text-white hover:bg-blue-700 hover:rounded"
                                >
                                    {item.name}
                                </div>
                            </Link>
                        ))}
                    </div>
                    {mobileOpen && (
                        <div 
                            className="fixed inset-0 bg-black/20 backdrop-blur-sm z-40 lg:hidden"
                            onClick={toggleDrawer}
                        />
                    )}
                </section>

                <section className="lg:col-span-2 py-5 lg:pl-5">
                    <Routes>
                        <Route index element={<UserDetails />} />
                        <Route path="profile" element={<UserDetails />} />
                        <Route path="orders" element={<Order />} />
                        <Route
                            path="orders/orderId/:orderItemId"
                            element={<OrderDetails />}
                        />
                        <Route path="addresses" element={<Address />} />
                    </Routes>
                </section>
            </div>
        </div>
    );
};

export default Account;