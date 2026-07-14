import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { IconButton } from "@mui/material";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../state/hooks.ts";
import { fetchProductById } from "../../state/customer/ProductSlice.ts";
import { fetchReviewsByProductId, deleteReview, createReview, updateReview, clearReviewActionError } from "../../state/customer/ReviewSlice.ts";
import type { Review as ReviewType } from "../../state/customer/ReviewSlice.ts";
import ReviewCard from "./ReviewCard.tsx";
import ReviewSummary from "./ReviewSummary.tsx";
import {fetchUserProfile} from "../../state/slice/AuthSlice.ts";

const formatPrice = (value: number) => `₹${value.toLocaleString("en-IN")}`;

const Review = () => {
    const { productId } = useParams<{ productId: string }>();
    const dispatch = useAppDispatch();
    const { product } = useAppSelector((state) => state.product);
    const { reviews, actionLoading, actionError } = useAppSelector((state) => state.review);
    const { user, jwt, isAuthenticated } = useAppSelector((state) => state.auth);

    const myReview = user ? reviews.find((r) => r.user.id === user.id) : undefined;

    const [showReviewForm, setShowReviewForm] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);
    const [editingReviewId, setEditingReviewId] = useState<number | null>(null);
    const [reviewText, setReviewText] = useState("");
    const [rating, setRating] = useState(5);
    const [productImages, setProductImages] = useState<string[]>([]);

    useEffect(() => {
        if (productId) {
            dispatch(fetchProductById(Number(productId)));
            dispatch(fetchReviewsByProductId(Number(productId)));
        }
    }, [productId, dispatch]);

    useEffect(() => {
        if (isAuthenticated && !user) {
            dispatch(fetchUserProfile());
        }
    }, [isAuthenticated, user, dispatch]);

    const resetForm = () => {
        setReviewText("");
        setRating(5);
        setProductImages([]);
        setShowReviewForm(false);
        setIsEditMode(false);
        setEditingReviewId(null);
        dispatch(clearReviewActionError());
    };

    const handleSubmitReview = () => {
        if (!jwt || !productId) return;

        const targetReviewId = editingReviewId ?? myReview?.id;

        if (isEditMode || targetReviewId) {
            dispatch(updateReview({
                reviewId: targetReviewId as number,
                jwt,
                request: { reviewText, reviewRating: rating, productImages }
            }));
        } else {
            dispatch(createReview({
                productId: Number(productId),
                jwt,
                request: { reviewText, reviewRating: rating, productImages }
            }));
        }

        resetForm();
    };

    const handleEditReview = (review: ReviewType) => {
        setReviewText(review.reviewText);
        setRating(review.rating);
        setProductImages(review.productImages || []);
        setEditingReviewId(review.id);
        setIsEditMode(true);
        setShowReviewForm(true);
    };

    const handleDeleteReview = (reviewId: number) => {
        if (jwt) {
            dispatch(deleteReview({ reviewId, jwt }));
        }
    };

    if (!product) {
        return <div className="flex justify-center items-center min-h-screen">Loading...</div>;
    }

    const images = product.images?.length ? product.images : ["/favicon.svg"];

    return (
        <div className={'p-5 lg:px-20 flex flex-col lg:flex-row gap-20'}>
            <section className={'w-full md:w-1/2 lg:w-[30%] space-y-2'}>
                <img src={images[0]} alt={product.title} className='w-full rounded-lg object-cover' />
                <div>
                    <div>
                        <p className={'font-bold text-xl'}>{product.brand || product.title}</p>
                        <p className={'text-lg text-gray-600'}>{product.title}</p>
                    </div>
                    <div className="flex items-center gap-2 flex-wrap">
                        <span className="font-bold text-lg text-gray-600">
                            {formatPrice(product.sellingPrice)}
                        </span>
                        {/*<span className="text-sm text-gray-400 line-through">*/}
                        {/* {formatPrice(product.mrp || product.sellingPrice)} MRP*/}
                        {/*</span>*/}
                        {product.discountPercent && (
                            <span className="text-green-600 text-sm font-medium">
                                {product.discountPercent}% Off
                            </span>
                        )}
                    </div>
                </div>
                <ReviewSummary reviews={reviews} />
            </section>

            <section className={'flex-1'}>
                {!showReviewForm && !myReview && (
                    <button
                        onClick={() => {
                            if (!isAuthenticated) {
                                alert("Please login to write a review");
                                return;
                            }
                            setShowReviewForm(true);
                        }}
                        className="mb-6 bg-blue-600 text-white py-2 px-6 rounded-lg font-semibold hover:bg-blue-700 transition-colors"
                    >
                        Write a Review
                    </button>
                )}

                {!showReviewForm && myReview && (
                    <div className="mb-6 flex items-center justify-between bg-gray-50 border border-gray-200 rounded-lg py-2 px-4">
                        <span className="text-sm text-gray-600">You've already reviewed this product</span>
                        <div className="flex items-center gap-1">
                            <IconButton
                                size="small"
                                onClick={() => handleEditReview(myReview)}
                                aria-label="Edit your review"
                            >
                                <EditIcon fontSize="small" sx={{ color: '#2563eb' }} />
                            </IconButton>
                            <IconButton
                                size="small"
                                onClick={() => handleDeleteReview(myReview.id)}
                                aria-label="Delete your review"
                            >
                                <DeleteIcon fontSize="small" sx={{ color: '#dc2626' }} />
                            </IconButton>
                        </div>
                    </div>
                )}

                {actionError && (
                    <div className="mb-4 text-sm text-red-600 bg-red-50 border border-red-100 rounded-lg px-4 py-2">
                        {actionError}
                    </div>
                )}

                {showReviewForm && (
                    <div className="bg-gray-50 p-6 rounded-lg mb-6">
                        <h3 className="text-lg font-semibold mb-4">
                            {isEditMode ? "Edit Your Review" : "Write Your Review"}
                        </h3>

                        <div className="mb-4">
                            <label className="block text-sm font-medium text-gray-700 mb-2">Rating</label>
                            <div className="flex gap-2">
                                {[1, 2, 3, 4, 5].map((star) => (
                                    <button
                                        key={star}
                                        onClick={() => setRating(star)}
                                        className={`text-2xl ${star <= rating ? 'text-yellow-400' : 'text-gray-300'}`}
                                    >
                                        ★
                                    </button>
                                ))}
                            </div>
                        </div>

                        <div className="mb-4">
                            <label className="block text-sm font-medium text-gray-700 mb-2">Your Review</label>
                            <textarea
                                value={reviewText}
                                onChange={(e) => setReviewText(e.target.value)}
                                rows={4}
                                className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                placeholder="Share your experience with this product..."
                            />
                        </div>

                        <div className="flex gap-3">
                            <button
                                onClick={handleSubmitReview}
                                disabled={actionLoading || !reviewText.trim()}
                                className="bg-blue-600 text-white py-2 px-6 rounded-lg font-semibold hover:bg-blue-700 transition-colors disabled:bg-gray-400 disabled:cursor-not-allowed"
                            >
                                {actionLoading ? "Submitting..." : (isEditMode ? "Update Review" : "Submit Review")}
                            </button>
                            <button
                                onClick={resetForm}
                                className="border border-gray-300 text-gray-700 py-2 px-6 rounded-lg font-semibold hover:bg-gray-100 transition-colors"
                            >
                                Cancel
                            </button>
                        </div>
                    </div>
                )}

                {reviews.length > 0 ? (
                    reviews.map((review) => (
                        <ReviewCard
                            key={review.id}
                            review={review}
                            onDelete={handleDeleteReview}
                            onEdit={handleEditReview}
                        />
                    ))
                ) : (
                    <div className="text-gray-500 py-8">No reviews yet. Be the first to review this product!</div>
                )}
            </section>
        </div>
    );
};

export default Review;