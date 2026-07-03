import {Box, Button, Divider} from "@mui/material";
import {useNavigate} from "react-router-dom";
import OrderStepper from "./OrderStepper.tsx";

const OrderDetails = () => {
    const navigate = useNavigate();
    return (
        <Box className='space-y-4 sm:space-y-5 '>
            <section className='flex flex-col gap-4 sm:gap-5 justify-center items-center border rounded-md p-4 sm:p-5'>
                <img className='w-20 sm:w-25' src={"https://i.pinimg.com/736x/bb/e3/26/bbe326016a38ade9366cf6464fb4a571.jpg"}
                     alt=""/>
                <div className='text-xs sm:text-sm space-y-1 text-center'>
                    <h1 className='font-bold text-sm sm:text-base'>{"Virani Clothing"}</h1>
                    <p className='text-xs sm:text-sm'>Cellecor RAY 1.43" AMOLED Display | 700 NITS | AOD | BT-Calling | AI Voice | Split Screen
                        Smartwatch (Black Strap, Free Size)</p>
                    <p className='text-xs sm:text-sm'><strong>Size:</strong>M</p>
                </div>
                <div>
                    <Button onClick={() => navigate(`/reviews/${5}/create`)} size={window.innerWidth < 640 ? "small" : "medium"}>Write Review</Button>
                </div>
            </section>
            <section className={'border rounded-md p-4 sm:p-5'}>
                <OrderStepper orderStatus={"ARRIVING"}/>
            </section>
            <section className={'border rounded-md p-4 sm:p-5'}>
                <h1 className={'font-bold pb-2 text-sm sm:text-base'}>Delivery Address</h1>
                <div className={'flex flex-col sm:flex-row'}>
                    <p className={'mr-0 sm:mr-3 mb-2 sm:mb-0'}>Riston</p>
                    <Divider orientation={window.innerWidth < 640 ? "horizontal" : "vertical"} flexItem className={window.innerWidth < 640 ? "my-2" : "mx-3"}/>
                    <p className={'ml-0 sm:ml-3'}>6577485676</p>
                </div>
            </section>
            <section className={'border rounded-md p-4 sm:p-5'}>
                <div className="order-summary-card">
                    <div className="price-info">
                        <span className="label pr-3 text-sm sm:text-base">Total Item Price</span>
                        <span className="price text-sm sm:text-base">₹ 799.00</span>
                    </div>
                    <div className="savings text-sm sm:text-base">You saved <span className={'text-blue-500 text-base sm:text-lg'}>₹699.00</span> on this item</div>

                    <div className="payment-method text-sm sm:text-base">
                        <span className="icon">💳</span> Online payment
                    </div>

                    <div className="seller-info text-sm sm:text-base">
                        Sold by : <strong>Virani Clothing</strong>
                    </div>

                    <Button variant={'contained'} color={'error'} className="status-button" fullWidth={window.innerWidth < 640}>Cancel Order</Button>
                </div>
            </section>
        </Box>
    )
}

export default OrderDetails;
