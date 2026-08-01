import ShopByCategoryCard from "./ShopByCategoryCard.tsx";
import { useAppSelector } from "../../../state/hooks.ts";
const ShopByCategory = () => {
    const { home } = useAppSelector((store) => store);

    console.log("ShopByCategory - homePageData:", home.homePageData);
    console.log("ShopByCategory - shopByCategories:", home.homePageData?.shopByCategories);
    console.log("ShopByCategory - loading:", home.loading);
    console.log("ShopByCategory - error:", home.error);

    if (home.loading) {
        return <div className="px-4 sm:px-6 py-6">Loading categories...</div>;
    }

    if (home.error) {
        return (
            <div className="px-4 sm:px-6 py-16 flex flex-col items-center justify-center text-center gap-3">
                <div className="w-14 h-14 rounded-full bg-gray-100 flex items-center justify-center">
                    <svg
                        className="w-7 h-7 text-gray-400"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                    >
                        <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth={1.5}
                            d="M3.75 6.75h16.5M3.75 12h16.5M3.75 17.25h16.5"
                        />
                    </svg>
                </div>
                <p className="text-gray-700 font-medium">
                    Categories coming soon
                </p>
                <p className="text-gray-400 text-sm max-w-xs">
                    We're setting things up — check back shortly for new categories to shop by.
                </p>
            </div>
        );
    }

    if (
        !home.homePageData?.shopByCategories ||
        home.homePageData.shopByCategories.length === 0
    ) {
        return (
            <div className="px-4 sm:px-6 py-16 flex flex-col items-center justify-center text-center gap-3">
                <div className="w-14 h-14 rounded-full bg-gray-100 flex items-center justify-center">
                    <svg
                        className="w-7 h-7 text-gray-400"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                    >
                        <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth={1.5}
                            d="M3.75 6.75h16.5M3.75 12h16.5M3.75 17.25h16.5"
                        />
                    </svg>
                </div>
                <p className="text-gray-700 font-medium">
                    Categories coming soon
                </p>
                <p className="text-gray-400 text-sm max-w-xs">
                    We're setting things up — check back shortly for new categories to shop by.
                </p>
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