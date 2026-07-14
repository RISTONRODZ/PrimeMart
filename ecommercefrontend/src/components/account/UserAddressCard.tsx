import { IconButton, Box } from "@mui/material";
import { Edit, Delete } from "@mui/icons-material";

interface AddressData {
    name: string;
    mobile: string;
    pinCode: string;
    address: string;
    locality: string;
    city: string;
    state: string;
}

interface UserAddressCardProps {
    address: AddressData;
    onEdit: () => void;
    onDelete: () => void;
}

const UserAddressCard = ({ address, onEdit, onDelete }: UserAddressCardProps) => {
    return (
        <div className="p-4 sm:p-5 border rounded-md flex">
            <div className="space-y-3 pt-3 w-full">
                <h1 className='text-sm sm:text-base'>{address.name}</h1>

                <p className='w-full sm:w-[320px] text-sm sm:text-base'>
                    <strong>Address :</strong> {address.address}
                </p>

                {address.locality && (
                    <p className='text-sm sm:text-base'>
                        <strong>Locality :</strong> {address.locality}
                    </p>
                )}

                <p className='text-sm sm:text-base'>
                    <strong>City :</strong> {address.city}
                </p>

                <p className='text-sm sm:text-base'>
                    <strong>State :</strong> {address.state}
                </p>

                <p className='text-sm sm:text-base'>
                    <strong>Pin Code :</strong> {address.pinCode}
                </p>

                <p className='text-sm sm:text-base'>
                    <strong>Mobile :</strong> {address.mobile}
                </p>

                <Box sx={{ display: "flex", gap: 1, mt: 2 }}>
                    <IconButton onClick={onEdit} color="primary" size="small">
                        <Edit />
                    </IconButton>
                    <IconButton onClick={onDelete} color="error" size="small">
                        <Delete />
                    </IconButton>
                </Box>
            </div>
        </div>
    );
};

export default UserAddressCard;