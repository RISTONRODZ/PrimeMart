import { useCallback, useEffect } from "react";
import { Pagination } from "@mui/material";
import { useSearchParams } from "react-router-dom";
import ProductCard from "./ProductCard.tsx";
import FilterSection from "./FilterSection.tsx";
import { useAppDispatch, useAppSelector } from "../../../../state/hooks.ts";
import { searchProduct } from "../../../../state/customer/ProductSlice.ts";

const SearchResults = () => {
    const [searchParams, setSearchParams] = useSearchParams();
    const query = searchParams.get("q") || "";
    const pageChange = Number(searchParams.get("page") || "1") || 1;
    const pageSize = 12;
    const dispatch = useAppDispatch();
    const { searchProducts: results, searchTotalPages, loading, error } = useAppSelector((state) => state.product);

    const fetchResults = useCallback((page: number) => {
        if (!query) return;
        dispatch(searchProduct({
            query,
            color: searchParams.get("color") || undefined,
            pageNumber: page - 1,
            pageSize,
        }));
    }, [dispatch, query, searchParams, pageSize]);

    useEffect(() => {
        fetchResults(pageChange);
    }, [fetchResults, pageChange]);

    const handlePageChange = (value: number) => {
        const nextParams = new URLSearchParams(searchParams);
        nextParams.set("page", String(value));
        setSearchParams(nextParams);
        fetchResults(value);
    };

    return (
        <div className="mt-10">
            <h1 className="text-3xl text-center font-bold text-gray-700 pb-8 px-9">
                {query ? `Search results for "${query}"` : "Search"}
            </h1>

            <div className="lg:flex">
                <section className="hidden lg:block w-[20%]">
                    <FilterSection />
                </section>

                <div className="w-full lg:w-[80%]">
                    {loading && <p className="text-center text-gray-500 py-10">Searching...</p>}
                    {!loading && error && <p className="text-center text-red-600 py-10">{error}</p>}
                    {!loading && !error && query && results.length === 0 && (
                        <p className="text-center text-gray-500 py-10">No products found matching "{query}".</p>
                    )}

                    {!loading && !error && results.length > 0 && (
                        <>
                            <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8 px-9">
                                {results.map((product) => (
                                    <ProductCard key={product.id} product={product} />
                                ))}
                            </section>
                            <div className="flex justify-center mt-10 mb-10">
                                <Pagination
                                    count={searchTotalPages}
                                    color="primary"
                                    page={pageChange}
                                    onChange={(_, value) => handlePageChange(value)}
                                />
                            </div>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
};

export default SearchResults;