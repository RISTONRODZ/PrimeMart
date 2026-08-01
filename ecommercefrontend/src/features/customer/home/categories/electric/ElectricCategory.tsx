import ElectricCategoryCard from "./ElectricCategoryCard.tsx";
import {useAppSelector} from "../../../../../state/hooks.ts";
const ElectricCategory = () => {
    const {home} = useAppSelector((store) => store)

    if (home.loading) {
        return <div className="py-10 lg:px-20 border-b-2 border-slate-400 px-4">Loading categories...</div>;
    }

    if (home.error) {
        return (
            <div className="py-10 lg:px-20 border-b-2 border-slate-400 px-4 text-red-500">
                Error: {home.error}
            </div>
        );
    }

    const categories = home.homePageData?.electricCategories || [];

    if (categories.length === 0) {
        return (
            <div className="py-10 lg:px-20 border-b-2 border-slate-400 flex flex-col items-center justify-center text-center gap-3">
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
        <div className="py-10 lg:px-20 border-b-2 border-slate-400">
            <div className="flex flex-nowrap justify-start lg:justify-between overflow-x-auto gap-4 px-4 pb-4 lg:flex-wrap lg:overflow-visible lg:px-0 lg:pb-0 scrollbar-none">
                {categories.map((category) => (
                    <div key={category.id} className="shrink-0 text-slate-600">
                        <ElectricCategoryCard category={category} />
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ElectricCategory;
