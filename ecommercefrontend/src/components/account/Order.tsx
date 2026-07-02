import OrderItem from "./OrderItem.tsx";

const Order = () => {
    return (
        <div className={'text-sm min-h-screen'}>
            <div className={'py-5 text-lg'}>
                <h1 className={'font-semibold'}>All Orders</h1>
                <p>From anytime</p>
            </div>
            <div className={'space-y-2'}>
                {
                    Array.from({length: 5}).map((_item, index) => (
                        <div key={index}>
                            <OrderItem/>
                        </div>
                    ))
                }
            </div>
        </div>
    );
};

export default Order;