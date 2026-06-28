// import ElectricCategory from "./categories/electric/ElectricCategory.tsx";
// import HomeCategoryGrid from "./HomeCategoryGrid.tsx";
// import Deal from "./deal/Deal.tsx";
// import ShopByCategory from "./ShopByCategory.tsx";
// import BecomeASeller from "./BecomeASeller.tsx";
// import Footer from "../../../components/layout/Footer.tsx";
// import ProductCard from "./product/ProductCard.tsx";
// import Product from "./product/Product.tsx";

import ElectricCategory from "./categories/electric/ElectricCategory.tsx";
import HomeCategoryGrid from "./HomeCategoryGrid.tsx";
import Deal from "./deal/Deal.tsx";
import ShopByCategory from "./ShopByCategory.tsx";
import BecomeASeller from "./BecomeASeller.tsx";
// import Product from "./product/Product.tsx";

const Home = () => {
    return (
        <div className={'lg:space-y-10'}>
           <ElectricCategory/>
            <HomeCategoryGrid/>
            <Deal/>
            <ShopByCategory/>
            <BecomeASeller/>
            {/*<Product/>*/}
        </div>

    );
};

export default Home;
