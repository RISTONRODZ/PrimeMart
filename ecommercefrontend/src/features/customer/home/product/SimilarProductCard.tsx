import { useState } from "react";

export interface Product {
    id: number;
    brand: string;
    name: string;
    price: number;
    originalPrice: number;
    rating: number;
    reviewCount: number;
    images: string[];
    badge?: "New" | "Sale" | "Hot";
    variant?: "solid" | "outline";
}

interface ProductCardProps {
    product: Product;
    onAddToCart?: (product: Product) => void;
    onWishlist?: (product: Product) => void;
}

const formatPrice = (price: number): string => `₹${price.toLocaleString("en-IN")}`;

const discountPercent = (original: number, current: number): number =>
    Math.round(((original - current) / original) * 100);

export const SimilarProductCard = ({ product }: ProductCardProps) => {
    const [currentImage, setCurrentImage] = useState(0);

    const discount = discountPercent(product.originalPrice, product.price);

    return (
        <div className="group w-full max-w-70 mx-auto p-2 sm:p-4 cursor-pointer">
            <div className="relative w-full aspect-square sm:h-80 overflow-hidden rounded-md bg-gray-50">
                <img
                    src={product.images[currentImage]}
                    alt={product.name}
                    className="w-full h-full object-contain transition-transform duration-500 ease-in-out group-hover:scale-110"
                />

                <div className="absolute bottom-4 left-1/2 -translate-x-1/2 flex gap-1.5 sm:gap-2 z-10">
                    {product.images.map((_, index) => (
                        <button
                            key={index}
                            onClick={() => setCurrentImage(index)}
                            className={`w-2 h-2 sm:w-2.5 sm:h-2.5 rounded-full transition-all duration-300 ${
                                currentImage === index ? "bg-blue-600" : "bg-gray-300"
                            }`}
                        />
                    ))}
                </div>
            </div>

            <div className="mt-3">
                <p className="text-[10px] sm:text-xs font-semibold text-blue-700 uppercase tracking-wide mb-1">
                    {product.brand}
                </p>
                <p className="text-sm sm:text-base text-gray-900 font-medium mb-1.5 line-clamp-2">
                    {product.name}
                </p>
                <div className="flex flex-col sm:flex-row sm:items-center gap-1 sm:gap-2 flex-wrap">
          <span className="font-bold text-base sm:text-lg text-gray-900">
            {formatPrice(product.price)}
          </span>
                    <div className="flex items-center gap-2">
            <span className="text-xs sm:text-sm text-gray-400 line-through">
              {formatPrice(product.originalPrice)}
            </span>
                        <span className="text-green-600 text-xs sm:text-sm font-medium">
              {discount}% Off
            </span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SimilarProductCard;