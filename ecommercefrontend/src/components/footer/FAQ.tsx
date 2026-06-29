import { useState } from "react";

const faqs = [
    {
        category: "Authentication",
        questions: [
            {
                q: "How does OTP login work?",
                a: "PrimeMart uses a one-time password (OTP) sent to your registered phone number instead of a traditional password. Enter your phone number, receive the OTP, and you're in — no password to remember or forget.",
            },
            {
                q: "I didn't receive my OTP. What do I do?",
                a: "Check that your phone number is entered correctly and has network coverage. OTPs can take up to 60 seconds to arrive. If it still doesn't come, use the Resend OTP option on the login screen.",
            },
            {
                q: "Is my account secure?",
                a: "Yes. Since PrimeMart uses OTP-based login, there's no stored password that can be compromised. Each OTP is single-use and expires within a short window, keeping your account protected.",
            },
        ],
    },
    {
        category: "Orders & Payments",
        questions: [
            {
                q: "What payment methods are accepted?",
                a: "PrimeMart accepts UPI, credit and debit cards, net banking, and wallets — all powered by Razorpay. You can choose your preferred method at checkout.",
            },
            {
                q: "Is my payment information safe?",
                a: "Absolutely. All payments are processed securely through Razorpay. PrimeMart never stores your card or UPI details on our servers. Transactions are encrypted end-to-end.",
            },
            {
                q: "Can I cancel an order after placing it?",
                a: "Yes, you can cancel an order before it is dispatched. Head to your Orders page, select the order, and choose Cancel. Once an order has been shipped, cancellation is no longer available.",
            },
        ],
    },
    {
        category: "Sellers",
        questions: [
            {
                q: "Are sellers on PrimeMart verified?",
                a: "Yes. Every seller goes through a verification process before they can list products on PrimeMart. This ensures you're buying from legitimate, trustworthy sellers.",
            },
            {
                q: "How do I become a seller on PrimeMart?",
                a: "Click on 'Become a Seller' in the navigation or footer. Fill in your business details and submit for verification. Once approved, you can start listing your products.",
            },
            {
                q: "How do I contact a seller?",
                a: "Each product page displays seller information. You can reach out through the contact option available on the seller's profile page.",
            },
        ],
    },
    {
        category: "General",
        questions: [
            {
                q: "What is PrimeMart?",
                a: "PrimeMart is a full-stack e-commerce platform where verified sellers list products across categories like fashion, electronics, home & furniture, and more. It's built for a fast, secure shopping experience.",
            },
            {
                q: "How do I contact support?",
                a: "You can reach our support team through the Contact Support page. We aim to respond to all queries within 24 hours on business days.",
            },
        ],
    },
];

const FAQItem = ({ q, a }: { q: string; a: string }) => {
    const [open, setOpen] = useState(false);

    return (
        <div className="border-b border-gray-100 last:border-0">
            <button
                onClick={() => setOpen(!open)}
                className="w-full flex items-center justify-between py-4 text-left gap-4 group"
            >
                <span className="text-sm font-medium text-gray-800 group-hover:text-blue-700 transition-colors">
                    {q}
                </span>
                <span className={`text-blue-700 text-lg font-light flex-shrink-0 transition-transform duration-200 ${open ? "rotate-45" : ""}`}>
                    +
                </span>
            </button>
            {open && (
                <p className="text-sm text-gray-500 leading-relaxed pb-4 pr-8">
                    {a}
                </p>
            )}
        </div>
    );
};

const FAQ = () => {
    return (
        <div className="min-h-screen bg-white text-gray-800">

            {/* Hero */}
            <div className="bg-blue-700 text-white py-16 px-6">
                <div className="max-w-4xl mx-auto">
                    <p className="text-blue-200 text-sm font-medium uppercase tracking-widest mb-3">
                        Support
                    </p>
                    <h1 className="text-4xl font-bold mb-4">Frequently Asked Questions</h1>
                    <p className="text-blue-100 text-base max-w-xl">
                        Quick answers to the most common questions about shopping, payments, and selling on PrimeMart.
                    </p>
                </div>
            </div>

            {/* FAQ sections */}
            <div className="max-w-3xl mx-auto px-6 py-14 space-y-12">
                {faqs.map((section) => (
                    <div key={section.category}>
                        <h2 className="text-xs font-semibold uppercase tracking-widest text-blue-700 mb-4">
                            {section.category}
                        </h2>
                        <div className="bg-white border border-gray-100 rounded-xl px-6 divide-y divide-gray-100 shadow-sm">
                            {section.questions.map((item) => (
                                <FAQItem key={item.q} q={item.q} a={item.a} />
                            ))}
                        </div>
                    </div>
                ))}
            </div>

            {/* Footer strip */}
            <div className="bg-blue-700 text-white py-10 px-6 text-center">
                <p className="text-blue-100 text-sm mb-1">Still have questions?</p>
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

export default FAQ;