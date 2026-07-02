import {
    Divider,

} from "@mui/material";
import ProfileFieldCard from "./ProfileFieldCard.tsx";


const UserDetails = () => {

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

                        <ProfileFieldCard keys={"name"} value={"Riston"} />
                        <Divider />
                        <ProfileFieldCard keys={"email"} value={"xyz@gmail.com"} />
                        <Divider />
                        <ProfileFieldCard keys={"Mobile"} value={"8976756546"} />
                    </div>
                </div>
            </div>


        </div>
    );
};

export default UserDetails;