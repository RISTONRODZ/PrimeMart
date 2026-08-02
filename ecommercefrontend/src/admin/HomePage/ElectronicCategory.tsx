import HomeCategoryTable from "./HomeCategoryTable.tsx";
import {useAppSelector} from "../../state/hooks.ts";

const ElectronicCategory = () => {
    const {home} = useAppSelector(store => store);
    return (
        <div>
           <HomeCategoryTable data={home.homePageData?.electricCategories || []} section="ELECTRIC"/>
        </div>
    );
};

export default ElectronicCategory;