import ReviewCard from "./ReviewCard.tsx";
import ReviewSummary from "./ReviewSummary.tsx";

const reviews = [
    {
        id: 1,
        userName: "John Doe",
        avatarUrl: "https://ui-avatars.com/api/?name=John+Doe",
        rating: 4,
        date: "2026-06-29",
        reviewText: "Great product! The quality is amazing and it fits perfectly.",
        photoUrl: "https://i.pinimg.com/1200x/08/53/5f/08535f1e7a385d279655275a02fa64ca.jpg"
    },
    {
        id: 2,
        userName: "Jane Smith",
        avatarUrl: "https://ui-avatars.com/api/?name=Jane+Smith",
        rating: 5,
        date: "2026-06-28",
        reviewText: "Absolutely love this shirt! The fabric is comfortable and the design is stylish."
    },
    {
        id: 3,
        userName: "Mike Johnson",
        avatarUrl: "https://ui-avatars.com/api/?name=Mike+Johnson",
        rating: 4,
        date: "2026-06-27",
        reviewText: "Good value for money. Shipping was fast and the product arrived in perfect condition."
    },
    {
        id: 4,
        userName: "Sarah Williams",
        avatarUrl: "https://ui-avatars.com/api/?name=Sarah+Williams",
        rating: 5,
        date: "2026-06-26",
        reviewText: "Exceeded my expectations! Will definitely buy again.",
        photoUrl: "https://i.pinimg.com/1200x/08/53/5f/08535f1e7a385d279655275a02fa64ca.jpg"
    },
    {
        id: 5,
        userName: "David Brown",
        avatarUrl: "https://ui-avatars.com/api/?name=David+Brown",
        rating: 4,
        date: "2026-06-25",
        reviewText: "Nice shirt, good quality. The color is exactly as shown in the pictures."
    }
];

const Review = () => {
    return (
        <div className={'p-5 lg:px-20 flex flex-col lg:flex-row gap-20'}>
           <section className={'w-full md:w-1/2 lg:w-[30%] space-y-2'}>
                <img src="https://i.pinimg.com/1200x/08/53/5f/08535f1e7a385d279655275a02fa64ca.jpg" alt="" />
               <div>
                   <div>
                       <p className={'font-bold text-xl'}>
                           Gucci
                       </p>
                       <p className={'text-lg text-gray-600'}>Men's White Shirt</p>
                   </div>
                   <div className="flex items-center gap-2 flex-wrap">
            <span className="font-bold text-lg text-gray-600 ">
         ₹4,499
    </span>
                       <span className="text-sm text-gray-400 line-through">
        ₹5,499 MRP
    </span>

                       <span className="text-green-600 text-sm font-medium">
        40% Off
    </span>
                   </div>
               </div>
               <ReviewSummary reviews={reviews} />
           </section>
            <section className={'flex-1'}>
                {reviews.map((review) => (
                    <ReviewCard key={review.id} review={review} />
                ))}
            </section>
        </div>
    );
};

export default Review;