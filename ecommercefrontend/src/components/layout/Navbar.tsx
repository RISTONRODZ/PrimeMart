import {Avatar, Box, Button, IconButton, useMediaQuery, useTheme} from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu'
import SearchIcon from '@mui/icons-material/Search';
import {AddShoppingCart, FavoriteBorder, Storefront} from "@mui/icons-material";
import {Logo} from "./Logo.tsx";

const Navbar = () => {
    const theme = useTheme();
    const isLarge = useMediaQuery(theme.breakpoints.up("md"));
    const isSmall = useMediaQuery(theme.breakpoints.up("sm"));
    return (
        <>
            <Box className={'border-b-2'}>
                <div className={'mr-3'}>
                    <div className={'flex items-center justify-between'}>
                        <div className={'flex items-center gap-2 ml-2'}>
                            {!isLarge && <IconButton>
                                <MenuIcon/>
                            </IconButton>}
                            <Logo/>
                            <ul className={'flex items-center text-blue-700 gap-5 pl-2 '}>
                                {isLarge &&
                                   ( [
                                        "Men",
                                        "Women",
                                        "Home & Furniture",
                                        "Electronics"
                                    ].map((item) => <li className={'hover:border-b-2 transition-all ease-in duration-100 hover:text-blue-900 font-medium cursor-pointer'}>{item}</li>)
                                )}
                            </ul>
                        </div>

                        <div className={'flex gap-2'}>
                            <IconButton>
                                <SearchIcon/>
                            </IconButton>
                            {
                                <div className={'flex items-center font-semibold gap-0.5'}>

                                    <IconButton>
                                        <Avatar src={'https://www.flaticon.com/free-icon/man_2202112'}/>
                                    </IconButton>
                                    <h1>
                                        {isLarge && <h1>Riston</h1>}
                                    </h1>

                                    <IconButton>
                                        <FavoriteBorder className={'text-blue-700'}/>
                                    </IconButton>
                                    <IconButton>
                                        <AddShoppingCart className={'text-blue-700'}/>
                                    </IconButton>
                                    {isSmall &&
                                        <Button startIcon={<Storefront className={'text-blue-700'}/>}
                                                variant={'outlined'}>
                                            Become Seller
                                        </Button>}

                                </div>
                            }
                        </div>
                    </div>
                </div>
            </Box>
        </>
    );
};

export default Navbar;
