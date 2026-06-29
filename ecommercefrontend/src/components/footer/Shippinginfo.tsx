const steps = [
    { step: "01", title: "Order Placed", desc: "You place your order and receive a confirmation email." },
    { step: "02", title: "Order Confirmed", desc: "The seller confirms your order within 1–2 business days." },
    { step: "03", title: "Dispatched", desc: "Your order is handed over to our delivery partner." },
    { step: "04", title: "Delivered", desc: "Your order arrives at your doorstep." },
];

const faqs = [
    {
        q: "Do you ship across India?",
        a: "Yes, PrimeMart delivers to most pin codes across India. Enter your pin code at checkout to confirm availability in your area.",
    },
    {
        q: "Can I change my delivery address after placing an order?",
        a: "Address changes are only possible before the order is dispatched. Contact support as soon as possible if you need to update your address.",
    },
    {
        q: "What if I'm not available at the time of delivery?",
        a: "Our delivery partner will attempt delivery up to 2 times. If both attempts fail, the order will be returned to the seller.",
    },
];

const ShippingInfo = () => {
    return (
        <div className="min-h-screen bg-white text-gray-800">
            <div className="bg-blue-700 text-white py-16 px-6">
                <div className="max-w-4xl mx-auto">
                    <p className="text-blue-200 text-sm font-medium uppercase tracking-widest mb-3">
                        Support
                    </p>
                    <h1 className="text-4xl font-bold mb-4">Shipping Info</h1>
                    <p className="text-blue-100 text-base max-w-xl">
                        Everything you need to know about how we deliver your orders across India.
                    </p>
                </div>
            </div>
            <div className="bg-blue-50 border-b border-blue-100 py-8 px-6">
                <div className="max-w-4xl mx-auto grid grid-cols-1 sm:grid-cols-3 gap-6">
                    {[
                        { icon: "🚚", label: "Standard Delivery", desc: "5–7 business days across India" },
                        { icon: "📍", label: "Pan India Delivery", desc: "We deliver to most pin codes across India" },
                        { icon: "🎁", label: "Free Delivery", desc: "On all orders, no minimum required" },
                    ].map((item) => (
                        <div key={item.label} className="flex items-start gap-3">
                            <span className="text-2xl">{item.icon}</span>
                            <div>
                                <p className="font-semibold text-blue-700 text-sm">{item.label}</p>
                                <p className="text-gray-500 text-sm">{item.desc}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            <div className="max-w-3xl mx-auto px-6 py-14 space-y-14">
                <div>
                    <h2 className="text-xs font-semibold uppercase tracking-widest text-blue-700 mb-4">
                        Shipping Charges
                    </h2>
                    <div className="border border-gray-100 rounded-xl p-6 shadow-sm">
                        <p className="text-sm text-gray-600 leading-relaxed">
                            PrimeMart offers <span className="font-semibold text-gray-800">free delivery</span> on all orders.
                        </p>
                    </div>
                </div>
                <div>
                    <h2 className="text-xs font-semibold uppercase tracking-widest text-blue-700 mb-6">
                        How It Works
                    </h2>
                    <div className="relative">
                        <div className="absolute left-5 top-0 bottom-0 w-px bg-blue-100" />
                        <div className="space-y-8">
                            {steps.map((s) => (
                                <div key={s.step} className="flex items-start gap-5 relative">
                                    <div className="w-10 h-10 rounded-full bg-blue-700 text-white flex items-center justify-center text-xs font-bold shrink-0 z-10">
                                        {s.step}
                                    </div>
                                    <div className="pt-2">
                                        <p className="text-sm font-semibold text-gray-800 mb-1">{s.title}</p>
                                        <p className="text-sm text-gray-500">{s.desc}</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
                <div>
                    <h2 className="text-xs font-semibold uppercase tracking-widest text-blue-700 mb-4">
                        Common Questions
                    </h2>
                    <div className="border border-gray-100 rounded-xl px-6 shadow-sm divide-y divide-gray-100">
                        {faqs.map((item) => (
                            <div key={item.q} className="border-l-4 border-blue-700 pl-4 py-5 -ml-6 my-1">
                                <p className="text-sm font-semibold text-gray-800 mb-1">{item.q}</p>
                                <p className="text-sm text-gray-500 leading-relaxed">{item.a}</p>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
            <div className="bg-blue-700 text-white py-10 px-6 text-center">
                <p className="text-blue-100 text-sm mb-1">Still have questions about your delivery?</p>
                <a
                    href="/contact-support"
                    className="text-white font-semibold underline underline-offset-4 hover:text-blue-200 transition-colors"
                >
                    Contact Support
                </a>
            </div>
        </div>
    );
};

export default ShippingInfo;