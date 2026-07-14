import type {HomeCategory} from "./HomeCategory.ts";


export interface Deal{
    id?: number;
    discount:number;
    homeCategoryId: number;
    category?: HomeCategory;
    homeCategory?: HomeCategory;
    image?: string;
    title?: string;
    description?: string;
}

export interface ApiResponse {
    message: string;
    status: boolean;
}

export interface DealsState {
    deals: Deal[];
    loading: boolean;
    error: string | null;
    dealCreated:boolean,
    dealUpdated:boolean,
}
