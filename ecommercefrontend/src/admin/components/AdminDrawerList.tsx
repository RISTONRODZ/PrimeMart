import DrawerList from "../../components/DrawerList.tsx";
import {AccountBox, Category, Dashboard, ElectricBolt, Home, IntegrationInstructions, LocalOffer, Logout} from "@mui/icons-material";
import AddIcon from "@mui/icons-material/Add";

const AdminDrawerList = ({toggleDrawer}: {toggleDrawer: () => void}) => {
    const menu = [
        {
            name: "Dashboard",
            path: "/admin",
            icon: <Dashboard className="text-blue-700" />,
            activeIcon: <Dashboard className="text-white" />,
        },
        {
            name: "Coupons",
            path: "/admin/coupon",
            icon: <IntegrationInstructions className="text-blue-700" />,
            activeIcon: <IntegrationInstructions className="text-white" />,
        },
        {
            name: "Add New Coupon",
            path: "/admin/add-coupon",
            icon: <AddIcon className="text-blue-700" />,
            activeIcon: <AddIcon className="text-white" />,
        },
        {
            name: "Home Page",
            path: "/admin/home-grid",
            icon: <Home className="text-blue-700" />,
            activeIcon: <Home className="text-white" />,
        },
        {
            name: "Electronics Category",
            path: "/admin/electronics-category",
            icon: <ElectricBolt className="text-blue-700" />,
            activeIcon: <ElectricBolt className="text-white" />,
        },
        {
            name: "Shop By Category",
            path: "/admin/shop-by-category",
            icon: <Category className="text-blue-700" />,
            activeIcon: <Category className="text-white" />,
        },
        {
            name: "Deals",
            path: "/admin/deals",
            icon: <LocalOffer className="text-blue-700" />,
            activeIcon: <LocalOffer className="text-white" />,
        },

    ];

    const menu2 = [
        // future implementation
        // {
        //     name: "Account",
        //     path: "/admin/account",
        //     icon: <AccountBox className="text-blue-700" />,
        //     activeIcon: <AccountBox className="text-white" />,
        // },
        {
            name: "Logout",
            path: "/",
            icon: <Logout className="text-blue-700" />,
            activeIcon: <Logout className="text-white" />,
        },

    ]
    return (
        <div>
           <DrawerList menu={menu} menu2={menu2} toggleDrawer={toggleDrawer}/>
        </div>
    );
};

export default AdminDrawerList;