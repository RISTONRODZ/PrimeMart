import {
    Divider,

} from "@mui/material";
import ProfileFieldCard from "./ProfileFieldCard.tsx";
import {useAppSelector} from "../../state/hooks.ts";


const UserDetails = () => {
    const {auth}=useAppSelector((store)=>store)
    
    console.log("Personal Details Fetched:", {
        userName: auth.user?.userName,
        email: auth.user?.email,
        mobileNo: auth.user?.mobileNo,
        fullUser: auth.user
    });
    
    return (
        <div className="flex justify-center py-6 sm:py-10">
            <div className="w-full lg:w-[70%]  ">
                <div className="flex items-center pb-3 justify-between">
                    <h1 className="text-xl sm:text-2xl font-bold text-gray-600 ">
                        Persional Details
                    </h1>
                </div>
                <div className="space-y-5">
                    <div>
                        <ProfileFieldCard keys={"name"} value={auth.user?.userName || "Not set"} />
                        <Divider />
                        <ProfileFieldCard keys={"email"} value={auth.user?.email || "Not set"} />
                        <Divider />
                        <ProfileFieldCard keys={"Mobile"} value={auth.user?.mobileNo || "Not set"} />
                    </div>
                </div>
            </div>


        </div>
    );
};

export default UserDetails;