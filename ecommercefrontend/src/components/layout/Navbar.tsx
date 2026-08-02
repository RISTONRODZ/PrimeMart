import { Avatar, Box, Button, IconButton, useMediaQuery, useTheme, Drawer, List, ListItemButton, ListItemText, Collapse, Grid, InputBase } from "@mui/material";
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import MenuIcon from '@mui/icons-material/Menu';
import SearchIcon from '@mui/icons-material/Search';
import CloseIcon from '@mui/icons-material/Close';
import { AddShoppingCart, FavoriteBorder, Storefront } from "@mui/icons-material";
import { Logo } from "./Logo.tsx";
import CategorySheet, { type CategoryKey } from "../../features/customer/home/categories/CategorySheet.tsx";
import { mainCategory, type MainCategory, type SubCategory } from "../../features/customer/data/category/mainCategory.ts";
import { menLevelThree } from "../../features/customer/data/category/level three/menLevelThree.ts";
import { womenLevelThree } from "../../features/customer/data/category/level three/womenLevelThree.ts";
import { furnitureLevelThree } from "../../features/customer/data/category/level three/furnitureLevelThree.ts";
import { electronicsLevelThree } from "../../features/customer/data/category/level three/electronicsLevelThree.ts";
import React, { useRef, useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAppSelector } from "../../state/hooks.ts";
import { jwtDecode } from "jwt-decode";
import { createPortal } from "react-dom";

