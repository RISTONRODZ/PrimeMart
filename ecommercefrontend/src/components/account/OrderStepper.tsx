import { Box, Typography } from "@mui/material";
import { useMemo } from "react";
import FiberManualRecordIcon from "@mui/icons-material/FiberManualRecord";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";

const steps = [
    { name: "Order Placed", description: "on Thu, 11 Jul", value: "PLACED" },
    { name: "Packed", description: "Item Packed", value: "CONFIRMED" },
    { name: "Shipped", description: "by Mon, 15 Jul", value: "SHIPPED" },
    { name: "Arriving", description: "by 16-18 Jul", value: "ARRIVING" },
    { name: "Delivered", description: "by 16-18 Jul", value: "DELIVERED" },
];

const canceledStep = [
    { name: "Order Placed", description: "on Thu, 11 Jul", value: "PLACED" },
    { name: "Order Canceled", description: "on Thu, 11 Jul", value: "CANCELLED" },
];

const OrderStepper = ({ orderStatus }: { orderStatus: string }) => {
    const statusSteps = useMemo(() => (orderStatus === 'CANCELLED' ? canceledStep : steps), [orderStatus]);
    const currentStepIndex = statusSteps.findIndex((s) => s.value === orderStatus);

    return (
        <Box className="max-w-sm mx-auto p-4 sm:p-6 bg-white">
            {statusSteps.map((step, index) => {
                const isCompleted = index < currentStepIndex;
                const isActive = index === currentStepIndex;
                const isCanceled = orderStatus === 'CANCELLED';

                return (
                    <div key={step.value} className="relative flex gap-4">
                        {index < statusSteps.length - 1 && (
                            <div
                                className={`absolute left-4 top-8 -bottom-6 w-0.5 ${
                                    isCompleted ? "bg-blue-500" : "bg-gray-200"
                                }`}
                            />
                        )}

                        <div className="relative z-10 flex flex-col items-center">
                            <div className={`mt-1 flex items-center justify-center w-8 h-8 rounded-full border-2 transition-colors duration-300 ${
                                isCompleted
                                    ? "bg-blue-500 border-blue-500 text-white"
                                    : isActive
                                        ? (isCanceled ? "bg-red-500 border-red-500 text-white" : "bg-blue-500 border-blue-500 text-white")
                                        : "bg-white border-gray-300 text-gray-300"
                            }`}>
                                {isCompleted ? <CheckCircleIcon fontSize="small" /> : <FiberManualRecordIcon fontSize="small" />}
                            </div>
                        </div>

                        <div className="pb-8">
                            <Typography className={`font-semibold ${isActive ? "text-gray-900" : "text-gray-500"}`}>
                                {step.name}
                            </Typography>
                            <Typography className="text-xs text-gray-400">
                                {step.description}
                            </Typography>
                        </div>
                    </div>
                );
            })}
        </Box>
    );
};

export default OrderStepper;