import HomeCategoryTable from "./HomeCategoryTable.tsx";
import {useAppSelector} from "../../state/hooks.ts";

const DealCategoryTable = () => {
    const {home} = useAppSelector(store => store);
    return (
        <div>
           <HomeCategoryTable data={home.homePageData?.dealCategories || []}/>
        </div>
    );
};

export default DealCategoryTable;