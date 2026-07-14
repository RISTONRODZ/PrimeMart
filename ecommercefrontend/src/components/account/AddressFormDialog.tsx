import { useState } from "react";
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button } from "@mui/material";

interface AddressData {
    name: string;
    mobile: string;
    pinCode: string;
    address: string;
    locality: string;
    city: string;
    state: string;
}

interface AddressFormDialogProps {
    open: boolean;
    onClose: () => void;
    onSave: (address: AddressData) => void;
    initialData?: AddressData;
}

const AddressFormDialog = ({ open, onClose, onSave, initialData }: AddressFormDialogProps) => {
    const [formData, setFormData] = useState<AddressData>(
        initialData || {
            name: "",
            mobile: "",
            pinCode: "",
            address: "",
            locality: "",
            city: "",
            state: "",
        }
    );

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = () => {
        onSave(formData);
        onClose();
    };

    return (
        <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
            <DialogTitle>{initialData ? "Edit Address" : "Add New Address"}</DialogTitle>
            <DialogContent>
                <TextField
                    autoFocus
                    margin="dense"
                    name="name"
                    label="Full Name"
                    fullWidth
                    value={formData.name}
                    onChange={handleChange}
                    variant="outlined"
                    sx={{ mb: 2 }}
                />
                <TextField
                    margin="dense"
                    name="mobile"
                    label="Mobile Number"
                    fullWidth
                    value={formData.mobile}
                    onChange={handleChange}
                    variant="outlined"
                    sx={{ mb: 2 }}
                />
                <TextField
                    margin="dense"
                    name="pinCode"
                    label="Pincode"
                    fullWidth
                    value={formData.pinCode}
                    onChange={handleChange}
                    variant="outlined"
                    sx={{ mb: 2 }}
                />
                <TextField
                    margin="dense"
                    name="address"
                    label="Address"
                    fullWidth
                    multiline
                    rows={2}
                    value={formData.address}
                    onChange={handleChange}
                    variant="outlined"
                    sx={{ mb: 2 }}
                />
                <TextField
                    margin="dense"
                    name="locality"
                    label="Locality"
                    fullWidth
                    value={formData.locality}
                    onChange={handleChange}
                    variant="outlined"
                    sx={{ mb: 2 }}
                />
                <TextField
                    margin="dense"
                    name="city"
                    label="City"
                    fullWidth
                    value={formData.city}
                    onChange={handleChange}
                    variant="outlined"
                    sx={{ mb: 2 }}
                />
                <TextField
                    margin="dense"
                    name="state"
                    label="State"
                    fullWidth
                    value={formData.state}
                    onChange={handleChange}
                    variant="outlined"
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Cancel</Button>
                <Button onClick={handleSubmit} variant="contained">
                    Save
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default AddressFormDialog;