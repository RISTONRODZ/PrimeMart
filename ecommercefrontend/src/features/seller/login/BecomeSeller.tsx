import { useState } from "react";
import SellerLoginForm from "./SellerLoginForm.tsx";
import SellerAccountForm from "./SellerAccountForm.tsx";
import { Button } from "@mui/material";
import { useAppDispatch } from "../../../state/hooks.ts";
import { logout } from "../../../state/slice/AuthSlice";

const BecomeSeller = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const dispatch = useAppDispatch();

    const handleLogin = () => {
        dispatch(logout());
        setIsLoggedIn((prev) => !prev);
    };

    return (
        <div className="grid md:gap-10 grid-cols-3 min-h-screen">
            <section className="lg:col-span-1 md:col-span-2 col-span-3 p-10 shadow-lg rounded-b-md">
                {isLoggedIn ? <SellerLoginForm /> : <SellerAccountForm />}

                <div className="mt-10 space-y-2">
                    <h1 className="text-center text-sm font-medium">
                        {isLoggedIn
                            ? "Don't have an account?"
                            : "Already have an account?"}
                    </h1>

                    <Button
                        onClick={handleLogin}
                        fullWidth
                        sx={{ py: "11px" }}
                        variant="outlined"
                    >
                        {isLoggedIn ? "Register" : "Login"}
                    </Button>
                </div>
            </section>
            <section className={'hidden md:col-span-1 lg:col-span-2 md:flex justify-center items-center pb-5 '}>
                    <div className={"lg:w-[70%] space-y-10"}>
                            <div className={'space-y-2 font-bold text-center pt-10'}>
                                <p className={"text-2xl"}>Join the Prime Marketplace</p>
                                <p className={'text-blue-700'}>Boost your sales today</p>
                            </div>
                        <img className={'rounded-2xl'} src="https://i.pinimg.com/1200x/3a/06/e5/3a06e5dddf105c10edbfc9ea9abee306.jpg" alt="become a seller image" />
                    </div>
            </section>
        </div>
    );
};

export default BecomeSeller;