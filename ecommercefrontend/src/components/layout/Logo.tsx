import {Link} from "react-router-dom";

export const Logo = ({className = ""}) => {
    return (
        <>

            <Link to={'/'}>
                <h1 className={`logo cursor-pointer text-gray-900 font-pacifico tracking-wide ${className}`}>
                    Prime<span className="text-blue-600">Mart</span>
                </h1>
            </Link>
        </>
    );
};
