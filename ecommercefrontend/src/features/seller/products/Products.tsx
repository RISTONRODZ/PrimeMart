import ProductTable from "./ProductTable.tsx";

const Products = () => {
    return (
        <div>
            <h1 className={'font-bold mb-4 sm:mb-5 text-lg sm:text-xl'}>All Products</h1>
           <ProductTable/>
        </div>
    );
};

export default Products;