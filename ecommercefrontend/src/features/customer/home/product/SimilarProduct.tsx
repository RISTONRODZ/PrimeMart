import SimilarProductCard from "./SimilarProductCard.tsx";

const similarProducts = [
    {
        id: 1,
        brand: "Ro's Clothing",
        name: "Men Blue Casual Shirt",
        price: 350,
        originalPrice: 450,
        rating: 4.2,
        reviewCount: 156,
        images: ["https://i.pinimg.com/1200x/b1/45/ef/b145efb4a2a6413b25fa421cbf55754c.jpg"],
        badge: "Sale" as const,
        variant: "solid" as const,
    },
    {
        id: 2,
        brand: "Ro's Clothing",
        name: "Men White Formal Shirt",
        price: 420,
        originalPrice: 520,
        rating: 4.5,
        reviewCount: 203,
        images: ["https://i.pinimg.com/1200x/b1/45/ef/b145efb4a2a6413b25fa421cbf55754c.jpg"],
        badge: "New" as const,
        variant: "outline" as const,
    },
    {
        id: 3,
        brand: "Ro's Clothing",
        name: "Men Black Polo T-Shirt",
        price: 280,
        originalPrice: 350,
        rating: 4.0,
        reviewCount: 89,
        images: ["https://i.pinimg.com/1200x/b1/45/ef/b145efb4a2a6413b25fa421cbf55754c.jpg"],
        badge: "Hot" as const,
        variant: "solid" as const,
    },
    {
        id: 4,
        brand: "Ro's Clothing",
        name: "Men Grey Checkered Shirt",
        price: 380,
        originalPrice: 480,
        rating: 4.3,
        reviewCount: 124,
        images: ["https://i.pinimg.com/1200x/b1/45/ef/b145efb4a2a6413b25fa421cbf55754c.jpg"],
        variant: "solid" as const,
    },
];

const SimilarProduct = () => {
    return (
        <div className="mt-12">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">Similar Products</h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                {similarProducts.map((product) => (
                    <SimilarProductCard key={product.id} product={product} />
                ))}
            </div>
        </div>
    );
};

export default SimilarProduct;