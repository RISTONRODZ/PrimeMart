

export interface Category {
    id?: number;
    name: string;
    categoryId: string;
    parentCategory?: Category;
    level: number;
}

export interface Product {
    id: number;
    title: string;
    description: string;
    sellingPrice: number;
    mrpPrice: number;
    discountPercent: number;
    color: string;
    images: string[];
    categoryName: string;
    sellerName: string;
    sizes: string;
    numRatings: number;
    quantity: number;
    rating?: number;
    brand?: string;
}