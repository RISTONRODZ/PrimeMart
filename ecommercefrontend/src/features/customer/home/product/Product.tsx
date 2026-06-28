import FilterSection from "./FilterSection";
import ProductCard from "./ProductCard";
import {
    Box,
    Drawer,
    FormControl,
    IconButton,
    InputLabel,
    MenuItem,
    Pagination,
    Select,
    useMediaQuery,
    useTheme,
    type SelectChangeEvent
} from "@mui/material";
import { FilterAlt } from "@mui/icons-material";
import { useState } from "react";

const Product = () => {
    const theme = useTheme();
    const isLarge = useMediaQuery(theme.breakpoints.up("lg"));

    const [sort, setSort] = useState("");
    const [openFilter, setOpenFilter] = useState(false);
    const [pageChange, setPageChange] = useState(1);

    const handleSortChange = (event: SelectChangeEvent) => {
        setSort(event.target.value);
    };

    function handlePageChange(value: number) {
        setPageChange(value);
    }

    return (
        <div className="mt-10">
            <h1 className="text-3xl text-center font-bold text-gray-700 pb-5 px-9">
                Women Sarees
            </h1>

            <div className="lg:flex">
                <section className="hidden lg:block w-[20%]">
                    <FilterSection />
                </section>

                <div className="w-full lg:w-[80%]">
                    <div className="flex justify-between items-center px-9 mb-8">
                        {!isLarge && (
                            <>
                                <IconButton onClick={() => setOpenFilter(true)}>
                                    <FilterAlt />
                                </IconButton>

                                <Drawer
                                    anchor="left"
                                    open={openFilter}
                                    onClose={() => setOpenFilter(false)}
                                >
                                    <Box sx={{ width: 300, p: 2 }}>
                                        <FilterSection />
                                    </Box>
                                </Drawer>
                            </>
                        )}

                        <FormControl size="small" sx={{ width: "200px" }}>
                            <InputLabel>Sort</InputLabel>
                            <Select
                                value={sort}
                                label="Sort"
                                onChange={handleSortChange}
                            >
                                <MenuItem value="price_low">
                                    Price low to high
                                </MenuItem>
                                <MenuItem value="price_high">
                                    Price high to low
                                </MenuItem>
                            </Select>
                        </FormControl>
                    </div>

                    <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8 px-9">
                        {[...Array(9)].map((_, index) => (
                            <ProductCard key={index} />
                        ))}
                    </section>

                    <div className="flex justify-center mt-10 mb-10">
                        <Pagination
                            count={10}
                            color="primary"
                            page={pageChange}
                            onChange={(_, value) => handlePageChange(value)}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Product;