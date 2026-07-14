import { useEffect } from 'react'
import WishlistProductCard from './WishlistProductCard';
import { useAppDispatch, useAppSelector } from "../../state/hooks.ts";
import { getWishlistByUserId } from "../../state/customer/WishlistSlice.ts";

const Wishlist = () => {
    const dispatch = useAppDispatch();
    const { wishlist, loading, error } = useAppSelector(store => store.wishlist)

    useEffect(() => {
        dispatch(getWishlistByUserId());
    }, [dispatch]);

    if (loading) return <div className="h-[85vh] flex justify-center items-center">Loading...</div>;
    if (error) return <div className="h-[85vh] flex justify-center items-center text-red-600">Error: {error}</div>;

    return (
        <div className="min-h-[85vh] p-4 sm:p-6 lg:p-20">
            {wishlist?.products?.length ? (
                <section>
                    <div className="flex flex-wrap items-baseline gap-2">
                        <h1 className="text-xl sm:text-2xl font-bold text-gray-900">My Wishlist</h1>
                        <span className="text-sm sm:text-base text-gray-500">
                            {wishlist.products.length} {wishlist.products.length === 1 ? "item" : "items"}
                        </span>
                    </div>

                    <div className="pt-6 sm:pt-10 grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4 sm:gap-6">
                        {wishlist.products.map((item) => (
                            <WishlistProductCard key={item.id} item={item} />
                        ))}
                    </div>
                </section>
            ) : (
                <div className="h-[70vh] flex justify-center items-center flex-col">
                    <div className="text-center py-5 px-4">
                        <h1 className="text-lg font-medium text-gray-800">Hey, it feels so light!</h1>
                        <p className="text-gray-500 text-sm mt-1">
                            There's nothing in your wishlist yet — let's add some items.
                        </p>
                    </div>
                </div>
            )}
        </div>
    )
}
export default Wishlist