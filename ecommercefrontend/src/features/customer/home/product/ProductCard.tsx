import {useState} from "react";

const images = ["https://m.media-amazon.com/images/I/61lS71dZsyL._SY879_.jpg", "https://m.media-amazon.com/images/I/71Fo1AOKzjL._SY879_.jpg", "https://m.media-amazon.com/images/I/61y7XfqxJuL._SY879_.jpg", "https://m.media-amazon.com/images/I/61lS71dZsyL._SY879_.jpg"];

const ProductCard = () => {
    const [currentImage, setCurrentImage] = useState(0);

    return (<div className="group px-4 relative">
        <div className="relative w-62.5 sm:w-full h-87.5 overflow-hidden rounded-md">
            <img
                src={images[currentImage]}
                alt="Product"
                className="w-full h-full object-cover transition-transform duration-500 ease-in-out group-hover:scale-110"
            />

            <div className="absolute bottom-4 left-1/2 -translate-x-1/2 flex gap-2 z-10">
                {images.map((_, index) => (<button
                    key={index}
                    onClick={() => setCurrentImage(index)}
                    className={`w-2.5 h-2.5 rounded-full transition-all duration-300 ${currentImage === index ? "bg-white" : "bg-white/50"}`}
                />))}
            </div>
        </div>

        <div className="flex items-center gap-2 flex-wrap">
            <span className="font-bold text-lg text-gray-600 ">
         ₹4,499
    </span>
            <span className="text-sm text-gray-400 line-through">
        ₹5,499 MRP
    </span>

            <span className="text-green-600 text-sm font-medium">
        40% Off
    </span>
        </div>
    </div>);
};

export default ProductCard;