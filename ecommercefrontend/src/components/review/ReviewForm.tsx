import { useState } from "react";
import { Button, Rating, TextField } from "@mui/material";
import { useAppDispatch, useAppSelector } from "../../state/hooks.ts";
import { createReview } from "../../state/customer/ReviewSlice.ts";

interface ReviewFormProps {
    productId: number;
    onSuccess: () => void;
    onError: () => void;
}

const ReviewForm = ({ productId, onSuccess, onError }: ReviewFormProps) => {
    const dispatch = useAppDispatch();
    const { jwt } = useAppSelector((state) => state.auth);
    const { actionLoading } = useAppSelector((state) => state.review);
    const [reviewText, setReviewText] = useState("");
    const [rating, setRating] = useState<number | null>(0);

    const handleSubmit = async () => {
        if (!jwt || !rating || !reviewText.trim()) return;
        try {
            await dispatch(createReview({
                productId,
                jwt,
                request: {
                    reviewText: reviewText.trim(),
                    reviewRating: rating,
                    productImages: []
                }
            })).unwrap();
            setReviewText("");
            setRating(0);
            onSuccess();
        } catch (error) {
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
            <h3 className='text-sm font-bold text-gray-800 uppercase tracking-wider mb-3'>Write a Review</h3>
            <Rating
                value={rating}
                precision={0.5}
                onChange={(_, newValue) => setRating(newValue)}
            />
            <TextField
                fullWidth
                multiline
                minRows={3}
                placeholder="Share your experience with this product..."
                value={reviewText}
                onChange={(e) => setReviewText(e.target.value)}
                sx={{ mt: 2 }}
            />
            <Button
                variant='contained'
                sx={{ mt: 2, color: '#2b2b2b' }}
                disabled={actionLoading || !rating || !reviewText.trim()}
                onClick={handleSubmit}
            >
                {actionLoading ? "Submitting..." : "Submit Review"}
            </Button>
        </div>
    );
};

export default ReviewForm;