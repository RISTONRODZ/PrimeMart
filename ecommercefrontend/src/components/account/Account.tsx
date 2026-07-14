import { Divider, IconButton } from "@mui/material";
import { Link, Route, Routes, useNavigate } from "react-router-dom";
import { useState } from "react";
import { Menu, Close } from "@mui/icons-material";
import { logout } from "../../state/slice/AuthSlice";
import { useAppDispatch } from "../../state/hooks";
import { clearSellerProfile } from "../../state/seller/SellerSlice";
import Address from "./Address";
import Order from "./Order";
import OrderDetails from "./OrderDetails";
import UserDetails from "./UserDetails";
import auth from "../auth/Auth.tsx";

const menu = [
    { name: "Orders", path: "/account/orders" },
    // future implementation
    // { name: "Profile", path: "/account/profile" },
    { name: "Addresses", path: "/account/addresses" },
    { name: "Logout", path: "/" },
];

const Account = () => {
    const [mobileOpen, setMobileOpen] = useState(false);
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const toggleDrawer = () => setMobileOpen(!mobileOpen);
    const handleLogout = () => {
        dispatch(logout(undefined));
        dispatch(clearSellerProfile());
        navigate("/");
        setMobileOpen(false);
    };

    return (
        <div className="px-4 sm:px-6 lg:px-52 min-h-screen mt-10">
            <h1 className="text-xl font-bold pb-5 text-blue-700">{auth.name}</h1>
            <Divider />
            <div className="flex flex-col lg:grid lg:grid-cols-3 lg:min-h-[78vh]">
                <section className="col-span-1 lg:border-r lg:pr-5 py-5">
                    <div className="lg:hidden mb-4 flex items-center justify-between">
                        <span className="font-semibold text-blue-700">Account Menu</span>
                        <IconButton onClick={toggleDrawer}>
                            <Menu />
                        </IconButton>
                    </div>

                    {/* Mobile backdrop */}
                    {mobileOpen && (
                        <div
                            className="fixed inset-0 bg-black/40 z-40 lg:hidden"
                            onClick={toggleDrawer}
                        />
                    )}

                    <div
                        className={`fixed inset-y-0 left-0 z-50 w-64 bg-white shadow-lg transform transition-transform duration-300 ${
                            mobileOpen ? "translate-x-0" : "-translate-x-full"
                        } lg:translate-x-0 lg:static lg:z-auto lg:w-full lg:shadow-none`}
                    >
                        <div className="flex items-center justify-between p-4 lg:hidden">
                            <span className="font-semibold text-blue-700">Menu</span>
                            <IconButton onClick={toggleDrawer}>
                                <Close />
                            </IconButton>
                        </div>

                        <nav className="flex flex-col px-2 lg:px-0">
                            {menu.map((item) =>
                                item.name === "Logout" ? (
                                    <button
                                        key={item.name}
                                        onClick={handleLogout}
                                        className="block w-full text-left py-3 px-3 rounded cursor-pointer hover:text-white hover:bg-blue-700 transition-colors"
                                    >
                                        {item.name}
                                    </button>
                                ) : (
                                    <Link
                                        key={item.name}
                                        to={item.path}
                                        onClick={() => setMobileOpen(false)}
                                        className="block w-full py-3 px-3 rounded cursor-pointer hover:text-white hover:bg-blue-700 transition-colors"
                                    >
                                        {item.name}
                                    </Link>
                                )
                            )}
                        </nav>
                    </div>
                </section>

                <section className="lg:col-span-2 py-5 lg:pl-5">
                    <Routes>
                        <Route index element={<UserDetails />} />
                        <Route path="profile" element={<UserDetails />} />
                        <Route path="orders" element={<Order />} />
                        <Route path="order/:orderId/:orderItemId" element={<OrderDetails />} />
                        <Route path="addresses" element={<Address />} />
                    </Routes>
                </section>
            </div>
        </div>
    );
};

export default Account;