interface PricingCardProps {
    title: string;
    price: number;
    features: string[];
}
const PricingCard = ({ title, price, features}: PricingCardProps) => {
    const handleRazorpayPayment = () => {
        console.log("Initiating Razorpay payment...");
        alert("Redirecting to Razorpay...");
    };

    return (
        <div className="border border-blue-700 rounded-lg p-6 lg:w-100 shadow-md bg-white flex flex-col items-center text-center w-80">
            <h2 className="text-xl font-bold text-slate-800 mb-2">{title}</h2>
            <div className="text-3xl font-extrabold text-blue-700 my-4">
                ₹{price}
            </div>
            <ul className="text-slate-600 mb-6 space-y-2">
                {features.map((feature, index) => (
                    <li key={index}>✓ {feature}</li>
                ))}
            </ul>
            <button
                onClick={handleRazorpayPayment}
                className="w-full bg-blue-700 text-white font-semibold py-2 rounded hover:bg-blue-800 transition-colors"
            >
                Pay with Razorpay
            </button>
        </div>
    );
};

export default PricingCard;