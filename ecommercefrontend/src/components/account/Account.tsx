import { Divider } from "@mui/material";
import { Link, Route, Routes } from "react-router-dom";

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
    return (
        <div className="px-5 lg:px-52 min-h-screen mt-10">
            <div>
                <h1 className="text-xl font-bold pb-5 text-blue-700">Riston</h1>
            </div>

            <Divider />

            <div className="grid grid-cols-1 lg:grid-cols-3 lg:min-h-[78vh]">
                <section className="col-span-1 lg:border-r lg:pr-5 py-5 h-full">
                    {menu.map((item) => (
                        <Link to={item.path}> <div
                            key={item.name}
                            className="py-3 px-2 cursor-pointer hover:text-white hover:bg-blue-700 hover:rounded"
                        >
                            {item.name}
                        </div>
                        </Link>
                    ))}
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