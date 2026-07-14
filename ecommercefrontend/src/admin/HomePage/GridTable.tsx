import { useEffect } from 'react';
import HomeCategoryTable from "./HomeCategoryTable.tsx";
import { useAppDispatch, useAppSelector } from "../../state/hooks.ts";
import { fetchHomePageData } from "../../state/customer/CustomerSlice.ts";

const GridTable = () => {
    const dispatch = useAppDispatch();
    const { home } = useAppSelector(store => store);

    useEffect(() => {
        dispatch(fetchHomePageData());
    }, [dispatch]);

    return (
        <div>
            <HomeCategoryTable data={home.homePageData?.grid || []}/>
        </div>
    );
};

export default GridTable;