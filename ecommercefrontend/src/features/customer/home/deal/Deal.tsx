import DealCard from "./DealCard.tsx";
import {Swiper, SwiperSlide} from 'swiper/react';
import 'swiper/css';
import {Autoplay,Navigation} from "swiper/modules";
import 'swiper/css/navigation';
import {useAppSelector} from "../../../../state/hooks.ts";
import {useEffect} from "react";
import {useAppDispatch} from "../../../../state/hooks.ts";
import {getAllDeals} from "../../../../state/admin/DealSlice.ts";

const Deal = () => {
    const dispatch = useAppDispatch();
    const {home, deal} = useAppSelector(store=>store);

    useEffect(() => {
        try {
            dispatch(getAllDeals());
        } catch (error) {
            console.error("Error fetching deals:", error);
        }
    }, [dispatch]);

    // console.log("Deal component - homePageData:", home.homePageData);
    // console.log("Deal component - home deals:", home.homePageData?.deals);
    // console.log("Deal component - deal slice deals:", deal.deals);
    // console.log("Deal component - deal loading:", deal.loading);
    // console.log("Deal component - home loading:", home.loading);
    // console.log("Deal component - deal error:", deal.error);
    // console.log("Deal component - home error:", home.error);

    const deals = (Array.isArray(deal.deals) && deal.deals.length > 0) ? deal.deals : Array.isArray(home.homePageData?.deals) ? home.homePageData.deals : [];
    // console.log("Deal component - final deals array:", deals);
    // console.log("Deal component - deals length:", deals.length);

    if (deal.loading || home.loading) return <div className="px-6 md:px-10 py-6">Loading deals...</div>;
    if (deal.error || home.error) return <div className="px-6 md:px-10 py-6 text-red-500">Error: {deal.error || home.error}</div>;
    if (deals.length === 0) {
        return <div className="px-6 md:px-10 py-6">No deals available</div>;
    }

    return (<div className="px-6 md:px-10 py-6">
            <Swiper
                spaceBetween={10}
                slidesPerView={5}
                modules={[Autoplay,Navigation]}
                breakpoints={{
                    0:    { slidesPerView: 1 },
                    640:  { slidesPerView: 2 },
                    1024: { slidesPerView: 3 },
                    1280: { slidesPerView: 4 },
                }}
                navigation={true}
                autoplay={{delay: 3000, disableOnInteraction: false}}
            >
                {deals.map((deal) => (
                    <SwiperSlide key={deal.id}>
                        <DealCard item={deal} />
                    </SwiperSlide>
                ))}
            </Swiper>
        </div>);
};

export default Deal;
