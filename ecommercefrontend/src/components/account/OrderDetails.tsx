import {Box, Button, Divider} from "@mui/material";
import {useNavigate} from "react-router-dom";
import OrderStepper from "./OrderStepper.tsx";

const OrderDetails = () => {
    const navigate = useNavigate();
    return (
        <Box className='space-y-5 '>
            <section className='flex flex-col gap-5 justify-center items-center border rounded-md p-5'>
                <img className='w-25' src={"https://i.pinimg.com/736x/bb/e3/26/bbe326016a38ade9366cf6464fb4a571.jpg"}
                     alt=""/>
                <div className='text-sm space-y-1 text-center'>
                    <h1 className='font-bold'>{"Virani Clothing"}</h1>
                    <p>Cellecor RAY 1.43" AMOLED Display | 700 NITS | AOD | BT-Calling | AI Voice | Split Screen
                        Smartwatch (Black Strap, Free Size)</p>
                    <p><strong>Size:</strong>M</p>
                </div>
                <div>
                    <Button onClick={() => navigate(`/reviews/${5}/create`)}>Write Review</Button>
                </div>
            </section>
            <section className={'border rounded-md p-5'}>
                <OrderStepper orderStatus={"ARRIVING"}/>
            </section>
            <section className={'border rounded-md p-5'}>
                <h1 className={'font-bold pb-2'}>Delivery Address</h1>
                <div className={'flex'}>
                    <p className={'mr-3'}>Riston</p>
                    <Divider orientation="vertical" flexItem/>
                    <p className={'ml-3'}>6577485676</p>
                </div>
            </section>
            <section className={'border rounded-md p-5'}>
                <div className="order-summary-card">
                    <div className="price-info">
                        <span className="label pr-3">Total Item Price</span>
                        <span className="price ">₹ 799.00</span>
                    </div>
                    <div className="savings">You saved <span className={'text-blue-500 text-lg'}>₹699.00</span> on this item</div>

                    <div className="payment-method">
                        <span className="icon">💳</span> Online payment
                    </div>

                    <div className="seller-info">
                        Sold by : <strong>Virani Clothing</strong>
                    </div>

                    <Button variant={'contained'} color={'error'} className="status-button">Cancel Order</Button>
                </div>
            </section>
        </Box>
    )
}

export default OrderDetails;
