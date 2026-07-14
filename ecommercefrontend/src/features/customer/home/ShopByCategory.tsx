import ShopByCategoryCard from "./ShopByCategoryCard.tsx";
import { useAppSelector } from "../../../state/hooks.ts";

const ShopByCategory = () => {
    const { home } = useAppSelector((store) => store);

    if (home.loading) {
        return <div className="px-4 sm:px-6 py-6">Loading categories...</div>;
    }

    if (home.error) {
        return (
            <div className="px-4 sm:px-6 py-6 text-red-500">
                Error: {home.error}
            </div>
        );
    }

    if (
        !home.homePageData?.shopByCategories ||
        home.homePageData.shopByCategories.length === 0
    ) {
        return (
            <div className="px-4 sm:px-6 py-6">
                No categories available
            </div>
        );
    }

    return (
        <div className="px-4 sm:px-6 lg:px-8 py-6">
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-4 sm:gap-5 lg:gap-6 auto-rows-fr">
                {home.homePageData.shopByCategories.map((category: any) => (
                    <ShopByCategoryCard
                        key={category.id}
                        item={category}
                    />
                ))}
            </div>
        </div>
    );
};

export default ShopByCategory;