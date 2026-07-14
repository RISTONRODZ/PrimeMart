import type {Address, User} from "./UserTypes.ts";
import type {Product} from "./ProductTypes.ts";

export interface OrderState {
    orders: Order[];
    orderItem:OrderItem | null;
    currentOrder: Order | null;
    paymentOrder: any | null;
    loading: boolean;
    error: string | null;
    orderCanceled: boolean
}

export interface Order {
    id: number;
    orderId: string;
    user: User;
    sellerId: number;
    orderItems: OrderItem[];
    orderDate: string;
    shippingAddress: Address;
    paymentDetails: any;
    totalMrpPrice: number;
    totalSellingPrice?: number; // Optional field
    discount?: number; // Optional field
    orderStatus: OrderStatus;
    totalItem: number;
    deliverDate:string;
}

export const OrderStatus = {
    PENDING :"PENDING",
    PLACED : "PLACED",
    CONFIRMED : "CONFIRMED",
    SHIPPED : "SHIPPED",
    DELIVERED : "DELIVERED",
    CANCELED : "CANCELED",
} as const;
export type OrderStatus = typeof OrderStatus[keyof typeof OrderStatus];
export interface OrderItem {
    id: number;
    productId: number;
    productTitle: string;
    productImage: string;
    color: string;
    size: string;
    quantity: number;
    mrpPrice: number;
    sellingPrice: number;
    userId?: number;
    product: Product;
}