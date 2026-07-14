import { useState, useEffect } from "react";
import { Button } from "@mui/material";
import { Add } from "@mui/icons-material";
import UserAddressCard from "./UserAddressCard.tsx";
import AddressFormDialog from "./AddressFormDialog.tsx";

interface AddressData {
    name: string;
    mobile: string;
    pinCode: string;
    address: string;
    locality: string;
    city: string;
    state: string;
}

const Address = () => {
    const [addresses, setAddresses] = useState<AddressData[]>([]);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [editingAddress, setEditingAddress] = useState<AddressData | null>(null);

    useEffect(() => {
        const savedAddresses = localStorage.getItem("savedAddresses");
        if (savedAddresses) {
            setAddresses(JSON.parse(savedAddresses));
        }
    }, []);

    const handleSaveAddress = (address: AddressData) => {
        let updatedAddresses: AddressData[];
        if (editingAddress) {
            updatedAddresses = addresses.map((addr) =>
                addr === editingAddress ? address : addr
            );
        } else {
            updatedAddresses = [...addresses, address];
        }
        setAddresses(updatedAddresses);
        localStorage.setItem("savedAddresses", JSON.stringify(updatedAddresses));
        setEditingAddress(null);
    };

    const handleEditAddress = (address: AddressData) => {
        setEditingAddress(address);
        setDialogOpen(true);
    };

    const handleDeleteAddress = (address: AddressData) => {
        const updatedAddresses = addresses.filter((addr) => addr !== address);
        setAddresses(updatedAddresses);
        localStorage.setItem("savedAddresses", JSON.stringify(updatedAddresses));
    };

    const handleAddAddress = () => {
        setEditingAddress(null);
        setDialogOpen(true);
    };

    const handleCloseDialog = () => {
        setDialogOpen(false);
        setEditingAddress(null);
    };

    return (
        <div className={'space-y-5'}>
            <Button
                variant="contained"
                startIcon={<Add />}
                onClick={handleAddAddress}
                sx={{ mb: 2 }}
            >
                Add New Address
            </Button>
            {addresses.length === 0 ? (
                <p className="text-gray-500">No addresses saved yet.</p>
            ) : (
                addresses.map((address, index) => (
                    <UserAddressCard
                        key={index}
                        address={address}
                        onEdit={() => handleEditAddress(address)}
                        onDelete={() => handleDeleteAddress(address)}
                    />
                ))
            )}
            <AddressFormDialog
                open={dialogOpen}
                onClose={handleCloseDialog}
                onSave={handleSaveAddress}
                initialData={editingAddress || undefined}
            />
        </div>
    );
};

export default Address;