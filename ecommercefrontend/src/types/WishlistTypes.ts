import type {User} from "./UserTypes.ts";
import type {Product} from "./ProductTypes.ts";


export interface Wishlist {
    id: number;
    user: User;
    products: Product[];
}

export interface WishlistState {
    wishlist: Wishlist | null;
    loading: boolean;
    updating: boolean;
    error: string | null;
}
// Payload interfaces for async thunks
export interface AddProductToWishlistPayload {
    wishlistId: number;
    productId: number;
}