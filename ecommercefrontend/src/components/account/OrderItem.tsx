import {Avatar} from "@mui/material";
import {ElectricBolt} from "@mui/icons-material";

const OrderItem = () => {
    return (
        <div className='text-sm bg-white p-5 space-y-4 border rounded-md cursor-pointer'>
            <div className='flex items-center gap-5'>
                <div>
                    <Avatar sizes='small' sx={{color: "#2b7fff"}}>
                        <ElectricBolt/>
                    </Avatar>
                </div>
                <div>
                    <h1 className='font-bold text-blue-700 '>PENDING</h1>
                    <p>Arriving By Mon, 15 Jul</p>
                </div>
            </div>

            <div className='p-5 bg-blue-200 flex gap-3 rounded'>
                <div>
                    <img
                        className='w-17.5'
                        src="https://i.pinimg.com/736x/bb/e3/26/bbe326016a38ade9366cf6464fb4a571.jpg"
                        alt=""
                    />
                </div>
                <div className='w-full space-y-2'>
                    <h1 className='font-bold'>Apple</h1>
                    <p>RAY 1.43" AMOLED Display | 700 NITS | AOD | BT-Calling | AI Voice | Split Screen Smartwatch (Black Strap, Free Size)</p>
                    <p>
                        <strong>size : </strong>
                        FREE
                    </p>
                </div>
            </div>
        </div>
    );
};
export default OrderItem;