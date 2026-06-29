const sections = [
    {
        title: "1. Information We Collect",
        content: `When you use PrimeMart, we collect information you provide directly — such as your name, email address, phone number, shipping address, and payment details when you register or place an order. We also automatically collect usage data like your IP address, browser type, device information, and pages visited to improve your experience.`,
    },
    {
        title: "2. How We Use Your Information",
        content: `We use your information to process and deliver your orders, send order confirmations and shipping updates, provide customer support, personalise your shopping experience, and improve our platform.`,
    },
    {
        title: "3. Sharing Your Information",
        content: `We do not sell your personal data. We share it only with trusted partners who help us operate PrimeMart — including payment processors (such as Razorpay), logistics and delivery partners, and cloud infrastructure providers. All partners are bound by strict data protection agreements.`,
    },
    {
        title: "4. Payment Security",
        content: `All payments on PrimeMart are processed securely through Razorpay. We do not store your full card details on our servers. Transactions are encrypted using industry-standard TLS/SSL protocols, and we comply with PCI-DSS standards for payment data security.`,
    },
    {
        title: "5. Cookies",
        content: `We use cookies to keep you logged in, remember your cart, and understand how you interact with our site. You can control cookies through your browser settings. Disabling cookies may affect some features of PrimeMart.`,
    },
    {
        title: "6. Data Retention",
        content: `We retain your personal data for as long as your account is active or as needed to provide services. If you delete your account, we will remove your personal information within 30 days, except where we are legally required to retain it (such as transaction records for tax purposes).`,
    },
    {
        title: "7. Your Rights",
        content: `You have the right to access, correct, or delete your personal data at any time. You may also request a copy of the data we hold about you. To exercise any of these rights, contact us at privacy@primemart.in. We will respond within 7 business days.`,
    },
    {
        title: "8. Children's Privacy",
        content: `PrimeMart is not intended for children under the age of 13. We do not knowingly collect personal information from children. If you believe a child has provided us with their data, please contact us and we will promptly delete it.`,
    },
    {
        title: "9. Changes to This Policy",
        content: `We may update this Privacy Policy from time to time. When we do, we'll revise the "Last updated" date at the top of this page and notify you via email or an in-app notice if the changes are significant. Continued use of PrimeMart after changes means you accept the revised policy.`,
    },
    {
        title: "10. Contact Us",
        content: `If you have any questions about this Privacy Policy or how we handle your data, reach out to us at privacy@primemart.in or write to us at PrimeMart Pvt. Ltd., Mumbai, Maharashtra, India.`,
    },
];

const PrivacyPolicy = () => {
    return (
        <div className="min-h-screen bg-white text-gray-800">


            <div className="bg-blue-700 text-white py-16 px-6">
                <div className="max-w-4xl mx-auto">
                    <p className="text-blue-200 text-sm font-medium uppercase tracking-widest mb-3">
                        Legal
                    </p>
                    <h1 className="text-4xl font-bold mb-4">Privacy Policy</h1>
                    <p className="text-blue-100 text-base max-w-xl">
                        We believe you should know exactly what data we collect and why.
                        Here's a plain-language breakdown of how PrimeMart handles your information.
                    </p>
                    <p className="text-blue-300 text-sm mt-6">Last updated: June 2026</p>
                </div>
            </div>

            {/* Quick highlights */}
            <div className="bg-blue-50 border-b border-blue-100 py-8 px-6">
                <div className="max-w-4xl mx-auto grid grid-cols-1 sm:grid-cols-3 gap-6">
                    {[
                        { icon: "🔒", label: "Secure Payments", desc: "Powered by Razorpay with TLS encryption" },
                        { icon: "🚫", label: "No Data Selling", desc: "We never sell your personal information" },
                        { icon: "✏️", label: "Your Control", desc: "Access, edit or delete your data anytime" },
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
                <p className="text-blue-100 text-sm mb-1">Questions about your privacy?</p>
                <a
                    href="mailto:privacy@primemart.in"
                    className="text-white font-semibold underline underline-offset-4 hover:text-blue-200 transition-colors"
                >
                    privacy@primemart.in
                </a>
            </div>
        </div>
    );
};

export default PrivacyPolicy;