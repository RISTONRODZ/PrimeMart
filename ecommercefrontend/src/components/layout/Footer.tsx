import {Link} from "react-router-dom";

const footerLinks = {
    shop: [
        { label: "Men's fashion", to: "#" },
        { label: "Women's fashion", to: "#" },
        { label: "Electronics", to: "#" },
        { label: "Home & furniture", to: "#" },
        { label: "Beauty & care", to: "#" },
        { label: "Sports & outdoors", to: "#" },
    ],
    help: [
        { label: "Track your order", to: "#" },
        { label: "Shipping info", to: "/shipping-info" },
        { label: "FAQs", to: "/faqs" },
        { label: "Contact support", to: "/contact-support" },
        // { label: "Size guide", to: "#" },
    ],
    company: [
        { label: "About us", to: "/about" },
        { label: "Become a seller", to: "/become-seller" },
    ],
};

const socialLinks = [
    {
        label: "Instagram",
        href: "#",
        icon: (
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={1.8} className="w-4 h-4">
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
            <svg viewBox="0 0 24 24" fill="currentColor" className="w-4 h-4">
                <path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z" />
            </svg>
        ),
    },
    {
        label: "Facebook",
        href: "#",
        icon: (
            <svg viewBox="0 0 24 24" fill="currentColor" className="w-4 h-4">
                <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z" />
            </svg>
        ),
    },
    {
        label: "YouTube",
        href: "#",
        icon: (
            <svg viewBox="0 0 24 24" fill="currentColor" className="w-4 h-4">
                <path d="M23.498 6.163a3.003 3.003 0 0 0-2.11-2.11C19.518 3.545 12 3.545 12 3.545s-7.518 0-9.388.507a3.003 3.003 0 0 0-2.11 2.11C0 8.033 0 12 0 12s0 3.967.502 5.837a3.003 3.003 0 0 0 2.11 2.11c1.87.507 9.388.507 9.388.507s7.518 0 9.388-.507a3.003 3.003 0 0 0 2.11-2.11C24 15.967 24 12 24 12s0-3.967-.502-5.837zM9.545 15.568V8.432L15.818 12l-6.273 3.568z" />
            </svg>
        ),
    },
];

const Footer = () => {
    return (
        <footer className="bg-blue-700 text-white">
            <div className="max-w-6xl mx-auto px-6 pt-12 pb-8 grid grid-cols-2 sm:grid-cols-4 gap-8">
                <div className="col-span-2 sm:col-span-1">
                    <div className="text-xl font-bold tracking-tight mb-2">
                        Prime<span className="text-blue-300">Mart</span>
                    </div>
                    <p className="text-sm text-blue-200 leading-relaxed mb-4 max-w-50">
                        Your one-stop destination for everything, delivered fast to your doorstep.
                    </p>
                    <div className="flex gap-2 mb-5">
                        {socialLinks.map((s) => (
                            <a
                                key={s.label}
                                href={s.href}
                                aria-label={s.label}
                                className="w-8 h-8 rounded-full bg-white/10 border border-white/20 flex items-center justify-center text-white hover:bg-white/25 transition-colors"
                            >
                                {s.icon}
                            </a>
                        ))}
                    </div>
                    <div className="flex flex-col gap-2">
                        <div className="flex items-center gap-2 text-xs text-blue-200">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={1.8} className="w-4 h-4 shrink-0">
                                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
                            </svg>
                            Secure payments
                        </div>
                        <div className="flex items-center gap-2 text-xs text-blue-200">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={1.8} className="w-4 h-4 shrink-0">
                                <path d="M1 3h15v13H1zM16 8h4l3 3v5h-7V8z" />
                                <circle cx="5.5" cy="18.5" r="2.5" />
                                <circle cx="18.5" cy="18.5" r="2.5" />
                            </svg>
                            Fast delivery
                        </div>
                    </div>
                </div>
                <div>
                    <h4 className="text-xs font-medium tracking-widest uppercase text-blue-300 mb-4">Shop</h4>
                    <ul className="space-y-2.5">
                        {footerLinks.shop.map((link) => (
                            <li key={link.label}>
                                <Link to={link.to} className="text-sm text-blue-200 hover:text-white transition-colors">
                                    {link.label}
                                </Link>
                            </li>
                        ))}
                    </ul>
                </div>
                <div>
                    <h4 className="text-xs font-medium tracking-widest uppercase text-blue-300 mb-4">Help</h4>
                    <ul className="space-y-2.5">
                        {footerLinks.help.map((link) => (
                            <li key={link.label}>
                                <Link to={link.to} className="text-sm text-blue-200 hover:text-white transition-colors">
                                    {link.label}
                                </Link>
                            </li>
                        ))}
                    </ul>
                </div>
                <div>
                    <h4 className="text-xs font-medium tracking-widest uppercase text-blue-300 mb-4">Company</h4>
                    <ul className="space-y-2.5">
                        {footerLinks.company.map((link) => (
                            <li key={link.label}>
                                <Link to={link.to} className="text-sm text-blue-200 hover:text-white transition-colors">
                                    {link.label}
                                </Link>
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
            <div className="border-t border-white/15">
                <div className="max-w-6xl mx-auto px-6 py-4 flex flex-col sm:flex-row items-center justify-between gap-3">
                    <span className="text-xs text-blue-300">© {new Date().getFullYear()} PrimeMart. All rights reserved.</span>
                    <div className="flex gap-5">
                        <Link to="/privacy-policy" className="text-xs text-blue-300 hover:text-white transition-colors">
                            Privacy policy
                        </Link>
                        <Link to="/terms-of-use" className="text-xs text-blue-300 hover:text-white transition-colors">
                            Terms of use
                        </Link>
                    </div>
                </div>
            </div>
        </footer>
    );
};

export default Footer;