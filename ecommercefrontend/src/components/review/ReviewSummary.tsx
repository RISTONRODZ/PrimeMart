import { Box, Typography, LinearProgress } from "@mui/material";
import StarIcon from "@mui/icons-material/Star";

export interface Review {
    id: number;
    userName: string;
    avatarUrl: string;
    rating: number;
    date: string;
    reviewText: string;
    photoUrl?: string;
}

interface ReviewSummaryProps {
    reviews: Review[];
}

const ReviewSummary = ({ reviews }: ReviewSummaryProps) => {
    const totalReviews = reviews.length;

    if (totalReviews === 0) {
        return null;
    }

    const averageRating = reviews.reduce((sum, review) => sum + review.rating, 0) / totalReviews;

    const ratingCounts = {
        5: reviews.filter(r => r.rating === 5).length,
        4: reviews.filter(r => r.rating === 4).length,
        3: reviews.filter(r => r.rating === 3).length,
        2: reviews.filter(r => r.rating === 2).length,
        1: reviews.filter(r => r.rating === 1).length,
    };

    const ratingPercentages = {
        5: (ratingCounts[5] / totalReviews) * 100,
        4: (ratingCounts[4] / totalReviews) * 100,
        3: (ratingCounts[3] / totalReviews) * 100,
        2: (ratingCounts[2] / totalReviews) * 100,
        1: (ratingCounts[1] / totalReviews) * 100,
    };

    return (
        <Box sx={{ p: { xs: 2, sm: 3 }, border: "1px solid #e0e0e0", borderRadius: 2 }}>
            <Box sx={{ display: "flex", alignItems: "center", gap: 2, mb: 3 }}>
                <Box sx={{ textAlign: "center" }}>
                    <Typography variant="h3" sx={{ fontWeight: "bold", color: "#1a1a1a" }}>
                        {averageRating.toFixed(1)}
                    </Typography>
                    <Box sx={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
                        <StarIcon sx={{ fontSize: 20, color: "#FFA500" }} />
                        <StarIcon sx={{ fontSize: 20, color: "#FFA500" }} />
                        <StarIcon sx={{ fontSize: 20, color: "#FFA500" }} />
                        <StarIcon sx={{ fontSize: 20, color: "#FFA500" }} />
                        <StarIcon sx={{ fontSize: 20, color: "#FFA500" }} />
                    </Box>
                </Box>
                <Box sx={{ flex: 1 }}>
                    {[5, 4, 3, 2, 1].map((star) => (
                        <Box key={star} sx={{ display: "flex", alignItems: "center", gap: 1, mb: 0.5 }}>
                            <Typography variant="body2" sx={{ minWidth: 20, fontSize: "0.875rem" }}>
                                {star}
                            </Typography>
                            <StarIcon sx={{ fontSize: 16, color: "#FFA500" }} />
                            <LinearProgress
                                variant="determinate"
                                value={ratingPercentages[star as keyof typeof ratingPercentages]}
                                sx={{
                                    flex: 1,
                                    height: 8,
                                    borderRadius: 4,
                                    backgroundColor: "#e0e0e0",
                                    "& .MuiLinearProgress-bar": {
                                        backgroundColor: "#FFA500",
                                        borderRadius: 4,
                                    },
                                }}
                            />
                            <Typography variant="body2" sx={{ minWidth: 45, textAlign: "right", fontSize: "0.875rem", color: "#666" }}>
                                {Math.round(ratingPercentages[star as keyof typeof ratingPercentages])}%
                            </Typography>
                        </Box>
                    ))}
                </Box>
            </Box>
            <Typography variant="body2" sx={{ color: "#666", fontSize: "0.875rem" }}>
                {totalReviews} global ratings
            </Typography>
        </Box>
    );
};

export default ReviewSummary;
