import StarIcon from '@mui/icons-material/Star';
import type { Review } from "../../state/customer/ReviewSlice.ts";

interface ReviewSummaryProps {
    reviews: Review[];
}

const ReviewSummary = ({ reviews }: ReviewSummaryProps) => {
    const totalReviews = reviews.length;
    const averageRating = totalReviews > 0
        ? reviews.reduce((sum, r) => sum + r.rating, 0) / totalReviews
        : 0;

    const distribution = [5, 4, 3, 2, 1].map((star) => {
        const count = reviews.filter((r) => r.rating === star).length;
        const percent = totalReviews > 0 ? (count / totalReviews) * 100 : 0;
        return { star, count, percent };
    });

    if (totalReviews === 0) {
        return (
            <div className='py-4'>
                <p className='text-sm text-gray-500'>No reviews yet</p>
            </div>
        );
    }

    return (
        <div className='py-4 space-y-3'>
            <div className='flex items-center gap-2'>
                <span className='text-3xl font-bold text-gray-900'>{averageRating.toFixed(1)}</span>
                <div className='flex items-center gap-0.5'>
                    {[1, 2, 3, 4, 5].map((star) => (
                        <StarIcon
                            key={star}
                            sx={{ fontSize: '20px', color: star <= Math.round(averageRating) ? '#facc15' : '#e5e7eb' }}
                        />
                    ))}
                </div>
            </div>
            <p className='text-sm text-gray-500'>{totalReviews} review{totalReviews !== 1 ? 's' : ''}</p>
            <div className='space-y-1.5'>
                {distribution.map(({ star, count, percent }) => (
                    <div key={star} className='flex items-center gap-2'>
                        <span className='text-xs text-gray-600 w-10'>{star} star</span>
                        <div className='flex-1 h-2 bg-gray-100 rounded-full overflow-hidden'>
                            <div className='h-full bg-yellow-400' style={{ width: `${percent}%` }} />
                        </div>
                        <span className='text-xs text-gray-400 w-6 text-right'>{count}</span>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ReviewSummary;