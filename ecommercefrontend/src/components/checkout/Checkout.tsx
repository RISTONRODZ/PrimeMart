import {useState} from 'react';
import AddressCard from "./AddressCard.tsx";
import AddressForm from "./AddressForm.tsx";
import PricingCard from "./PricingCard.tsx";

const AddressModal = ({isOpen, onClose}: { isOpen: boolean; onClose: () => void }) => {
    if (!isOpen) return null;
    return (
        <div className="fixed inset-0 flex items-center justify-center z-50 backdrop-blur-xs ">
            <div className="bg-white p-6 rounded-lg shadow-lg w-96">
                <h2 className="text-xl font-bold mb-4">Add New Address</h2>
                <AddressForm/>
                <button
                    onClick={onClose}
                    className="mt-4 w-full bg-blue-700 text-white px-4 py-2 rounded"
                >
                    Close
                </button>
            </div>
        </div>
    );
};

const Checkout = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    return (
        <div className='pt-10 px-5 sm:px-10 md:px-44 lg:px-60 min-h-screen text-slate-800 lg:grid lg:grid-cols-3 py-3'>
            <AddressModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}/>

            <div className='col-span-2 space-y-5'>
                <h1 className="font-semibold text-lg">Select Address</h1>

                <div className='text-xs font-medium space-y-5'>
                    <p>Saved Addresses</p>
                    <div>
                        {Array.from({ length: 10 }).map((_item, index) => (
                            <AddressCard key={index}/>
                        ))}
                    </div>
                </div>

                <div className='py-4 px-5 rounded-md border border-blue-700'>
                    <button
                        onClick={() => setIsModalOpen(true)}
                        className="w-full text-blue-700 font-semibold"
                    >
                        Add new Address
                    </button>
                </div>
            </div>
            <div className={'lg:sticky lg:top-10 lg:pl-10 pt-10 pl-10'}>
                <PricingCard title="Standard" price={1000}
                             features={['Free Shipping', '10% Discount', '₹10 platform fee']}/>
            </div>
        </div>
    );
};

export default Checkout;