const contactMethods = [
    {
        icon: (
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={1.8} className="w-5 h-5">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z" />
                <polyline points="22,6 12,13 2,6" />
            </svg>
        ),
        label: "Email",
        value: "support@primemart.in",
        note: "We respond within 24 business hours",
        href: "mailto:support@primemart.in",
    },
];

const socialLinks = [
    {
        label: "Instagram",
        href: "#",
        icon: (
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={1.8} className="w-5 h-5">
                <rect x="2" y="2" width="20" height="20" rx="5" ry="5" />
                <circle cx="12" cy="12" r="4" />
                <circle cx="17.5" cy="6.5" r="0.5" fill="currentColor" stroke="none" />
            </svg>
        ),
    },
    {
        label: "Twitter / X",
        href: "#",
        icon: (
            <svg viewBox="0 0 24 24" fill="currentColor" className="w-5 h-5">
                <path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z" />
            </svg>
        ),
    },
    {
        label: "Facebook",
        href: "#",
        icon: (
            <svg viewBox="0 0 24 24" fill="currentColor" className="w-5 h-5">
                <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z" />
            </svg>
        ),
    },
    {
        label: "YouTube",
        href: "#",
        icon: (
            <svg viewBox="0 0 24 24" fill="currentColor" className="w-5 h-5">
                <path d="M23.498 6.163a3.003 3.003 0 0 0-2.11-2.11C19.518 3.545 12 3.545 12 3.545s-7.518 0-9.388.507a3.003 3.003 0 0 0-2.11 2.11C0 8.033 0 12 0 12s0 3.967.502 5.837a3.003 3.003 0 0 0 2.11 2.11c1.87.507 9.388.507 9.388.507s7.518 0 9.388-.507a3.003 3.003 0 0 0 2.11-2.11C24 15.967 24 12 24 12s0-3.967-.502-5.837zM9.545 15.568V8.432L15.818 12l-6.273 3.568z" />
            </svg>
        ),
    },
];

const ContactSupport = () => {
    return (
        <div className="min-h-screen bg-white text-gray-800">

            <div className="bg-blue-700 text-white py-16 px-6">
                <div className="max-w-4xl mx-auto">
                    <p className="text-blue-200 text-sm font-medium uppercase tracking-widest mb-3">
                        Support
                    </p>
                    <h1 className="text-4xl font-bold mb-4">Contact Support</h1>
                    <p className="text-blue-100 text-base max-w-xl">
                        Have a question or ran into an issue? We're here to help.
                        Reach out and we'll get back to you as soon as possible.
                    </p>
                </div>
            </div>
            <div className="max-w-3xl mx-auto px-6 py-14">

                {contactMethods.map((method) => (
                    <a
                        key={method.label}
                        href={method.href}
                        className="flex items-start gap-5 p-6 border border-gray-100 rounded-xl shadow-sm hover:border-blue-200 hover:shadow-md transition-all"
                    >
                        <div className="w-10 h-10 rounded-full bg-blue-50 flex items-center justify-center text-blue-700 shrink-0">
                            {method.icon}
                        </div>
                        <div>
                            <p className="text-xs font-semibold uppercase tracking-widest text-blue-700 mb-1">
                                {method.label}
                            </p>
                            <p className="text-gray-800 font-medium text-sm mb-1">{method.value}</p>
                            <p className="text-gray-400 text-xs">{method.note}</p>
                        </div>
                    </a>
                ))}
                <div className="flex items-center gap-4 my-10">
                    <div className="flex-1 h-px bg-gray-100" />
                    <span className="text-xs text-gray-400 uppercase tracking-widest">or find us on</span>
                    <div className="flex-1 h-px bg-gray-100" />
                </div>
                <div className="flex gap-3 justify-center">
                    {socialLinks.map((s) => (
                        <a
                            key={s.label}
                            href={s.href}
                            aria-label={s.label}
                            className="w-10 h-10 rounded-full border border-gray-200 flex items-center justify-center text-gray-500 hover:border-blue-700 hover:text-blue-700 transition-colors"
                        >
                            {s.icon}
                        </a>
                    ))}
                </div>
                <div className="mt-12 bg-blue-50 border border-blue-100 rounded-xl p-6 text-center">
                    <p className="text-sm text-gray-600 mb-3">
                        Before reaching out, you might find your answer in our FAQ.
                    </p>
                    <a
                        href="/faqs"
                        className="inline-block text-sm font-semibold text-blue-700 underline underline-offset-4 hover:text-blue-900 transition-colors"
                    >
                        Browse FAQs →
                    </a>
                </div>
            </div>

            <div className="bg-blue-700 text-white py-10 px-6 text-center">
                <p className="text-blue-100 text-sm mb-1">We typically respond within 24 business hours.</p>
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

export default ContactSupport;