import {Avatar, Box, Button, IconButton, useMediaQuery, useTheme} from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu'
import SearchIcon from '@mui/icons-material/Search';
import {AddShoppingCart, FavoriteBorder, Storefront} from "@mui/icons-material";
import {Logo} from "./Logo.tsx";
import CategorySheet, {type CategoryKey} from "../../features/customer/home/categories/CategorySheet.tsx";
import {mainCategory} from "../../features/customer/data/category/mainCategory.ts";
import {useRef, useState} from "react";
import {Link} from "react-router-dom";

const Navbar = () => {
    const theme = useTheme();
    const isLarge = useMediaQuery(theme.breakpoints.up("md"));
    const isSmall = useMediaQuery(theme.breakpoints.up("sm"));
    const [selectedCategory, setSelectedCategory] = useState<CategoryKey>("men");
    const [showCategorySheet, setShowCategorySheet] = useState(false);
    const hideTimeout = useRef<ReturnType<typeof setTimeout> | null>(null);
    const handleMouseEnter = () => {
        if (hideTimeout.current) clearTimeout(hideTimeout.current);
        setShowCategorySheet(true);
    };
    const handleMouseLeave = () => {
        hideTimeout.current = setTimeout(() => {
            setShowCategorySheet(false);
        }, 300);
    };
    const isLoggedIn = true;
    return (<>
        <Box className={'border-b-2 border-blue-600'}>
            <div className={'mr-3'}>
                <div className={'flex items-center justify-between'}>
                    <div className={'flex items-center gap-2 ml-2'}>
                        {!isLarge && <IconButton>
                            <MenuIcon/>
                        </IconButton>}
                        <Logo/>
                        <ul
                            className={'flex items-center text-blue-700 gap-5 pl-2 '}>
                            {isLarge && mainCategory.map((item) => (<li
                                key={item.categoryId}
                                className={'hover:border-b-2 transition-all ease-in duration-100 hover:text-blue-900 font-medium cursor-pointer'}
                                onMouseLeave={handleMouseLeave}
                                onMouseEnter={() => {
                                    handleMouseEnter();
                                    setSelectedCategory(item.categoryId as CategoryKey);
                                }}
                            >
                                {item.name}
                            </li>))}
                        </ul>
                    </div>

                    <div className={'flex gap-2'}>
                        <IconButton>
                            <SearchIcon/>
                        </IconButton>
                        <div className="flex items-center font-semibold gap-0.5">
                            {isLoggedIn ? (
                                <>
                                    <IconButton component={Link} to="/account">
                                        <Avatar
                                            src="https://i.pinimg.com/736x/9e/c0/f8/9ec0f877571edc437f89c15c08081533.jpg"/>
                                    </IconButton>

                                    {isLarge && (
                                        <span className="text-blue-700 font-semibold">
                    Riston
                </span>
                                    )}
                                </>
                            ) : (
                                <Button component={Link} to="/login"
                                        className="bg-blue-700! hover:bg-blue-800! text-white! normal-case! px-5"
                                >
                                    Login
                                </Button>
                            )}

                            <IconButton>
                                <FavoriteBorder className="text-blue-700"/>
                            </IconButton>

                            <IconButton component={Link} to="/cart">
                                <AddShoppingCart className="text-blue-700"/>
                            </IconButton>

                            {isSmall && (

                                <Button component={Link} to="/become-seller"
                                        startIcon={<Storefront className="text-blue-700"/>}
                                        variant="outlined"
                                >
                                    Become Seller
                                </Button>
                            )}
                        </div>
                        {showCategorySheet && <div
                            onMouseLeave={handleMouseLeave}
                            onMouseEnter={handleMouseEnter}
                            className={'categorySheet absolute top-[3.3rem] left-20 right-20 border bg-slate-500'}>
                            <CategorySheet selectedCategory={selectedCategory} setShowSheet={setShowCategorySheet}/>
                        </div>}
                    </div>
                </div>
            </div>
        </Box>
    </>);
};

export default Navbar;
