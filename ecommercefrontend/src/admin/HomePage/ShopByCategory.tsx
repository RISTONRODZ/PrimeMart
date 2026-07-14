import HomeCategoryTable from "./HomeCategoryTable.tsx";
import {useAppSelector} from "../../state/hooks.ts";

const ShopByCategory = () => {
    const {home} = useAppSelector(store=>store);
    return (
        <div>
           <HomeCategoryTable data={home.homePageData?.shopByCategories || []} section="SHOP_BY_CATEGORIES"/>
        </div>
    );
};

export default ShopByCategory;