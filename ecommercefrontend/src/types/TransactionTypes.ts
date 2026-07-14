// import type {User} from "./UserTypes.ts";
// import type {Order} from "./OrderTypes.ts";
// import type {Seller} from "./SellerTypes.ts";


export interface Transaction {
    id: number;
    orderId: number;
    orderTrackingId: string;
    sellerId: number;
    sellerName: string;
    totalSellingPrice: number;
    paymentStatus: string;
    date: string;
}