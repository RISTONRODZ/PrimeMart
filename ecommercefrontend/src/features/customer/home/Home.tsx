
import ElectricCategory from "./categories/electric/ElectricCategory.tsx";
import HomeCategoryGrid from "./HomeCategoryGrid.tsx";
import Deal from "./deal/Deal.tsx";
import ShopByCategory from "./ShopByCategory.tsx";
import BecomeASeller from "./BecomeASeller.tsx";

const Home = () => {
    return (
        <div className={'lg:space-y-10'}>
           <ElectricCategory/>
            <HomeCategoryGrid/>
            <Deal/>
            <ShopByCategory/>
            <BecomeASeller/>
        </div>

    );
};

export default Home;
