import { useState, useEffect } from "react";
import AddressCard from "./AddressCard.tsx";
import AddressForm from "./AddressForm.tsx";
import PricingCard from "./PricingCard.tsx";
import { Close } from "@mui/icons-material";
import type { Address } from "../../types/UserTypes.ts";

const AddressModal = ({
                          isOpen,
                          onClose,
                          onAddressSave,
                      }: {
    isOpen: boolean;
    onClose: () => void;
    onAddressSave: (address: Address) => void;
}) => {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 z-100 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4 animate-in fade-in duration-200">
            <div className="w-full max-w-lg max-h-[90vh] bg-white rounded-3xl shadow-2xl flex flex-col overflow-hidden animate-in zoom-in-95 duration-200">
                <div className="flex items-center justify-between px-6 py-5 border-b border-slate-100 flex-shrink-0">
                    <h2 className="text-xl font-bold text-slate-800">Add New Address</h2>
                    <button
                        onClick={onClose}
                        className="p-2 rounded-full hover:bg-slate-100 transition-colors"
                    >
                        <Close fontSize="small" />
                    </button>
                </div>
                <div className="p-6 overflow-y-auto flex-1">
                    <AddressForm
                        onClose={(address) => {
                            if (address) onAddressSave(address);
                            onClose();
                        }}
                    />
                </div>
            </div>
        </div>
    );
};

const Checkout = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [addresses, setAddresses] = useState<Address[]>(() => {
        const saved = localStorage.getItem("savedAddresses");
        return saved ? JSON.parse(saved) : [];
    });
    const [selectedIndex, setSelectedIndex] = useState<number | null>(() => {
        const saved = localStorage.getItem("selectedAddressIndex");
        return saved ? JSON.parse(saved) : null;
    });

    useEffect(() => {
        localStorage.setItem("savedAddresses", JSON.stringify(addresses));
    }, [addresses]);

    useEffect(() => {
        localStorage.setItem("selectedAddressIndex", JSON.stringify(selectedIndex));
    }, [selectedIndex]);

    const handleAddressSave = (address: Address) => setAddresses([...addresses, address]);
    const handleAddressDelete = (indexToDelete: number) => {
        setAddresses(addresses.filter((_, i) => i !== indexToDelete));
        if (selectedIndex === indexToDelete) setSelectedIndex(null);
    };

    return (
        <div className="min-h-screen bg-slate-50/50">
            <AddressModal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                onAddressSave={handleAddressSave}
            />

            <main className="max-w-6xl mx-auto px-4 py-8 lg:py-12">
                <div className="grid lg:grid-cols-12 gap-8 xl:gap-12">
                    <div className="lg:col-span-7 xl:col-span-8 space-y-8">
                        <div>
                            <h1 className="text-3xl font-extrabold text-slate-900 mb-6">Checkout</h1>
                            <div className="flex items-center justify-between mb-4">
                                <h2 className="text-lg font-bold text-slate-700">Shipping Address</h2>
                                <button
                                    onClick={() => setIsModalOpen(true)}
                                    className="text-sm font-semibold text-blue-600 hover:text-blue-700 transition-colors"
                                >
                                    + Add New
                                </button>
                            </div>

                            <div className="grid gap-4">
                                {addresses.length > 0 ? (
                                    addresses.map((addr, idx) => (
                                        <AddressCard
                                            key={idx}
                                            address={addr}
                                            index={idx}
                                            onSelect={() => setSelectedIndex(idx)}
                                            onDelete={() => handleAddressDelete(idx)}
                                            isSelected={selectedIndex === idx}
                                        />
                                    ))
                                ) : (
                                    <div className="text-center py-12 border-2 border-dashed border-slate-200 rounded-2xl">
                                        <p className="text-slate-500">No addresses saved yet.</p>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                    <div className="lg:col-span-5 xl:col-span-4">
                        <div className="lg:sticky lg:top-8">
                            <PricingCard
                                title="Standard Plan"
                                price={1000}
                                features={["Free Priority Shipping", "Exclusive 10% Discount", "24/7 Support Access"]}
                                isAddressSelected={selectedIndex !== null}
                                selectedAddress={selectedIndex !== null ? addresses[selectedIndex] : null}
                            />
                        </div>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default Checkout;