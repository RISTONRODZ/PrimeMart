import DealCard from "./DealCard.tsx";
import {Swiper, SwiperSlide} from 'swiper/react';
import 'swiper/css';
import {Autoplay,Navigation} from "swiper/modules";
import 'swiper/css/navigation';
const Deal = () => {
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
                onSlideChange={() => console.log('slide change')}
            >
                {Array.from({length: 10}).map((_, i) => (<SwiperSlide key={i}>
                    <DealCard/>
                </SwiperSlide>))}
            </Swiper>
        </div>);
};

export default Deal;
