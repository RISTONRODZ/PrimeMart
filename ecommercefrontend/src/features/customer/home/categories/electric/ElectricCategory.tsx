import ElectricCategoryCard from "./ElectricCategoryCard.tsx";

const ElectricCategory = () => {
    return (
        <div className="py-10 lg:px-20 border-b">
            {/* flex-nowrap: prevents cards from wrapping on small screens
              overflow-x-auto: enables horizontal scrollbar when content overflows
              gap-4: adds spacing between cards
              lg:flex-wrap: allows wrapping on large screens
              lg:justify-between: justifies spacing on large screens
            */}
            <div className="flex flex-nowrap justify-center lg:justify-between overflow-x-auto gap-4 px-4 pb-4 lg:flex-wrap lg:overflow-visible lg:px-0 lg:pb-0 scrollbar-none">
                {[1, 1, 1, 1, 1, 1, 1].map((_, index) => (
                    <div key={index} className="shrink-0">
                        <ElectricCategoryCard />
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ElectricCategory;
