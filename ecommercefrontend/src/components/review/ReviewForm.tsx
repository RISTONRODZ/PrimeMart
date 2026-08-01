import { useState, useEffect, useRef } from "react";
import { Button, Rating, TextField } from "@mui/material";
import { useAppDispatch, useAppSelector } from "../../state/hooks.ts";
import { createReview, updateReview, type Review } from "../../state/customer/ReviewSlice.ts";

interface ReviewFormProps {
    productId: number;
    editingReview?: Review | null;
    onCancelEdit?: () => void;
    onSuccess: () => void;
    onError: () => void;
}

const ReviewForm = ({ productId, editingReview, onCancelEdit, onSuccess, onError }: ReviewFormProps) => {
    const dispatch = useAppDispatch();
    const { jwt } = useAppSelector((state) => state.auth);
    const { actionLoading } = useAppSelector((state) => state.review);
    const [formData, setFormData] = useState({ reviewText: "", rating: 0 });
    const prevEditingReviewRef = useRef(editingReview);

    useEffect(() => {
        if (editingReview !== prevEditingReviewRef.current) {
            if (editingReview) {
                setFormData({ reviewText: editingReview.reviewText, rating: editingReview.rating });
            } else {
                setFormData({ reviewText: "", rating: 0 });
            }
            prevEditingReviewRef.current = editingReview;
        }
         
    }, [editingReview]);

    const handleSubmit = async () => {
        if (!jwt || !formData.rating || !formData.reviewText.trim()) return;
        try {
            if (editingReview) {
                await dispatch(updateReview({
                    reviewId: editingReview.id,
                    jwt,
                    request: {
                        reviewText: formData.reviewText.trim(),
                        reviewRating: formData.rating,
                        productImages: []
                    }
                })).unwrap();
            } else {
                await dispatch(createReview({
                    productId,
                    jwt,
                    request: {
                        reviewText: formData.reviewText.trim(),
                        reviewRating: formData.rating,
                        productImages: []
                    }
                })).unwrap();
            }
            setFormData({ reviewText: "", rating: 0 });
            onSuccess();
        } catch {
            onError();
        }
    };

    if (!jwt) {
        return (
            <p className='text-gray-500 text-sm mb-6'>Log in to write a review for this product.</p>
        );
    }

    return (
        <div className='mb-8 pb-8 border-b border-gray-100'>
            <h3 className='text-sm font-bold text-gray-800 uppercase tracking-wider mb-3'>
                {editingReview ? "Edit Your Review" : "Write a Review"}
            </h3>
            <Rating
                value={formData.rating}
                precision={0.5}
                onChange={(_, newValue) => setFormData({ ...formData, rating: newValue })}
            />
            <TextField
                fullWidth
                multiline
                minRows={3}
                placeholder="Share your experience with this product..."
                value={formData.reviewText}
                onChange={(e) => setFormData({ ...formData, reviewText: e.target.value })}
                sx={{ mt: 2 }}
            />
            <div className='flex gap-2 mt-2'>
                <Button
                    variant='contained'
                    sx={{ color: '#2b2b2b' }}
                    disabled={actionLoading || !formData.rating || !formData.reviewText.trim()}
                    onClick={handleSubmit}
                >
                    {actionLoading ? "Submitting..." : (editingReview ? "Update Review" : "Submit Review")}
                </Button>
                {editingReview && onCancelEdit && (
                    <Button
                        variant='outlined'
                        onClick={onCancelEdit}
                        disabled={actionLoading}
                    >
                        Cancel
                    </Button>
                )}
            </div>
        </div>
    );
};

export default ReviewForm;