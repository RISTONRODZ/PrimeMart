import {Storefront} from "@mui/icons-material";
import {Button} from "@mui/material";
import {useNavigate} from "react-router-dom";
import becomeASellerImg from "../../../assets/become_a_seller.png";
const BecomeASeller = () => {
    const navigate = useNavigate();
    return (
        <div className={'flex justify-center'}>
            <div className="w-[90vw] relative rounded-2xl overflow-hidden">
                <img
                    src={becomeASellerImg}
                    alt="Become a seller background"
                    className="w-full h-auto block"
                />
                <div
                    className="absolute top-[60%] md:top-[62%] lg:top-[55%] lg:left-[22%] md:left-[22%] s:left-[22%] left-[15%] -translate-y-1/2">
                    <Button
                        startIcon={<Storefront/>}
                        variant="contained"
                        onClick={() => navigate("/become-seller")}
                        sx={{
                            backgroundColor: 'white',
                            color: '#1e3a8a',
                            fontSize: 'clamp(0.5rem, 1.5vw, 0.875rem)',
                            padding: 'clamp(4px, 1vw, 8px) clamp(8px, 2vw, 16px)',
                            minWidth: 0,
                            '&:hover': {backgroundColor: '#f3f4f6'},
                            '& .MuiButton-startIcon svg': {
                                fontSize: 'clamp(0.75rem, 2vw, 1.25rem)',
                            }
                        }}
                    >
                        Become Seller
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default BecomeASeller;
