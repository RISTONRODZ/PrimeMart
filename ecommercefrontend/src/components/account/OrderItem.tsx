import { Avatar } from "@mui/material";
import { ElectricBolt, Close } from "@mui/icons-material";
import type { Order } from "../../types/OrderTypes.ts";
import {useNavigate} from "react-router-dom";
import { useAppDispatch } from "../../state/hooks.ts";
import { deleteOrder } from "../../state/customer/OrderSlice.ts";

const OrderItem = ({ order }: { order: Order }) => {
    const formattedDate = new Date(order.deliverDate).toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric',
        year: 'numeric'
    });
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const item = order.orderItems[0];

    const handleDeleteOrder = (e: React.MouseEvent) => {
        e.stopPropagation();
        dispatch(deleteOrder(order.orderId));
    };

    // const canDelete = order.orderStatus === "CANCELED" || order.orderStatus === "DELIVERED";

    return (
        <div onClick={()=> {
            console.log("Order ID:", order.id);
            console.log("Item ID:", item?.id);
            navigate(`/account/order/${order.orderId}/${item?.id}`)
        }} className='text-sm bg-white p-4 sm:p-5 space-y-4 border rounded-md cursor-pointer relative'>
            {/*{canDelete && (*/}
            {/*    <button*/}
            {/*        onClick={handleDeleteOrder}*/}
            {/*        className='absolute top-2 right-2 p-1 hover:bg-gray-100 rounded-full transition-colors'*/}
            {/*        title='Remove from order history'*/}
            {/*    >*/}
            {/*        <Close className='text-gray-500 hover:text-red-500' />*/}
            {/*    </button>*/}
            {/*)}*/}
            <div className='flex items-center gap-3 sm:gap-5'>
                <div>
                    <Avatar sizes='small' sx={{ color: "#2b7fff", color: "#e0ebff" }}>
                        <ElectricBolt />
                    </Avatar>
                </div>
                <div>
                    <h1 className='font-bold text-blue-700 text-sm sm:text-base'>
                        {order.orderStatus}
                    </h1>
                    <p className='text-xs sm:text-sm'>Arriving By {formattedDate}</p>
                </div>
            </div>

            <div className='p-4 sm:p-5 bg-blue-50 flex gap-3 rounded'>
                <div className="shrink-0">
                    <img
                        className="w-16 h-16 sm:w-20 sm:h-20 object-cover rounded"
                        src={item?.productImage || "https://i.pinimg.com/736x/bb/e3/26/bbe326016a38ade9366cf6464fb4a571.jpg"}
                        alt={item?.productTitle}
                    />
                </div>
                <div className="flex-1 min-w-0 space-y-1">
                    <h1 className="font-bold text-sm sm:text-base break-all line-clamp-2">
                        {item?.productTitle || "Product"}
                    </h1>
                    <p className='text-xs sm:text-sm text-gray-600'>{item?.color || 'N/A'}</p>
                    <p className='text-xs sm:text-sm'>
                        <strong>Size: </strong>
                        {item?.size || 'N/A'}
                    </p>
                    <p className='text-sm font-semibold'>
                        ₹{item?.sellingPrice?.toLocaleString()}
                    </p>
                </div>
            </div>
        </div>
    );
};

export default OrderItem;