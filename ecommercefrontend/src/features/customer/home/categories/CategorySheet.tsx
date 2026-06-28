import { menLevelTwo } from "../../data/category/level two/menLevelTwo.ts";
import { womenLevelTwo } from "../../data/category/level two/womenLevelTwo.ts";
import { menLevelThree } from "../../data/category/level three/menLevelThree.ts";
import { furnitureLevelThree } from "../../data/category/level three/furnitureLevelThree.ts";
import { womenLevelThree } from "../../data/category/level three/womenLevelThree.ts";
import { electronicsLevelThree } from "../../data/category/level three/electronicsLevelThree.ts";
import { Box } from "@mui/material";
import { electronicsLevelTwo } from "../../data/category/level two/electronicsLavelTwo.ts";
import { furnitureLevelTwo } from "../../data/category/level two/furnitureLevleTwo.ts";

export type CategoryKey = "men" | "women" | "electronics" | "home_furniture";

interface CategoryItem {
    name: string;
    categoryId: string;
    parentCategoryId: string;
    parentCategoryName?: string;
    level?: number;
}

const categoryThree: Record<CategoryKey, CategoryItem[]> = {
    men: menLevelThree,
    women: womenLevelThree,
    electronics: electronicsLevelThree,
    home_furniture: furnitureLevelThree,
};

const categoryTwo: Record<CategoryKey, CategoryItem[]> = {
    men: menLevelTwo,
    women: womenLevelTwo,
    electronics: electronicsLevelTwo,
    home_furniture: furnitureLevelTwo,
};

interface CategorySheetProps {
    selectedCategory: CategoryKey;
    setShowSheet?: (show: boolean) => void;
}

const CategorySheet = ({ selectedCategory }: CategorySheetProps) => {

    const childCategory = (category: CategoryItem[], parentCategoryId: string) => {
        return category.filter((child) => child.parentCategoryId === parentCategoryId);
    };

    return (
        <Box
            sx={{ zIndex: 50 }}
            className={'absolute left-0 right-0 bg-white shadow-lg overflow-y-auto max-h-[70vh] lg:max-h-125'}
        >
            <div className={'flex flex-col sm:flex-row sm:flex-wrap gap-6 px-4 text-sm'}>
                {categoryTwo[selectedCategory]?.map((item) => (
                    <div key={item.categoryId} className="mb-4 min-w-35">
                        <h2 className="text-blue-700 mb-2 font-semibold pl-2 pt-1">
                            {item.name}
                        </h2>
                        <ul className="space-y-3">
                            {childCategory(categoryThree[selectedCategory] || [], item.categoryId).map((child) => (
                                <li
                                    key={child.categoryId}
                                    className="text-slate-700 hover:text-blue-800 cursor-pointer pl-2 text-sm hover:underline hover:underline-offset-4"
                                >
                                    {child.name}
                                </li>
                            ))}
                        </ul>
                    </div>
                ))}
            </div>
        </Box>
    );
};

export default CategorySheet;