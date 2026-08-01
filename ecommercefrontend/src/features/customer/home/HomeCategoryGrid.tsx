import { useAppSelector } from "../../../state/hooks.ts";

const PLACEHOLDER_IMAGES = [
    "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=600&q=80", // Fashion
    "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600&q=80", // Electronics
    "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80", // Headphones
    "https://images.unsplash.com/photo-1512436991641-6745cdb1723f?w=600&q=80", // Clothing
    "https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=600&q=80", // Shopping
    "https://images.unsplash.com/photo-1483985988355-763728e1935b?w=600&q=80", // Lifestyle
];

const HomeCategoryGrid = () => {
    const { home } = useAppSelector((store) => store);

    console.log("HomeCategoryGrid - homePageData:", home.homePageData);
    console.log("HomeCategoryGrid - grid data:", home.homePageData?.grid);
    console.log("HomeCategoryGrid - loading:", home.loading);
    console.log("HomeCategoryGrid - error:", home.error);

    const displayData =
        home.homePageData?.grid && home.homePageData.grid.length > 0
            ? home.homePageData.grid
            : Array.from({ length: 6 }, (_, index) => ({
                id: `placeholder-${index}`,
                imageUrl: PLACEHOLDER_IMAGES[index],
                categoryId: "CATEGORY_NAME",
                name: "NAME",
                section: "GRID",
            }));

    console.log("HomeCategoryGrid - final displayData:", displayData);

    return (
        <div className="hidden md:grid grid-cols-4 grid-rows-2 gap-4 px-20 h-200 py-10">
            {displayData.map((category, index) => {
                const isFirst = index === 0;
                const isThird = index === 2;
                const spanClass = isFirst || isThird ? "row-span-2" : "row-span-1";

                return (
                    <div key={category.id} className={`col-span-1 ${spanClass}`}>
                        <img 
                            className="w-full h-full object-cover rounded-lg overflow-hidden" 
                            src={category.imageUrl} 
                            alt={category.name || "Category"} 
                        />
                    </div>
                );
            })}
        </div>
    );
};

export default HomeCategoryGrid;
