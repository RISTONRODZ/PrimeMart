import { useEffect, useState, useRef } from "react";
import { useParams } from "react-router-dom";
import StarIcon from '@mui/icons-material/Star';
import { Button, Divider, IconButton, Snackbar, Alert } from "@mui/material";
import { AddShoppingCart, FavoriteBorder, LocalShipping, Shield, WorkspacePremium } from "@mui/icons-material";
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import SimilarProduct from "./SimilarProduct.tsx";
import ReviewCard from "../../../../components/review/ReviewCard.tsx";
import { useAppDispatch, useAppSelector } from "../../../../state/hooks.ts";
import { fetchProductById } from "../../../../state/customer/ProductSlice.ts";
import { addProductToWishlist } from "../../../../state/customer/WishlistSlice.ts";
import { addItemToCart } from "../../../../state/customer/CartSlice.ts";
import { fetchReviewsByProductId, deleteReview, updateReview, type Review} from "../../../../state/customer/ReviewSlice.ts";
import ReviewForm from "../../../../components/review/ReviewForm.tsx";

const ProductDetails = () => {
    const {productId} = useParams();
    const dispatch = useAppDispatch();
    const {product, loading, error} = useAppSelector((state) => state.product);
    const {jwt, user} = useAppSelector((state) => state.auth);
    const {reviews, loading: reviewsLoading, error: reviewsError} = useAppSelector((state) => state.review);

    const [activeImage, setActiveImage] = useState("");
    const [quantity, setQuantity] = useState(1);
    const [visibleCount, setVisibleCount] = useState(3);
    const [editingReview, setEditingReview] = useState<Review | null>(null);
    const [snackbar, setSnackbar] = useState<{ open: boolean; message: string; severity: 'success' | 'error' }>({
        open: false,
        message: '',
        severity: 'success'
    });

    // Check if the current logged-in user already has a review for this product
    const myReview = user ? reviews.find((r) => r.user.id === user.id) : undefined;

    const handleWishlist = async () => {
        if (!product) return;
        try {
            await dispatch(addProductToWishlist({productId: product.id})).unwrap();
            setSnackbar({open: true, message: 'Added to wishlist successfully!', severity: 'success'});
        } catch (error) {
            setSnackbar({open: true, message: 'Failed to add to wishlist', severity: 'error'});
        }
    }

    const handleAddToCart = async () => {
        if (!product) return;
        try {
            await dispatch(addItemToCart({
                jwt,
                request: {
                    productId: product.id,
                    size: "M",
                    quantity
                }
            })).unwrap();
            setSnackbar({open: true, message: 'Added to cart successfully!', severity: 'success'});
        } catch (error) {
            setSnackbar({open: true, message: 'Failed to add to cart', severity: 'error'});
        }
    }

    const isInitialized = useRef(false);

    useEffect(() => {
        if (productId) {
            dispatch(fetchProductById(Number(productId)));
            dispatch(fetchReviewsByProductId(Number(productId)));
        }
    }, [dispatch, productId]);

    useEffect(() => {
        if (product && product.images?.length && !isInitialized.current) {
            setActiveImage(product.images[0]);
            isInitialized.current = true;
        }
    }, [product]);

    const handleDeleteReview = async (id: number) => {
        if (!jwt) {
            setSnackbar({open: true, message: 'Please log in to delete a review', severity: 'error'});
            return;
        }
        try {
            await dispatch(deleteReview({reviewId: id, jwt})).unwrap();
            setSnackbar({open: true, message: 'Review deleted successfully!', severity: 'success'});
            if (editingReview?.id === id) {
                setEditingReview(null);
            }
        } catch (error) {
            setSnackbar({open: true, message: 'Failed to delete review', severity: 'error'});
        }
    };

    const handleIncrement = () => setQuantity(prev => prev + 1);
    const handleDecrement = () => setQuantity(prev => prev > 1 ? prev - 1 : 1);

    if (loading) return <div className="text-center py-20">Loading...</div>;
    if (error) return <div className="text-center py-20 text-red-600">Error: {error}</div>;
    if (!product) return null;

    return (
        <div className='max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10'>
            <div className='grid grid-cols-1 lg:grid-cols-12 gap-12 lg:gap-16 items-start'>
                <section className='lg:col-span-7 flex flex-col-reverse md:flex-row gap-4 w-full'>
                    <div className='flex flex-row md:flex-col gap-3 overflow-x-auto md:overflow-visible w-full md:w-[15%] scrollbar-none'>
                        {product.images?.map((img, index) => (
                            <img
                                key={index}
                                onClick={() => setActiveImage(img)}
                                className={`w-20 h-20 md:w-full md:h-24 object-cover cursor-pointer rounded-lg border-2 transition-all duration-200 ${
                                    activeImage === img ? 'border-blue-600 scale-[1.02]' : 'border-transparent hover:border-gray-300'
                                }`}
                                src={img}
                                alt={`Product thumbnail ${index + 1}`}
                            />
                        ))}
                    </div>
                    <div className='w-full md:w-[85%]'>
                        <img
                            className='w-full h-auto object-cover rounded-xl shadow-sm border border-gray-100'
                            src={activeImage}
                            alt="Main product"
                        />
                    </div>
                </section>
                <section className='lg:col-span-5 flex flex-col justify-between h-full'>
                    <div>
                        <h1 className='font-bold text-2xl md:text-3xl text-blue-700 tracking-wide'>
                            {product.brand}
                        </h1>
                        <p className='text-gray-500 font-medium text-lg mt-1 capitalize'>
                            {product.title}
                        </p>
                        <div className='flex items-center gap-3 py-1.5 border border-gray-200 rounded-lg w-fit px-3 mt-4 bg-gray-50/50'>
                            <div className='flex gap-1 items-center font-semibold text-gray-700'>
                                <span>{product.rating || 4.5}</span>
                                <StarIcon sx={{color: '#1447e6', fontSize: "18px"}}/>
                            </div>
                            <Divider orientation='vertical' flexItem/>
                            <span className='text-sm text-gray-500 font-medium'>
                                {product.numRatings || 0} Ratings
                            </span>
                        </div>
                        <div className='mt-6 flex flex-wrap items-baseline gap-3 border-b border-gray-100 pb-6'>
                            <span className='text-3xl font-bold text-gray-900'>
                                &#x20B9;{product.sellingPrice}
                            </span>
                            <span className='text-green-600 font-semibold text-lg bg-green-50 px-2 py-0.5 rounded'>
                                {product.discountPercent}% off
                            </span>
                        </div>
                        <div className='mt-6 space-y-3.5 text-gray-600 text-sm font-medium'>
                            <div className='flex items-center gap-3.5'>
                                <Shield sx={{color: '#1447e6', fontSize: 22}}/>
                                <p>Authentic & Quality Assured</p>
                            </div>
                            <div className='flex items-center gap-3.5'>
                                <WorkspacePremium sx={{color: '#1447e6', fontSize: 22}}/>
                                <p>Free Shipping</p>
                            </div>
                            <div className='flex items-center gap-3.5'>
                                <LocalShipping sx={{color: '#1447e6', fontSize: 22}}/>
                                <p>Fast delivery</p>
                            </div>
                        </div>
                        <div className='mt-8 pt-6 border-t border-gray-100'>
                            <h3 className='text-sm font-bold text-gray-800 uppercase tracking-wider mb-3'>Quantity</h3>
                            <div className='flex items-center border border-gray-300 rounded-lg w-fit bg-white shadow-sm'>
                                <IconButton onClick={handleDecrement} size="small"><RemoveIcon/></IconButton>
                                <span className='w-12 text-center font-semibold'>{quantity}</span>
                                <IconButton onClick={handleIncrement} size="small"><AddIcon/></IconButton>
                            </div>
                        </div>
                    </div>
                    <div className='mt-8 space-y-4'>
                        <div className='flex gap-4'>
                            <Button onClick={handleAddToCart} variant='contained' startIcon={<AddShoppingCart/>}
                                    fullWidth sx={{py: 1.5, color: '#2b2b2b'}}>Add to Cart</Button>
                            <Button onClick={handleWishlist} variant='outlined' startIcon={<FavoriteBorder/>} fullWidth
                                    sx={{py: 1.5}}>Wishlist</Button>
                        </div>
                        <div className='pt-6 border-t border-gray-100 text-gray-600 text-sm leading-relaxed'>
                            <p>{product.description}</p>
                        </div>
                    </div>
                </section>
            </div>

            <div className='mt-16 border-t border-gray-100 pt-10'>
                <h2 className='text-2xl font-bold text-gray-800 mb-6'>Customer Reviews</h2>
                <div className='max-w-3xl'>

                    {/* CONDITIONAL RENDERING: Review Banner vs Review Form */}
                    {!editingReview && myReview ? (
                        <div className="mb-8 flex flex-col sm:flex-row items-start sm:items-center justify-between bg-blue-50/50 border border-blue-100 rounded-xl py-3 px-5">
                            <span className="text-sm font-medium text-gray-800 mb-3 sm:mb-0">
                                You have already reviewed this product
                            </span>
                            <div className="flex items-center gap-2">
                                <IconButton
                                    size="small"
                                    onClick={() => setEditingReview(myReview)}
                                    aria-label="Edit your review"
                                    sx={{ backgroundColor: 'white', border: '1px solid #e5e7eb', '&:hover': { backgroundColor: '#f3f4f6' } }}
                                >
                                    <EditIcon fontSize="small" sx={{ color: '#2563eb' }} />
                                </IconButton>
                                <IconButton
                                    size="small"
                                    onClick={() => handleDeleteReview(myReview.id)}
                                    aria-label="Delete your review"
                                    sx={{ backgroundColor: 'white', border: '1px solid #e5e7eb', '&:hover': { backgroundColor: '#fee2e2' } }}
                                >
                                    <DeleteIcon fontSize="small" sx={{ color: '#dc2626' }} />
                                </IconButton>
                            </div>
                        </div>
                    ) : (
                        <ReviewForm
                            productId={Number(productId)}
                            editingReview={editingReview}
                            onCancelEdit={() => setEditingReview(null)}
                            onSuccess={() => {
                                setSnackbar({
                                    open: true,
                                    message: editingReview ? 'Review updated successfully!' : 'Review submitted successfully!',
                                    severity: 'success'
                                });
                                setEditingReview(null);
                            }}
                            onError={() => setSnackbar({open: true, message: editingReview ? 'Failed to update review' : 'Failed to submit review', severity: 'error'})}
                        />
                    )}

                    {reviewsLoading && <p className='text-gray-500 text-sm my-4'>Loading reviews...</p>}
                    {reviewsError && <p className='text-red-600 text-sm my-4'>{reviewsError}</p>}

                    {!reviewsLoading && reviews.length === 0 && (
                        <p className='text-gray-500 text-sm my-4'>No reviews yet. Be the first to review this product!</p>
                    )}

                    <div className="mt-6 space-y-4">
                        {reviews.slice(0, visibleCount).map((review) => (
                            <ReviewCard
                                key={review.id}
                                review={review}
                                onDelete={handleDeleteReview}
                                onEdit={() => setEditingReview(review)}
                            />
                        ))}
                    </div>

                    {reviews.length > 3 && (
                        <div className="mt-4">
                            <Button
                                variant="text"
                                onClick={() => setVisibleCount(visibleCount === 3 ? reviews.length : 3)}
                                sx={{ textTransform: 'none', fontWeight: 600 }}
                            >
                                {visibleCount === 3 ? "View More Reviews" : "View Less"}
                            </Button>
                        </div>
                    )}
                </div>
            </div>

            <Snackbar
                open={snackbar.open}
                autoHideDuration={3000}
                onClose={() => setSnackbar({ ...snackbar, open: false })}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
            >
                <Alert
                    onClose={() => setSnackbar({ ...snackbar, open: false })}
                    severity={snackbar.severity}
                    variant="filled"
                >
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </div>
    );
};

export default ProductDetails;