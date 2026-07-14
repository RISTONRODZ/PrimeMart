import { Link } from "react-router-dom";
import { useSelector } from "react-redux";

const NotFound = () => {
    const user  = useSelector((state) => state);

    return (
        <div className="min-h-[70vh] flex flex-col items-center justify-center px-4 text-center">
            <div className="relative mb-8">
                <svg
                    width="140"
                    height="140"
                    viewBox="0 0 140 140"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                    role="img"
                    aria-label="Open, empty package illustrating a missing page"
                    className="animate-[float_3s_ease-in-out_infinite]"
                >
                    <ellipse cx="70" cy="118" rx="38" ry="6" fill="#dbeafe" />
                    <path
                        d="M25 48L70 26L115 48V92L70 114L25 92V48Z"
                        stroke="#1d4ed8"
                        strokeWidth="2.5"
                        strokeLinejoin="round"
                        fill="#eff6ff"
                    />
                    <path
                        d="M25 48L70 70L115 48"
                        stroke="#1d4ed8"
                        strokeWidth="2.5"
                        strokeLinejoin="round"
                    />
                    <path d="M70 70V114" stroke="#1d4ed8" strokeWidth="2.5" />
                    <path
                        d="M52 34L88 56"
                        stroke="#93c5fd"
                        strokeWidth="2"
                        strokeLinecap="round"
                        strokeDasharray="3 4"
                    />
                    <circle cx="70" cy="58" r="14" fill="white" stroke="#93c5fd" strokeWidth="2" />
                    <path
                        d="M64 52L76 64M76 52L64 64"
                        stroke="#93c5fd"
                        strokeWidth="2.5"
                        strokeLinecap="round"
                    />
                </svg>
            </div>

            <p className="text-blue-700 font-semibold text-sm tracking-wide uppercase mb-2">
                Error 404
            </p>
            <h1 className="text-2xl sm:text-3xl font-bold text-gray-900 mb-3">
                Nothing to unbox here
            </h1>
            <p className="text-gray-600 text-sm sm:text-base max-w-sm mb-8">
                The page you're looking for doesn't exist or may have moved.
                Check the link, or head back to browsing.
            </p>
            <div className="flex flex-col sm:flex-row gap-3">
                <Link
                    to="/"
                    className="px-6 py-2.5 rounded-md bg-blue-700 text-white text-sm font-medium hover:bg-blue-800 active:bg-blue-900 transition-colors"
                >
                    Back to home
                </Link>
                <Link
                    to={user ? "/account/orders" : "/login"}
                    className="px-6 py-2.5 rounded-md border border-gray-300 text-gray-700 text-sm font-medium hover:bg-gray-50 active:bg-gray-100 transition-colors"
                >
                    {user ? "View my orders" : "Sign in"}
                </Link>
            </div>

            <style>{`
                @keyframes float {
                    0%, 100% { transform: translateY(0px); }
                    50% { transform: translateY(-8px); }
                }
            `}</style>
        </div>
    );
};

export default NotFound;