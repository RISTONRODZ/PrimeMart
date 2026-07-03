import {Button, Card, Divider} from "@mui/material";
import TransactionTable from "./TransactionTable.tsx";

const Payment = () => {
    return (
        <div className={'space-y-5'}>
            <Card className={'rounded-md space-y-4 p-4 sm:p-5'}>
                <h1 className={'text-gray-600 font-medium text-sm sm:text-base'}>Total Earning</h1>
                <h1 className={'text-lg sm:text-xl font-medium pb-1'}>$1,234.56</h1>
                <Divider/>
                <p className={'text-gray-600 font-medium pt-1 text-sm sm:text-base'}>Last Payment: <strong>$1,234.56</strong></p>
            </Card>
            <div className={'mt-6 sm:mt-10'}>
                <Button variant={'contained'} fullWidth={true} className={'sm:w-auto'}>
Transaction
                </Button>
                <div className={'mt-4 sm:mt-5'}>
                <TransactionTable/>
                </div>
            </div>
        </div>
    );
};

export default Payment;