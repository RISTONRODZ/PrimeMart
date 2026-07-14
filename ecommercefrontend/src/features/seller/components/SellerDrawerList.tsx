import AddIcon from "@mui/icons-material/Add";
import {
    AccountBalanceWallet,
    AccountBox,
    Dashboard,
    Inventory,
    Logout,
    Receipt,
    ShoppingBag
} from "@mui/icons-material";
import DrawerList from "../../../components/DrawerList.tsx";

const menu = [
    // {
    //     name: "Dashboard",
    //     path: "/seller",
    //     icon: <Dashboard className="text-blue-700" />,
    //     activeIcon: <Dashboard className="text-white" />,
    // },
    {
        name: "Orders",
        path: "/seller/orders",
        icon: <ShoppingBag className="text-blue-700" />,
        activeIcon: <ShoppingBag className="text-white" />,
    },
    {
        name: "Products",
        path: "/seller/products",
        icon: <Inventory className="text-blue-700" />,
        activeIcon: <Inventory className="text-white" />,
    },
    {
        name: "Add Product",
        path: "/seller/products/add",
        icon: <AddIcon className="text-blue-700" />,
        activeIcon: <AddIcon className="text-white" />,
    },
    // {
    //     name: "Payment",
    //     path: "/seller/payments",
    //     icon: <AccountBalanceWallet className="text-blue-700" />,
    //     activeIcon: <AccountBalanceWallet className="text-white" />,
    // },
    {
        name: "Transaction",
        path: "/seller/transactions",
        icon: <Receipt className="text-blue-700" />,
        activeIcon: <Receipt className="text-white" />,
    },
    // {
    //   name: "Inventory",
    //   path: "/seller/inventory",
    //   icon: <MailIcon className="text-primary-color" />,
    //   activeIcon: <MailIcon className="text-white" />,
    // },
];

const menu2 = [

    {
        name: "Account",
        path: "/seller/profile",
        icon: <AccountBox className="text-blue-700" />,
        activeIcon: <AccountBox className="text-white" />,
    },
    {
        name: "Logout",
        path: "/",
        icon: <Logout className="text-blue-700" />,
        activeIcon: <Logout className="text-white" />,
    },
];
const SellerDrawerList = ({toggleDrawer}: {toggleDrawer: () => void}) => {
    return (
        <div>
           <DrawerList menu={menu} menu2={menu2} toggleDrawer={toggleDrawer}/>
        </div>
    );
};

export default SellerDrawerList;