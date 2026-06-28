import ElectricCategoryCard from "./ElectricCategoryCard.tsx";

const ElectricCategory = () => {
    return (
        <div className="py-10 lg:px-20 border-b-2 border-slate-400">
            <div className="flex flex-nowrap justify-center lg:justify-between overflow-x-auto gap-4 px-4 pb-4 lg:flex-wrap lg:overflow-visible lg:px-0 lg:pb-0 scrollbar-none">
                {[1, 1, 1, 1, 1, 1, 1].map((_, index) => (
                    <div key={index} className="shrink-0 text-slate-600">
                        <ElectricCategoryCard />
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ElectricCategory;
