import ElectricCategoryCard from "./ElectricCategoryCard.tsx";
import {useAppSelector} from "../../../../../state/hooks.ts";
const ElectricCategory = () => {
    const {home} = useAppSelector((store) => store)
    console.log("home categories ",home.homeCategories)
    console.log("home page data ",home.homePageData)
    console.log("electric categories ",home.homePageData?.electricCategories)
    console.log(home)
    return (
        <div className="py-10 lg:px-20 border-b-2 border-slate-400">
            <div className="flex flex-nowrap justify-start lg:justify-between overflow-x-auto gap-4 px-4 pb-4 lg:flex-wrap lg:overflow-visible lg:px-0 lg:pb-0 scrollbar-none">
                {home.homePageData?.electricCategories.map((category) => (
                    <div key={category.id} className="shrink-0 text-slate-600">
                        <ElectricCategoryCard category={category} />
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ElectricCategory;
