import { Storefront } from "@mui/icons-material";
import { Button } from "@mui/material";

const BecomeASeller = () => {
    return (
        <div className="w-full relative">
            <img
                src="src/assets/become_a_seller.png"
                alt="Become a seller background"
                className="w-full h-auto block"
            />
            <div className="absolute top-[60%] md:top-[62%] lg:top-[55%] lg:left-[22%] md:left-[22%] s:left-[22%] left-[20%] -translate-y-1/2">
                <Button
                    startIcon={<Storefront />}
                    variant="contained"
                    sx={{
                        backgroundColor: 'white',
                        color: '#1e3a8a',
                        fontSize: 'clamp(0.5rem, 1.5vw, 0.875rem)',
                        padding: 'clamp(4px, 1vw, 8px) clamp(8px, 2vw, 16px)',
                        minWidth: 0,
                        '&:hover': { backgroundColor: '#f3f4f6' },
                        '& .MuiButton-startIcon svg': {
                            fontSize: 'clamp(0.75rem, 2vw, 1.25rem)',
                        }
                    }}
                >
                    Become Seller
                </Button>
            </div>
        </div>
    );
};

export default BecomeASeller;
