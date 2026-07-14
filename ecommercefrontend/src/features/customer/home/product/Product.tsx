import FilterSection from "./FilterSection";
import ProductCard from "./ProductCard.tsx";
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
import {useCallback, useEffect, useState} from "react";
import {useAppDispatch, useAppSelector} from "../../../../state/hooks.ts";
import {useSearchParams} from "react-router";
import {getAllProducts} from "../../../../state/customer/ProductSlice.ts";
import {useParams} from "react-router-dom";

const Product = () => {
    const theme = useTheme();
    const isLarge = useMediaQuery(theme.breakpoints.up("lg"));

    const [sort, setSort] = useState("");
    const [openFilter, setOpenFilter] = useState(false);
    const [search, setSearchParams] = useSearchParams();
    const {categoryId} = useParams();
    const pageChange = Number(search.get("page") || "1") || 1;
    const pageSize = 9;
    const dispatch = useAppDispatch();

    const fetchProducts = useCallback((page: number) => {
        let minPrice: number | string | null = null;
        let maxPrice: number | string | null = null;
        const priceValue = search.get("price");
        if (priceValue) {
            if (priceValue === "500") {
                minPrice = 0;
                maxPrice = 500;
            } else if (priceValue === "10000") {
                minPrice = 10000;
                maxPrice = null;
            } else if (priceValue.includes(" - ")) {
                const [min, max] = priceValue.split(" - ");
                minPrice = min;
                maxPrice = max;
            }
        }

        const sortValue = sort === "price_low" ? "price,asc"
                         : sort === "price_high" ? "price,desc"
                         : sort === "discount_high" ? "discountPercent,desc"
                         : sort === "discount_low" ? "discountPercent,asc"
                         : undefined;

        dispatch(getAllProducts({
            categoryId,
            minPrice,
            maxPrice,
            sort: sortValue,
            brand: search.get("brand") || undefined,
            color: search.get("color") || undefined,
            minDiscount: search.get("discount") || undefined,
            pageNumber: page - 1,
            pageSize,
        }));
    }, [categoryId, dispatch, search, sort, pageSize]);

    const handleSortChange = (event: SelectChangeEvent) => {
        const nextParams = new URLSearchParams(search);
        nextParams.set("page", "1");
        setSearchParams(nextParams);
        setSort(event.target.value);
    };
    const {products, totalPages} = useAppSelector((state) => state.product);
    function handlePageChange(value: number) {
        const nextParams = new URLSearchParams(search);
        nextParams.set("page", String(value));
        setSearchParams(nextParams);
        fetchProducts(value);
    }
    useEffect(() => {
        fetchProducts(pageChange);
    }, [fetchProducts, pageChange]);
    return (
        <div className="mt-10">
            <h1 className="text-3xl text-center font-bold text-gray-700 pb-5 px-9">
                {categoryId ? `Products in Category ${categoryId}` : "All Products"}
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
                                <MenuItem value="discount_high">
                                    Discount high to low
                                </MenuItem>
                                <MenuItem value="discount_low">
                                    Discount low to high
                                </MenuItem>
                            </Select>
                        </FormControl>
                    </div>

                    <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8 px-9">
                        {products.map((product) => (
                            <ProductCard key={product.id} product={product} />
                        ))}
                    </section>

                    <div className="flex justify-center mt-10 mb-10">
                        <Pagination
                            count={totalPages}
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