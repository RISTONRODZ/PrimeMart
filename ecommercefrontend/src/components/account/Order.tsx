import OrderItem from "./OrderItem.tsx";
import {useAppDispatch, useAppSelector} from "../../state/hooks.ts";
import {useEffect} from "react";
import {fetchUserOrderHistory} from "../../state/customer/OrderSlice.ts";

const Order = () => {
    const dispatch = useAppDispatch();
    const order = useAppSelector(store => store.order);

    useEffect(() => {
        dispatch(fetchUserOrderHistory(localStorage.getItem("jwt") || ""));
    }, [dispatch]);

    return (
        <div className={'text-sm min-h-screen'}>
            <div className={'py-5 text-lg'}>
                <h1 className={'font-semibold'}>All Orders</h1>
                <p>From anytime</p>
            </div>
            <div className={'space-y-2'}>
                {
                    order.orders.map((order) =>
                        order.orderItems.map((item) =>
                            <OrderItem key={item.id} order={order}/>
                        )
                    )
                }
            </div>
        </div>
    );
};

export default Order;