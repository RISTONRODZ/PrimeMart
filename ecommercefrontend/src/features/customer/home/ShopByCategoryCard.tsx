import { useState } from "react";
import { useNavigate } from "react-router-dom";

const ShopByCategoryCard = ({ item }) => {
    const [imgError, setImgError] = useState(false);
    const navigate = useNavigate();

    const handleCategoryClick = () => {
        navigate(`/product/${item.name}`);
    };

    return (
        <div onClick={handleCategoryClick} className="flex flex-col h-full rounded-2xl overflow-hidden border border-gray-100 bg-white shadow-sm hover:shadow-md transition-shadow duration-300 cursor-pointer group">
            <div className="h-50 w-full overflow-hidden bg-gray-100 shrink-0">
                {imgError ? (
                    <div className="w-full h-full flex items-center justify-center bg-blue-50 p-4">
                        <span className="text-blue-900 text-sm font-semibold text-center">
                            {item.name}
                        </span>
                    </div>
                ) : (
                    <img
                        src={item.imageUrl}
                        alt={item.name}
                        onError={() => setImgError(true)}
                        className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                    />
                )}
            </div>

            <div className="flex flex-col flex-1 p-4">
                <h3 className="font-semibold text-gray-900 text-sm line-clamp-2 min-h-[40px]">
                    {item.name}
                </h3>

                <p className="text-xs text-gray-500 mt-1">
                    {item.section}
                </p>

                {item.discount && (
                    <span className="mt-auto inline-flex w-fit rounded-full bg-blue-50 px-2 py-1 text-[10px] font-semibold text-blue-900">
                        {item.discount}
                    </span>
                )}
            </div>
        </div>
    );
};

export default ShopByCategoryCard;