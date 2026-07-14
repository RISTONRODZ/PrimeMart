import { Navigate, useLocation } from "react-router-dom";
import { useAppSelector } from "../../state/hooks.ts";

const ProtectedRoute = ({
                            children,
                            requiredRole,
                        }: {
    children: React.ReactNode;
    requiredRole?: string;
}) => {
    const jwt = useAppSelector((store) => store.auth.jwt);
    const isAuthenticated = useAppSelector((store) => store.auth.isAuthenticated);
    const role = useAppSelector((store) => store.auth.role);
    const location = useLocation();

    if (!isAuthenticated || !jwt) {
        const isSellerRoute = location.pathname.startsWith("/seller");
        const loginPath = isSellerRoute ? "/become-seller" : "/login";
        return <Navigate to={loginPath} state={{ from: location.pathname }} replace />;
    }

    if (requiredRole && role !== requiredRole) {
        return <Navigate to="/" replace />;
    }

    return <>{children}</>;
};

export default ProtectedRoute;