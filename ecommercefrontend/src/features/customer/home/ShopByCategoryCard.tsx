import { useState } from 'react';

interface CategoryCardProps {
    label: string;
    count: number;
    discount: string;
    img: string;
    alt: string;
}

const categories: CategoryCardProps[] = [
    {
        label: "Fragrance",
        count: 48,
        discount: "20%",
        alt: "Fragrance",
        img: "https://images.unsplash.com/photo-1541643600914-78b084683702?w=400&fit=crop&auto=format",
    },
    {
        label: "Kitchen & Table",
        count: 124,
        discount: "15%",
        alt: "Kitchen and Table",
        img: "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400&fit=crop&auto=format",
    },
    {
        label: "Electronics",
        count: 89,
        discount: "30%",
        alt: "Electronics",
        img: "https://images.unsplash.com/photo-1498049794561-7780e7231661?w=400&fit=crop&auto=format",
    },
    {
        label: "Fashion",
        count: 210,
        discount: "25%",
        alt: "Fashion",
        img: "https://images.unsplash.com/photo-1445205170230-053b83016050?w=400&fit=crop&auto=format",
    },
    {
        label: "Beauty & Skincare",
        count: 95,
        discount: "18%",
        alt: "Beauty and Skincare",
        img: "https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400&fit=crop&auto=format",
    },
    {
        label: "Sports & Fitness",
        count: 76,
        discount: "22%",
        alt: "Sports and Fitness",
        img: "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400&fit=crop&auto=format",
    },
    {
        label: "Home Decor",
        count: 143,
        discount: "10%",
        alt: "Home Decor",
        img: "https://images.unsplash.com/photo-1484101403633-562f891dc89a?w=400&fit=crop&auto=format",
    },
    {
        label: "Books & Stationery",
        count: 200,
        discount: "12%",
        alt: "Books and Stationery",
        img: "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?w=400&fit=crop&auto=format",
    },
    {
        label: "Toys & Kids",
        count: 60,
        discount: "35%",
        alt: "Toys and Kids",
        img: "https://images.unsplash.com/photo-1558060370-d644479cb6f7?w=400&fit=crop&auto=format",
    },
    {
        label: "Footwear",
        count: 112,
        discount: "20%",
        alt: "Footwear",
        img: "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&fit=crop&auto=format",
    },
    {
        label: "Grocery & Food",
        count: 320,
        discount: "8%",
        alt: "Grocery and Food",
        img: "https://images.unsplash.com/photo-1542838132-92c53300491e?w=400&fit=crop&auto=format",
    },
    {
        label: "Jewellery & Watches",
        count: 55,
        discount: "28%",
        alt: "Jewellery and Watches",
        img: "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&fit=crop&auto=format",
    },
];

const ShopByCategoryCard = ({ label, count, discount, img, alt }: CategoryCardProps) => {
    const [imgError, setImgError] = useState(false);

    return (
        <div className="rounded-2xl overflow-hidden border border-gray-100 bg-white cursor-pointer group shadow-sm hover:shadow-md transition-shadow duration-300">
            <div className="overflow-hidden h-36 bg-gray-100">
                {imgError ? (
                    <div className="w-full h-full flex items-center justify-center bg-blue-50">
                        <span className="text-blue-900 text-xs font-semibold text-center px-2">{label}</span>
                    </div>
                ) : (
                    <img
                        src={img}
                        alt={alt}
                        className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                        onError={() => setImgError(true)}
                    />
                )}
            </div>
            <div className="px-3 pb-3 pt-2">
                <p className="text-sm font-semibold text-gray-900">{label}</p>
                <p className="text-xs text-gray-400">{count} products</p>
                <span className="inline-block mt-1.5 bg-blue-50 text-blue-900 text-[10px] font-semibold px-2 py-0.5 rounded-full">
                    Up to {discount} off
                </span>
            </div>
        </div>
    );
};

const ShopByCategory = () => {
    return (
        <div className="py-6">
            <div className="flex justify-between items-baseline mb-4">
                <h2 className="text-lg font-bold text-gray-900">Shop by Category</h2>
                <a href="#" className="text-sm font-medium text-blue-900">See all →</a>
            </div>
            <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3">
                {categories.map((cat) => (
                    <ShopByCategoryCard key={cat.label} {...cat} />
                ))}
            </div>
        </div>
    );
};

export default ShopByCategory;
