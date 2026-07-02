import { Box, Typography, Avatar, Rating, IconButton } from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";

export interface Review {
    id: number;
    userName: string;
    avatarUrl: string;
    rating: number;
    date: string;
    reviewText: string;
    photoUrl?: string;
}

interface ReviewCardProps {
    review: Review;
    onDelete?: (id: number) => void;
}

const ReviewCard = ({ review, onDelete }: ReviewCardProps) => {
    return (
        <Box className={'text-slate-800'} sx={{
            p: { xs: 1.5, sm: 2 },
            borderBottom: "1px solid #e0e0e0",
            mb: 2
        }}>
            <Box sx={{ display: "flex", alignItems: "center", justifyContent: "space-between", mb: 1 }}>
                <Box sx={{ display: "flex", alignItems: "center" }}>
                    <Avatar
                        src={review.avatarUrl}
                        alt={review.userName}
                        sx={{ mr: 2, width: { xs: 35, sm: 40 }, height: { xs: 35, sm: 40 } }}
                    />
                    <Box>
                        <Typography variant="subtitle1" sx={{ fontWeight: "bold", fontSize: { xs: "0.9rem", sm: "1rem" } }}>
                            {review.userName}
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                            {review.date}
                        </Typography>
                    </Box>
                </Box>
                    <IconButton
                        onClick={() => onDelete?.(review.id)}
                        size="small"
                        sx={{
                            "&:hover": {
                                backgroundColor: "error.light",
                            }
                        }}
                    >
                        <DeleteIcon fontSize="small" color="error" />
                    </IconButton>
            </Box>

            <Rating value={review.rating} readOnly size="small" sx={{ mb: 1 }} />

            <Typography variant="body2" sx={{ mb: 1.5, color: "text.primary", fontSize: { xs: "0.85rem", sm: "0.875rem" } }}>
                {review.reviewText}
            </Typography>

            {review.photoUrl && (
                <Box
                    component="img"
                    src={review.photoUrl}
                    alt="Review attachment"
                    sx={{
                        width: { xs: 80, sm: 100 },
                        height: { xs: 80, sm: 100 },
                        borderRadius: 2,
                        objectFit: "cover",
                        border: "1px solid #eee",
                        mt: 1,
                        display: "block"
                    }}
                />
            )}
        </Box>
    );
};

export default ReviewCard;