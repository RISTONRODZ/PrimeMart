// types.ts
// export interface HomeData {
//     id: string;
//     title: string;
//     description: string;
//     imageUrl: string;
//   }

interface Deal{
    id?: number;
    category:HomeCategory;
    discount:number;
    image: string;
    title: string;
    description: string;
}

export interface HomeData {
    id: number;
    grid: HomeCategory[];
    shopByCategories: HomeCategory[];
    electricCategories: HomeCategory[];
    deals: Deal[];
    dealCategories:HomeCategory[];
}

export interface HomeCategory {
    id?:number;
    categoryId: string;
    section?: string;
    name?: string;
    imageUrl: string;
    parentCategoryId?: string;
}
  