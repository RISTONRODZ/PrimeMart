import {Avatar, Box, Button, IconButton, useMediaQuery, useTheme, Drawer, List, ListItemButton, ListItemText, Collapse, Grid} from "@mui/material";
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import MenuIcon from '@mui/icons-material/Menu'
import SearchIcon from '@mui/icons-material/Search';
import {AddShoppingCart, FavoriteBorder, Storefront} from "@mui/icons-material";
import {Logo} from "./Logo.tsx";
import CategorySheet, {type CategoryKey} from "../../features/customer/home/categories/CategorySheet.tsx";
import {mainCategory, type MainCategory, type SubCategory} from "../../features/customer/data/category/mainCategory.ts";
import {menLevelThree} from "../../features/customer/data/category/level three/menLevelThree.ts";
import {womenLevelThree} from "../../features/customer/data/category/level three/womenLevelThree.ts";
import {furnitureLevelThree} from "../../features/customer/data/category/level three/furnitureLevelThree.ts";
import {electronicsLevelThree} from "../../features/customer/data/category/level three/electronicsLevelThree.ts";
import {useRef, useState} from "react";
import {Link} from "react-router-dom";

const Navbar = () => {
    const theme = useTheme();
    const isLarge = useMediaQuery(theme.breakpoints.up("md"));
    const isSmall = useMediaQuery(theme.breakpoints.up("sm"));
    const [selectedCategory, setSelectedCategory] = useState<CategoryKey>("men");
    const [showCategorySheet, setShowCategorySheet] = useState(false);
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
    const [expandedCategories, setExpandedCategories] = useState<Set<string>>(new Set());
    const [expandedSubCategories, setExpandedSubCategories] = useState<Set<string>>(new Set());
    const hideTimeout = useRef<ReturnType<typeof setTimeout> | null>(null);

    const toggleCategory = (categoryId: string) => {
        setExpandedCategories(prev => {
            const newSet = new Set(prev);
            if (newSet.has(categoryId)) {
                newSet.delete(categoryId);
            } else {
                newSet.add(categoryId);
            }
            return newSet;
        });
    };

    const toggleSubCategory = (subCategoryId: string) => {
        setExpandedSubCategories(prev => {
            const newSet = new Set(prev);
            if (newSet.has(subCategoryId)) {
                newSet.delete(subCategoryId);
            } else {
                newSet.add(subCategoryId);
            }
            return newSet;
        });
    };

    const getLevelThreeCategories = (parentCategoryId: string, mainCategoryId: string) => {
        let levelThreeData: any[] = [];
        if (mainCategoryId === "men") {
            levelThreeData = menLevelThree.filter(item => item.parentCategoryId === parentCategoryId);
        } else if (mainCategoryId === "women") {
            levelThreeData = womenLevelThree.filter(item => item.parentCategoryId === parentCategoryId);
        } else if (mainCategoryId === "home_furniture") {
            levelThreeData = furnitureLevelThree.filter(item => item.parentCategoryId === parentCategoryId);
        } else if (mainCategoryId === "electronics") {
            levelThreeData = electronicsLevelThree.filter(item => item.parentCategoryId === parentCategoryId);
        }
        return levelThreeData;
    };
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
                        {!isLarge && <IconButton onClick={() => setMobileMenuOpen(true)}>
                            <MenuIcon/>
                        </IconButton>}
                        <Logo/>
                        <ul className={'flex items-center text-blue-700 gap-5 pl-2 '}>
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
                                        <Avatar src="https://i.pinimg.com/736x/9e/c0/f8/9ec0f877571edc437f89c15c08081533.jpg"/>
                                    </IconButton>
                                    {isLarge && (
                                        <span className="text-blue-700 font-semibold">Riston</span>
                                    )}
                                </>
                            ) : (
                                <Button component={Link} to="/login" className="bg-blue-700! hover:bg-blue-800! text-white! normal-case! px-5">
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
                                <Button component={Link} to="/become-seller" startIcon={<Storefront className="text-blue-700"/>} variant="outlined">
                                    Become Seller
                                </Button>
                            )}
                        </div>
                        {showCategorySheet && <div
                            onMouseLeave={handleMouseLeave}
                            onMouseEnter={handleMouseEnter}
                            className={'categorySheet absolute top-[3.3rem] left-20 right-20 border bg-slate-500 z-50'}>
                            <CategorySheet selectedCategory={selectedCategory} setShowSheet={setShowCategorySheet}/>
                        </div>}
                    </div>
                </div>
            </div>
        </Box>
        <Drawer
            anchor="left"
            open={mobileMenuOpen}
            onClose={() => setMobileMenuOpen(false)}
        >
            <Box sx={{ width: 320 }} role="presentation">
                <List>
                    {mainCategory.map((item: MainCategory) => {
                        const subcategories: SubCategory[] = item.levelTwoCategory || item.levelTowCategory || [];
                        const hasSubcategories = subcategories.length > 0;
                        const isExpanded = expandedCategories.has(item.categoryId);

                        return (
                            <div key={item.categoryId}>
                                <ListItemButton
                                    onClick={() => {
                                        if (hasSubcategories) {
                                            toggleCategory(item.categoryId);
                                        } else {
                                            setMobileMenuOpen(false);
                                        }
                                    }}
                                    component={!hasSubcategories ? Link : "div"}
                                    to={!hasSubcategories ? `/product/${item.categoryId}` : undefined}
                                >
                                    <ListItemText primary={item.name} />
                                    {hasSubcategories && (isExpanded ? <ExpandLess /> : <ExpandMore />)}
                                </ListItemButton>
                                {hasSubcategories && (
                                    <Collapse in={isExpanded} timeout="auto" unmountOnExit>
                                        <List component="div" disablePadding>
                                            {subcategories.map((sub: SubCategory) => {
                                                const levelThreeItems = getLevelThreeCategories(sub.categoryId, item.categoryId);
                                                const hasLevelThree = levelThreeItems.length > 0;
                                                const isSubExpanded = expandedSubCategories.has(sub.categoryId);

                                                return (
                                                    <div key={sub.categoryId}>
                                                        <ListItemButton
                                                            sx={{ pl: 4 }}
                                                            onClick={() => {
                                                                if (hasLevelThree) {
                                                                    toggleSubCategory(sub.categoryId);
                                                                } else {
                                                                    setMobileMenuOpen(false);
                                                                }
                                                            }}
                                                            component={!hasLevelThree ? Link : "div"}
                                                            to={!hasLevelThree ? `/product/${sub.categoryId}` : undefined}
                                                        >
                                                            <ListItemText primary={sub.name} sx={{ '& .MuiTypography-root': { fontSize: '0.875rem', color: 'text.secondary' } }} />
                                                            {hasLevelThree && (isSubExpanded ? <ExpandLess /> : <ExpandMore />)}
                                                        </ListItemButton>
                                                        {hasLevelThree && (
                                                            <Collapse in={isSubExpanded} timeout="auto" unmountOnExit>
                                                                <Box sx={{ pl: 6, pr: 2, py: 1 }}>
                                                                    <Grid container spacing={1}>
                                                                        {levelThreeItems.map((levelThreeItem) => (
                                                                            <Grid item xs={6} key={levelThreeItem.categoryId}>
                                                                                <ListItemButton
                                                                                    component={Link}
                                                                                    to={`/product/${levelThreeItem.categoryId}`}
                                                                                    onClick={() => setMobileMenuOpen(false)}
                                                                                    sx={{ py: 0.5, px: 1, borderRadius: 1, '&:hover': { backgroundColor: 'rgba(0, 0, 0, 0.04)' } }}
                                                                                >
                                                                                    <ListItemText primary={levelThreeItem.name} sx={{ '& .MuiTypography-root': { fontSize: '0.75rem', color: 'text.secondary', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' } }} />
                                                                                </ListItemButton>
                                                                            </Grid>
                                                                        ))}
                                                                    </Grid>
                                                                </Box>
                                                            </Collapse>
                                                        )}
                                                    </div>
                                                );
                                            })}
                                        </List>
                                    </Collapse>
                                )}
                            </div>
                        );
                    })}
                </List>
            </Box>
        </Drawer>
    </>);
};

export default Navbar;