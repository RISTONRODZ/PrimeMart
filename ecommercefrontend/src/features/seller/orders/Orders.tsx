import OrderTable from "./OrderTable.tsx";

const Orders = () => {
    return (
        <div>
            <h1 className={'font-bold mb-4 sm:mb-5 text-lg sm:text-xl'}>All Orders</h1>
           <OrderTable/>
        </div>
    );
};

export default Orders;