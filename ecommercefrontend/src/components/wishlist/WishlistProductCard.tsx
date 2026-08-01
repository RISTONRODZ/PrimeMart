import { useAppDispatch } from "../../state/hooks.ts";
import type { Product } from "../../types/ProductTypes.ts";
import { useNavigate } from "react-router-dom";
import { removeProductFromWishlist } from "../../state/customer/WishlistSlice.ts";
import CloseIcon from "@mui/icons-material/Close";
import React from "react";

interface ProductCardProps {
    item: Product;
}

const WishlistProductCard: React.FC<ProductCardProps> = ({ item }) => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const handleIconClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.stopPropagation();
        if (item.id) dispatch(removeProductFromWishlist({ productId: item.id }));
    };

    const handleCardClick = () => {
        navigate(`/product-details/${item.categoryName}/${item.title}/${item.id}`);
    };

    return (
        <div
            className="group relative w-full max-w-55 sm:max-w-60 mx-auto cursor-pointer"
            onClick={handleCardClick}
        >
            <div className="relative w-full aspect-3/4 overflow-hidden rounded-lg bg-gray-100">
                <img
                    className="h-full w-full object-cover object-top
                               transition-transform duration-300 group-hover:scale-[1.03]"
                    src={item.images[0]}
                    alt={`product-${item.title}`}
                    loading="lazy"
                />

                {item.discountPercent > 0 && (
                    <span className="absolute top-2 left-2 rounded-md bg-blue-700 px-1.5 py-0.5
                                     text-[10px] sm:text-xs font-semibold text-white shadow-sm">
                        {item.discountPercent}% OFF
                    </span>
                )}

                <button
                    onClick={handleIconClick}
                    aria-label="Remove from wishlist"
                    className="absolute top-2 right-2 flex h-7 w-7 sm:h-8 sm:w-8 items-center justify-center
                               rounded-full bg-white/90 backdrop-blur-sm shadow-md
                               transition-all duration-200
                               hover:bg-blue-700 hover:scale-110 active:scale-95"
                >
                    <CloseIcon
                        className="text-gray-700 transition-colors duration-200 hover:text-white"
                        sx={{ fontSize: { xs: "1rem", sm: "1.15rem" } }}
                    />
                </button>
            </div>

            <div className="pt-2.5 sm:pt-3 space-y-1">
                <p className="truncate text-sm sm:text-base font-medium text-gray-800">
                    {item.title}
                </p>

                <div className="flex flex-wrap items-center gap-1.5 sm:gap-2">
                    <span className="text-sm sm:text-base font-semibold text-gray-900">
                        ₹{item.sellingPrice}
                    </span>
                    <span className="text-xs sm:text-sm line-through text-gray-400">
                        ₹{item.mrpPrice}
                    </span>
                    {item.discountPercent > 0 && (
                        <span className="text-xs sm:text-sm font-semibold text-blue-700">
                            {item.discountPercent}% off
                        </span>
                    )}
                </div>
            </div>
        </div>
    );
};

export default WishlistProductCard;