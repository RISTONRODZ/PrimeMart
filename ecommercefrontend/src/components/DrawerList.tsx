import {Divider, ListItemIcon, ListItemText} from "@mui/material";
import {Link, useLocation, useNavigate} from "react-router-dom";
import {logout} from "../state/slice/AuthSlice.ts";
import {useAppDispatch} from "../state/hooks.ts";
import {clearSellerProfile} from "../state/seller/SellerSlice.ts";

interface menuItem {
    name: string;
    path: string;
    icon: React.ReactNode;
    activeIcon: React.ReactNode;
}

interface DrawerListProp {
    menu: menuItem[];
    menu2: menuItem[];
    toggleDrawer: () => void;
}

const DrawerList = ({menu, menu2, toggleDrawer}: DrawerListProp) => {
    const location = useLocation();
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const renderMenuItem = (item: menuItem) => {
        const isActive = location.pathname === item.path;
        const isLogout = item.name === "Logout";

        const handleClick = (e: React.MouseEvent) => {
            if (isLogout) {
                e.preventDefault();
                dispatch(logout(undefined));
                dispatch(clearSellerProfile());
                navigate("/");
                return;
            }
            toggleDrawer();
        };

        if (isLogout) {
            return (
                <div onClick={handleClick} key={item.name}>
                    <div className="pr-9 cursor-pointer">
                        <div
                            className={`
                ${isActive ? "text-black bg-blue-400" : "text-blue-700"}
                hover:bg-blue-200 transition-colors duration-200
                rounded-r-4xl px-2 py-2 flex items-center
              `}
                        >
                            <ListItemIcon className="min-w-10">
                                {isActive ? item.activeIcon : item.icon}
                            </ListItemIcon>
                            <ListItemText primary={item.name}/>
                        </div>
                    </div>
                </div>
            );
        }

        return (
            <Link to={item.path} onClick={handleClick} key={item.name}>
                <div className="pr-9 cursor-pointer">
                    <div
                        className={`
            ${isActive ? "text-black bg-blue-400" : "text-blue-700"} 
            hover:bg-blue-200 transition-colors duration-200 
            rounded-r-4xl px-2 py-2 flex items-center
          `}
                    >
                        <ListItemIcon className="min-w-10">
                            {isActive ? item.activeIcon : item.icon}
                        </ListItemIcon>
                        <ListItemText primary={item.name}/>
                    </div>
                </div>
            </Link>
        );
    };

    return (
        <div className="h-full">
            <div className="flex flex-col justify-between h-full w-full border-r py-5 px-2">
                <div className="space-y-2">
                    {menu.map((item) => renderMenuItem(item))}
                </div>
                <Divider/>
                <div className="space-y-2">
                    {menu2.map((item) => renderMenuItem(item))}
                </div>
            </div>
        </div>
    );
};

export default DrawerList;