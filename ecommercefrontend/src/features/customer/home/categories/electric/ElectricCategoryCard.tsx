import {useNavigate} from "react-router-dom";
import type {HomeCategory} from "../../../../../types/HomeCategory.ts";

const ElectricCategoryCard = ({category}: {category: HomeCategory}) => {
    const navigate = useNavigate();
    
    const handleCategoryClick = () => {
        navigate(`/product/${category.name}`);
    };
    
    return (
        <div onClick={handleCategoryClick} className="cursor-pointer">
            <img className={'object-fit w-40 h-30 '} alt={category.name || 'Category'} src={category.imageUrl}/>
            <h2 className={'font-semibold text-center'}>{category.name}</h2>
        </div>
    );
};

export default ElectricCategoryCard;