const Navbar = () => {
    const theme = useTheme();
    const isLarge = useMediaQuery(theme.breakpoints.up("md"));
    const isSmall = useMediaQuery(theme.breakpoints.up("sm"));
    const [selectedCategory, setSelectedCategory] = useState<CategoryKey>("men");
    const [showCategorySheet, setShowCategorySheet] = useState(false);
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
    const [expandedCategories, setExpandedCategories] = useState<Set<string>>(new Set());
    const [expandedSubCategories, setExpandedSubCategories] = useState<Set<string>>(new Set());
    const [isSeller, setIsSeller] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);

    // Search form states
    const [searchOpen, setSearchOpen] = useState(false);
    const [searchQuery, setSearchQuery] = useState("");

    const hideTimeout = useRef<ReturnType<typeof setTimeout> | null>(null);
    const auth = useAppSelector(store => store.auth);
    const navigate = useNavigate();

    useEffect(() => {
        if (auth.jwt) {
            try {
                const { authorities } = jwtDecode<{ authorities: string }>(auth.jwt);
                setIsSeller(authorities === "ROLE_SELLER");
                setIsAdmin(authorities === "ROLE_ADMIN");
            } catch {
                setIsSeller(false);
                setIsAdmin(false);
            }
        } else {
            setIsSeller(false);
            setIsAdmin(false);
        }
    }, [auth.jwt]);

    const toggleCategory = (categoryId: string) => {
        setExpandedCategories(prev => {
            const newSet = new Set(prev);
            if (newSet.has(categoryId)) newSet.delete(categoryId);
            else newSet.add(categoryId);
            return newSet;
        });
    };

    const toggleSubCategory = (subCategoryId: string) => {
        setExpandedSubCategories(prev => {
            const newSet = new Set(prev);
            if (newSet.has(subCategoryId)) newSet.delete(subCategoryId);
            else newSet.add(subCategoryId);
            return newSet;
        });
    };

    const getLevelThreeCategories = (parentCategoryId: string, mainCategoryId: string) => {
        const dataMap: Record<string, any[]> = {
            "men": menLevelThree,
            "women": womenLevelThree,
            "home_furniture": furnitureLevelThree,
            "electronics": electronicsLevelThree
        };
        return (dataMap[mainCategoryId] || []).filter(item => item.parentCategoryId === parentCategoryId);
    };

    const handleMouseEnter = () => {
        if (hideTimeout.current) clearTimeout(hideTimeout.current);
        setShowCategorySheet(true);
    };

    const handleMouseLeave = () => {
        hideTimeout.current = setTimeout(() => setShowCategorySheet(false), 300);
    };

    const handleSearchSubmit = (e?: React.FormEvent) => {
        if (e) e.preventDefault();
        const trimmed = searchQuery.trim();
        if (trimmed) {
            navigate(`/search?q=${encodeURIComponent(trimmed)}`);
            setSearchOpen(false);
            setSearchQuery("");
        }
    };
    const isCustomer = !isSeller && !isAdmin;

    return (
        <Box className="sticky top-0 z-50 bg-white border-b-2 border-blue-600">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="relative flex items-center justify-between h-16">

                    {/* Conditionally rendered inline search row overlay */}
                    {searchOpen ? (
                        <form onSubmit={handleSearchSubmit} className="flex items-center w-full gap-3 h-full animate-fadeIn">
                            <IconButton onClick={handleSearchSubmit} type="button">
                                <SearchIcon className="text-blue-700" />
                            </IconButton>
                            <InputBase
                                placeholder="Search for products, brands and categories..."
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                                autoFocus
                                fullWidth
                                className="text-gray-800 font-medium"
                                sx={{ fontSize: '1rem' }}
                            />
                            <IconButton onClick={() => { setSearchOpen(false); setSearchQuery(""); }}>
                                <CloseIcon />
                            </IconButton>
                        </form>
                    ) : (
                        <>
                            {/* Standard Navbar Content */}
                            <div className="flex items-center">
                                {!isLarge && (
                                    <IconButton onClick={() => setMobileMenuOpen(true)}>
                                        <MenuIcon />
                                    </IconButton>
                                )}
                                <Logo />
                                {isLarge && (
                                    <ul className="flex items-center text-blue-700 gap-5 ml-6">
                                        {mainCategory.map((item) => (
                                            <li
                                                key={item.categoryId}
                                                className="hover:border-b-2 border-blue-700 transition-all cursor-pointer font-medium"
                                                onMouseLeave={handleMouseLeave}
                                                onMouseEnter={() => {
                                                    handleMouseEnter();
                                                    setSelectedCategory(item.categoryId as CategoryKey);
                                                }}
                                            >
                                                {item.name}
                                            </li>
                                        ))}
                                    </ul>
                                )}
                            </div>

                            <div className="flex items-center gap-2">
                                <IconButton onClick={() => setSearchOpen(true)}>
                                    <SearchIcon />
                                </IconButton>
                                {auth.isAuthenticated ? (
                                    <>
                                        <IconButton component={Link} to={isAdmin ? "/admin" : isSeller ? "/seller/profile" : "/account/orders"}>
                                            <Avatar src={`https://api.dicebear.com/9.x/shapes/svg?seed=${auth.jwt?.substring(0, 20) || 'guest'}`} sx={{ width: 35, height: 35 }} />
                                        </IconButton>
                                        {isLarge && <span className="text-blue-700 font-semibold">{auth.user?.userName || "Account"}</span>}
                                    </>
                                ) : (
                                    <Button onClick={() => navigate("/login")} component={Link} to="/login" variant="contained" className="bg-blue-700 normal-case px-5">Login</Button>
                                )}
                                {isCustomer && (
                                    <>
                                        <IconButton onClick={() => auth.isAuthenticated ? navigate("/wishlist") : navigate("/login")}><FavoriteBorder className="text-blue-700" /></IconButton>
                                        <IconButton onClick={() => auth.isAuthenticated ? navigate("/cart") : navigate("/login")}><AddShoppingCart className="text-blue-700" /></IconButton>
                                        {isSmall && (
                                            <Button component={Link} to="/become-seller" startIcon={<Storefront />} variant="outlined">Become Seller</Button>
                                        )}
                                    </>
                                )}
                            </div>
                        </>
                    )}

                    {showCategorySheet && !searchOpen && createPortal(
                        <div
                            onMouseLeave={handleMouseLeave}
                            onMouseEnter={handleMouseEnter}
                            className="fixed top-16 left-0 right-0 border bg-white shadow-xl"
                            style={{ zIndex: 50 }}
                        >
                            <CategorySheet selectedCategory={selectedCategory} setShowSheet={setShowCategorySheet} />
                        </div>,
                        document.body
                    )}
                </div>
            </div>

            {/* Drawer Area */}
            <Drawer anchor="left" open={mobileMenuOpen} onClose={() => { setMobileMenuOpen(false); setExpandedCategories(new Set()); setExpandedSubCategories(new Set()); }}>
                <Box sx={{ width: 320 }}>
                    <List>
                        {mainCategory.map((item: MainCategory) => {
                            const subs = item.levelTwoCategory || item.levelTowCategory || [];
                            const hasSubs = subs.length > 0;
                            return (
                                <div key={item.categoryId}>
                                    <ListItemButton onClick={() => hasSubs ? toggleCategory(item.categoryId) : setMobileMenuOpen(false)} component={!hasSubs ? Link : "div"} to={!hasSubs ? `/product/${item.categoryId}` : undefined}>
                                        <ListItemText primary={item.name} />
                                        {hasSubs && (expandedCategories.has(item.categoryId) ? <ExpandLess /> : <ExpandMore />)}
                                    </ListItemButton>
                                    <Collapse in={expandedCategories.has(item.categoryId)} unmountOnExit>
                                        <List disablePadding>
                                            {subs.map((sub: SubCategory) => {
                                                const level3 = getLevelThreeCategories(sub.categoryId, item.categoryId);
                                                const hasL3 = level3.length > 0;
                                                return (
                                                    <div key={sub.categoryId}>
                                                        <ListItemButton sx={{ pl: 4 }} onClick={() => hasL3 ? toggleSubCategory(sub.categoryId) : setMobileMenuOpen(false)} component={!hasL3 ? Link : "div"} to={!hasL3 ? `/product/${sub.categoryId}` : undefined}>
                                                            <ListItemText primary={sub.name} sx={{ '& .MuiTypography-root': { fontSize: '0.875rem' } }} />
                                                            {hasL3 && (expandedSubCategories.has(sub.categoryId) ? <ExpandLess /> : <ExpandMore />)}
                                                        </ListItemButton>
                                                        <Collapse in={expandedSubCategories.has(sub.categoryId)} unmountOnExit>
                                                            <Box sx={{ pl: 6, pr: 2, py: 1 }}>
                                                                <Grid container spacing={1}>
                                                                    {level3.map((l3) => (
                                                                        <Grid item xs={6} key={l3.categoryId}>
                                                                            <ListItemButton component={Link} to={`/product/${l3.categoryId}`} onClick={() => setMobileMenuOpen(false)}>
                                                                                <ListItemText primary={l3.name} sx={{ '& .MuiTypography-root': { fontSize: '0.75rem' } }} />
                                                                            </ListItemButton>
                                                                        </Grid>
                                                                    ))}
                                                                </Grid>
                                                            </Box>
                                                        </Collapse>
                                                    </div>
                                                );
                                            })}
                                        </List>
                                    </Collapse>
                                </div>
                            );
                        })}
                    </List>
                </Box>
            </Drawer>
        </Box>
    );
};
export default Navbar;