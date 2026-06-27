import ShopByCategoryCard from "./ShopByCategoryCard.tsx";

const ShopByCategory = () => {
    return (
        <div className={'flex px-5 flex-wrap'}>
            {
                <ShopByCategoryCard/>
           }
        </div>

    );
};

export default ShopByCategory;
