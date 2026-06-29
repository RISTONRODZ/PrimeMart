const sections = [
    {
        title: "1. Acceptance of Terms",
        content: `By accessing or using PrimeMart, you confirm that you are at least 18 years old and agree to be bound by these Terms of Use. If you do not agree with any part of these terms, please do not use our platform.`,
    },
    {
        title: "2. Account Responsibility",
        content: `You are responsible for maintaining the confidentiality of your account credentials. Any activity that occurs under your account is your responsibility. If you suspect unauthorized access, contact us immediately. PrimeMart reserves the right to suspend or terminate accounts that violate these terms.`,
    },
    {
        title: "3. Orders & Payments",
        content: `By placing an order on PrimeMart, you agree to provide accurate and complete payment and shipping information. All payments are processed securely through Razorpay. PrimeMart reserves the right to cancel orders in cases of pricing errors, stock unavailability, or suspected fraudulent activity.`,
    },
    {
        title: "4. Pricing & Availability",
        content: `All prices on PrimeMart are listed in Indian Rupees (INR) and are subject to change without notice. We do not guarantee that any product will be available at the time of your order. In the event of a pricing error, we reserve the right to cancel the affected order and issue a full refund.`,
    },
    {
        title: "5. Shipping & Delivery",
        content: `Estimated delivery times are provided at checkout and are not guaranteed. PrimeMart is not liable for delays caused by courier partners, natural events, or circumstances beyond our control. Risk of loss and title for items pass to you upon delivery.`,
    },
    {
        title: "6. Seller Conduct",
        content: `Sellers on PrimeMart agree to list only genuine products, provide accurate descriptions, and fulfill orders in a timely manner. Fraudulent listings, counterfeit products, or repeated policy violations will result in immediate account termination and potential legal action.`,
    },
    {
        title: "7. Prohibited Activities",
        content: `You agree not to use PrimeMart for any unlawful purpose, including but not limited to: submitting false orders, scraping or harvesting data, attempting to gain unauthorized access to our systems, or engaging in any activity that disrupts the platform for other users.`,
    },
    {
        title: "8. Intellectual Property",
        content: `All content on PrimeMart — including the logo, design, text, and code — is the property of PrimeMart Pvt. Ltd. and is protected under applicable intellectual property laws. You may not reproduce, distribute, or use our content without prior written permission.`,
    },
    {
        title: "9. Limitation of Liability",
        content: `PrimeMart is provided on an "as is" basis. To the fullest extent permitted by law, we are not liable for any indirect, incidental, or consequential damages arising from your use of the platform, including loss of data, revenue, or business opportunities.`,
    },
    {
        title: "11. Changes to Terms",
        content: `We may update these Terms of Use at any time. When we do, we will revise the "Last updated" date on this page. Continued use of PrimeMart after changes are posted constitutes your acceptance of the revised terms. We recommend reviewing this page periodically.`,
    },
    {
        title: "12. Contact Us",
        content: `If you have questions about these Terms of Use, please contact us at support@primemart.in or write to PrimeMart Pvt. Ltd., Mumbai, Maharashtra, India.`,
    },
];

const TermsOfUse = () => {
    return (
        <div className="min-h-screen bg-white text-gray-800">
            <div className="bg-blue-700 text-white py-16 px-6">
                <div className="max-w-4xl mx-auto">
                    <p className="text-blue-200 text-sm font-medium uppercase tracking-widest mb-3">
                        Legal
                    </p>
                    <h1 className="text-4xl font-bold mb-4">Terms of Use</h1>
                    <p className="text-blue-100 text-base max-w-xl">
                        These terms govern your use of PrimeMart. Please read them carefully
                        before placing orders or creating an account.
                    </p>
                    <p className="text-blue-300 text-sm mt-6">Last updated: June 2026</p>
                </div>
            </div>

            <div className="bg-blue-50 border-b border-blue-100 py-8 px-6">
                <div className="max-w-4xl mx-auto grid grid-cols-1 sm:grid-cols-3 gap-6">
                    {[
                        { icon: "🔒", label: "Secure Payments", desc: "Powered by Razorpay with encryption" },
                        { icon: "🏪", label: "Verified Sellers", desc: "Every seller is reviewed before listing" },
                        { icon: "🛡️", label: "Seller Standards", desc: "Only genuine products allowed" },
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

            <div className="max-w-4xl mx-auto px-6 py-14 space-y-10">
                {sections.map((section) => (
                    <div key={section.title} className="border-l-4 border-blue-700 pl-6">
                        <h2 className="text-lg font-semibold text-blue-700 mb-2">
                            {section.title}
                        </h2>
                        <p className="text-gray-600 leading-relaxed text-sm">
                            {section.content}
                        </p>
                    </div>
                ))}
            </div>
            <div className="bg-blue-700 text-white py-10 px-6 text-center">
                <p className="text-blue-100 text-sm mb-1">Questions about these terms?</p>
                <a
                    href="mailto:support@primemart.in"
                    className="text-white font-semibold underline underline-offset-4 hover:text-blue-200 transition-colors"
                >
                    support@primemart.in
                </a>
            </div>
        </div>
    );
};

export default TermsOfUse;