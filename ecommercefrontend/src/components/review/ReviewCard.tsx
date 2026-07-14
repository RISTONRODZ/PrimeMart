import StarIcon from '@mui/icons-material/Star';
import DeleteOutlineOutlinedIcon from '@mui/icons-material/DeleteOutlineOutlined';
import EditIcon from '@mui/icons-material/Edit';
import { IconButton } from "@mui/material";
import { useAppSelector } from "../../state/hooks.ts";
import type {Review} from "../../state/customer/ReviewSlice.ts";

interface ReviewCardProps {
    review: Review;
    onDelete: (id: number) => void;
    onEdit: (review: Review) => void;
}

const ReviewCard = ({ review, onDelete, onEdit }: ReviewCardProps) => {
    const { user } = useAppSelector((state) => state.auth);
    const isOwner = user?.id === review.user.id;

    const formattedDate = new Date(review.createdAt).toLocaleDateString("en-IN", {
        year: "numeric",
        month: "short",
        day: "numeric",
    });

    return (
        <div className='py-6 border-b border-gray-100 flex gap-4'>
            <img
                src={`https://ui-avatars.com/api/?name=${encodeURIComponent(review.user.fullName)}`}
                alt={review.user.fullName}
                className='w-10 h-10 rounded-full flex-shrink-0'
            />
            <div className='flex-1'>
                <div className='flex items-center justify-between'>
                    <div>
                        <p className='font-semibold text-gray-800'>{review.user.fullName}</p>
                        <div className='flex items-center gap-2 mt-0.5'>
                            <div className='flex items-center gap-0.5 bg-blue-50 text-blue-700 px-1.5 py-0.5 rounded text-xs font-semibold'>
                                <span>{review.rating}</span>
                                <StarIcon sx={{ fontSize: '14px' }} />
                            </div>
                            <span className='text-xs text-gray-400'>{formattedDate}</span>
                        </div>
                    </div>
                    {isOwner && (
                        <div className="flex gap-1">
                            <IconButton size="small" onClick={() => onEdit(review)}>
                                <EditIcon fontSize="small" sx={{ color: '#9ca3af' }} />
                            </IconButton>
                            <IconButton size="small" onClick={() => onDelete(review.id)}>
                                <DeleteOutlineOutlinedIcon fontSize="small" sx={{ color: '#9ca3af' }} />
                            </IconButton>
                        </div>
                    )}
                </div>
                <p className='text-gray-600 text-sm mt-2 leading-relaxed'>{review.reviewText}</p>
                {review.productImages?.length > 0 && (
                    <div className='flex gap-2 mt-3'>
                        {review.productImages.map((img, index) => (
                            <img
                                key={index}
                                src={img}
                                alt={`Review photo ${index + 1}`}
                                className='w-16 h-16 object-cover rounded-lg border border-gray-200'
                            />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default ReviewCard;