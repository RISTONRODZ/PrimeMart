import {useMemo, useState, type FC, useEffect} from "react";
import type {Product} from "../../../../types/ProductTypes.ts";
import {useNavigate} from "react-router-dom";
import {useAppDispatch} from "../../../../state/hooks.ts";
import {addProductToWishlist} from "../../../../state/customer/WishlistSlice.ts";

interface ProductCardProps {
    product: Product;
}

const formatPrice = (value: number) => `₹${value.toLocaleString("en-IN")}`;

const ProductCard: FC<ProductCardProps> = ({product}) => {
    const images = useMemo(
        () => (product.images?.length ? product.images : ["/favicon.svg"]),
        [product.images]
    );
    const [currentImage, setCurrentImage] = useState(0);
    const [isHovered, setIsHovered] = useState(false);
    const imageSrc = images[currentImage] ?? images[0];
    useEffect(() => {
        let interval:any
        if(isHovered){
            interval = setInterval(() => {
                setCurrentImage((prev) => (prev + 1) % images.length);
            }, 3000);
        }
        return () => {
            clearInterval(interval);
        };
    }, [isHovered]);
    const navigate = useNavigate();
    return (<div  onClick={() => navigate(`/product-details/${product.categoryName}/${product.title}/${product.id}`)} className="group px-4 relative cursor-pointer">
        <div className="relative w-62.5 sm:w-full h-87.5 overflow-hidden rounded-md">
            <img
                src={imageSrc}
                alt={product.title}
                className="w-full h-full object-cover transition-transform duration-500 ease-in-out group-hover:scale-110"
            />

            <div className="absolute bottom-4 left-1/2 -translate-x-1/2 flex gap-2 z-10">
                {images.map((_, index) => (<button
                    key={index}
                    onClick={(e) => {
                        e.stopPropagation();
                        setCurrentImage(index);
                    }}
                    className={`w-2.5 h-2.5 rounded-full transition-all duration-300 ${currentImage === index ? "bg-white" : "bg-white/50"}`}
                />))}
            </div>
        </div>

        <p className="mt-3 text-gray-700 text-sm font-medium line-clamp-2">{product.title}</p>

        <div className="flex items-center gap-2 flex-wrap">
            <span className="font-bold text-lg text-gray-600 ">{formatPrice(product.sellingPrice)}</span>
            {/*<span className="text-sm text-gray-400 line-through">*/}
            {/*    {formatPrice(product.sellingPrice)} MRP*/}
            {/*</span>*/}

            <span className="text-green-600 text-sm font-medium">
                {product.discountPercent}% Off
            </span>
        </div>
    </div>);
};

export default ProductCard;