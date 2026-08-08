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

    // Calculate combined totals across all orders
    const combinedTotal = order.orders.reduce((sum, o) => sum + (o.totalSellingPrice || 0), 0);
    const totalItems = order.orders.reduce((sum, o) => sum + (o.totalItem || 0), 0);
    const uniqueSellers = new Set(order.orders.map(o => o.sellerId)).size;

    return (
        <div className={'text-sm min-h-screen'}>
            <div className={'py-5 text-lg'}>
                <h1 className={'font-semibold'}>All Orders</h1>
                <p>From anytime</p>
            </div>

            {/* Combined Order Summary */}
            {order.orders.length > 0 && (
                <div className="bg-linear-to-r from-blue-50 to-indigo-50 border border-blue-100 rounded-lg p-4 mb-4">
                    <div className="flex justify-between items-center">
                        <div>
                            <p className="text-xs text-gray-600">Combined Total ({order.orders.length} order{order.orders.length > 1 ? 's' : ''} from {uniqueSellers} seller{uniqueSellers > 1 ? 's' : ''})</p>
                            <p className="text-2xl font-bold text-gray-900">₹{combinedTotal.toLocaleString('en-IN')}</p>
                        </div>
                        <div className="text-right">
                            <p className="text-xs text-gray-600">Total Items</p>
                            <p className="text-lg font-semibold text-gray-700">{totalItems}</p>
                        </div>
                    </div>
                </div>
            )}

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