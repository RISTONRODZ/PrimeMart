import { useState } from "react";
import StarIcon from '@mui/icons-material/Star';
import { Button, Divider, IconButton } from "@mui/material";
import { AddShoppingCart, FavoriteBorder, LocalShipping, Shield, WorkspacePremium } from "@mui/icons-material";
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import SimilarProduct from "./SimilarProduct.tsx";
import ReviewCard from "../../../../components/review/ReviewCard.tsx";;

const ProductDetails = () => {
    const images = [
        'https://i.pinimg.com/1200x/b1/45/ef/b145efb4a2a6413b25fa421cbf55754c.jpg',
        'https://i.pinimg.com/1200x/b1/45/ef/b145efb4a2a6413b25fa421cbf55754c.jpg',
        'https://i.pinimg.com/1200x/b1/45/ef/b145efb4a2a6413b25fa421cbf55754c.jpg',
        'https://i.pinimg.com/1200x/b1/45/ef/b145efb4a2a6413b25fa421cbf55754c.jpg'
    ];
    const [activeImage, setActiveImage] = useState(images[0]);
    const [quantity, setQuantity] = useState(1);
    const [visibleCount, setVisibleCount] = useState(3);

    const [reviews, setReviews] = useState([...Array(10)].map((_, index) => ({
        id: index + 1,
        userName: `User ${index + 1}`,
        avatarUrl: "https://ui-avatars.com/api/?name=User",
        rating: 4,
        date: "2026-06-29",
        reviewText: "Great product!",
        photoUrl: "https://i.pinimg.com/1200x/08/53/5f/08535f1e7a385d279655275a02fa64ca.jpg"
    })));

    const handleDeleteReview = (id: number) => {
        setReviews(reviews.filter(review => review.id !== id));
    };

    const handleIncrement = () => setQuantity(prev => prev + 1);
    const handleDecrement = () => setQuantity(prev => prev > 1 ? prev - 1 : 1);

    return (
        <div className='max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10'>
            <div className='grid grid-cols-1 lg:grid-cols-12 gap-12 lg:gap-16 items-start'>
                <section className='lg:col-span-7 flex flex-col-reverse md:flex-row gap-4 w-full'>
                    <div className='flex flex-row md:flex-col gap-3 overflow-x-auto md:overflow-visible w-full md:w-[15%] scrollbar-none'>
                        {images.map((img, index) => (
                            <img
                                key={index}
                                onClick={() => setActiveImage(img)}
                                className={`w-20 h-20 md:w-full md:h-auto object-cover cursor-pointer rounded-lg border-2 transition-all duration-200  ${
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
                            alt="Main product representation"
                        />
                    </div>
                </section>

                <section className='lg:col-span-5 flex flex-col justify-between h-full'>
                    <div>
                        <h1 className='font-bold text-2xl md:text-3xl text-blue-700 tracking-wide'>
                            Ro's Clothing
                        </h1>
                        <p className='text-gray-500 font-medium text-lg mt-1 capitalize'>
                            men black shirt
                        </p>
                        <div className='flex items-center gap-3 py-1.5 border border-gray-200 rounded-lg w-fit px-3 mt-4 bg-gray-50/50'>
                            <div className='flex gap-1 items-center font-semibold text-gray-700'>
                                <span>4.5</span>
                                <StarIcon sx={{ color: '#1447e6', fontSize: "18px" }} />
                            </div>
                            <Divider orientation='vertical' flexItem />
                            <span className='text-sm text-gray-500 font-medium'>
                                243 Ratings
                            </span>
                        </div>
                        <div className='mt-6 flex flex-wrap items-baseline gap-3 border-b border-gray-100 pb-6'>
                            <span className='text-3xl font-bold text-gray-900'>
                                $400
                            </span>
                            <span className='line-through text-gray-400 text-lg'>
                                $500
                            </span>
                            <span className='text-green-600 font-semibold text-lg bg-green-50 px-2 py-0.5 rounded'>
                                20% off
                            </span>
                            <p className='text-xs text-gray-400 w-full mt-1 font-medium'>
                                Inclusive of all taxes
                            </p>
                        </div>
                        <div className='mt-6 space-y-3.5 text-gray-600 text-sm font-medium'>
                            <div className='flex items-center gap-3.5'>
                                <Shield sx={{ color: '#1447e6', fontSize: 22 }} />
                                <p>Authentic & Quality Assured</p>
                            </div>
                            <div className='flex items-center gap-3.5'>
                                <WorkspacePremium sx={{ color: '#1447e6', fontSize: 22 }} />
                                <p>Free Shipping</p>
                            </div>
                            <div className='flex items-center gap-3.5'>
                                <LocalShipping sx={{ color: '#1447e6', fontSize: 22 }} />
                                <p>Fast delivery</p>
                            </div>
                        </div>
                        <div className='mt-8 pt-6 border-t border-gray-100'>
                            <h3 className='text-sm font-bold text-gray-800 uppercase tracking-wider mb-3'>
                                Quantity
                            </h3>
                            <div className='flex items-center border border-gray-300 rounded-lg w-fit bg-white shadow-sm'>
                                <IconButton onClick={handleDecrement} size="small" className="p-2 text-gray-600">
                                    <RemoveIcon fontSize="small" />
                                </IconButton>
                                <span className='w-12 text-center font-semibold text-gray-800 select-none'>
                                    {quantity}
                                </span>
                                <IconButton onClick={handleIncrement} size="small" className="p-2 text-gray-600">
                                    <AddIcon fontSize="small" />
                                </IconButton>
                            </div>
                        </div>
                    </div>
                    <div className='mt-8 space-y-4'>
                        <div className='flex flex-col sm:flex-row items-center gap-4'>
                            <Button
                                variant='contained'
                                startIcon={<AddShoppingCart />}
                                fullWidth
                                sx={{
                                    py: "0.85rem",
                                    backgroundColor: '#1447e6',
                                    fontWeight: 'bold',
                                    borderRadius: '8px',
                                    '&:hover': { backgroundColor: '#0f35b5' }
                                }}
                            >
                                Add to Cart
                            </Button>
                            <Button
                                variant='outlined'
                                startIcon={<FavoriteBorder />}
                                fullWidth
                                sx={{
                                    py: "0.85rem",
                                    borderColor: '#e5e7eb',
                                    color: '#374151',
                                    fontWeight: 'bold',
                                    borderRadius: '8px',
                                    '&:hover': { borderColor: '#1447e6', backgroundColor: '#f0f4ff' }
                                }}
                            >
                                Wishlist
                            </Button>
                        </div>
                        <div className='pt-6 border-t border-gray-100 text-gray-600 text-sm leading-relaxed'>
                            <p>
                                Crafted from breathable premium cotton, this modern silhouette premium shirt offers standard athletic versatility tailored perfectly for casual ensembles or professional layers alike.
                            </p>
                        </div>
                    </div>
                </section>
            </div>

            <div className='mt-16 border-t border-gray-100 pt-10'>
                <h2 className='text-2xl font-bold text-gray-800 mb-6'>Customer Reviews</h2>
                <div className='max-w-3xl'>
                    {reviews.slice(0, visibleCount).map((review) => (
                        <ReviewCard key={review.id} review={review} onDelete={handleDeleteReview}/>
                    ))}
                    {reviews.length > 3 && (
                        <Button
                            variant="text"
                            onClick={() => setVisibleCount(visibleCount === 3 ? reviews.length : 3)}
                            sx={{ mt: 2, fontWeight: 'bold' }}
                        >
                            {visibleCount === 3 ? "View More Reviews" : "View Less"}
                        </Button>
                    )}
                </div>
            </div>

            <div>
                <SimilarProduct />
            </div>
        </div>
    );
};

export default ProductDetails;