import {Divider, ListItemIcon, ListItemText} from "@mui/material";
import {Link, useLocation} from "react-router-dom";

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

    const renderMenuItem = (item: menuItem) => {
        const isActive = location.pathname === item.path;
        return (
            <Link to={item.path} onClick={toggleDrawer} key={item.name}>
                <div className="pr-9 cursor-pointer">
                    <p
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
                    </p>
                </div>
            </Link>
        );
    };

    return (
        <div className="h-full">
            <div className="flex flex-col justify-between h-full w-75 border-r py-5">
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