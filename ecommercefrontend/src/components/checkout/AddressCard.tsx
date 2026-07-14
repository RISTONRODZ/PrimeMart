import type { Address } from "../../types/UserTypes.ts";
import { Delete } from "@mui/icons-material";

interface AddressCardProps {
    address: Address;
    onSelect?: () => void;
    onDelete?: () => void;
    isSelected?: boolean;
    index?: number;
}

const AddressCard = ({ address, onSelect, onDelete, isSelected }: AddressCardProps) => {
    return (
        <div
            className="flex items-start gap-4 p-4 border border-blue-200 rounded-md mb-4 hover:border-blue-700 transition-colors cursor-pointer"
            onClick={(e) => {
                if ((e.target as HTMLElement).closest('button')) return;
                onSelect?.();
            }}
        >
            <div className="pointer-events-none">
                <input
                    type="radio"
                    className="accent-blue-700 mt-1"
                    checked={isSelected}
                    readOnly
                />
            </div>
            <div className='flex-1 min-w-0 space-y-3 pt-3'>
                <h1 className="font-semibold text-blue-900">{address.name}</h1>
                <p className='text-sm text-slate-600 break-words'>
                    {address.address}, {address.locality}, {address.city}, {address.state} - {address.pinCode}
                </p>
                <p className="text-sm">
                    <strong className="text-blue-800">Mobile :</strong> {address.mobile}
                </p>
            </div>
            <button
                onClick={(e) => {
                    e.stopPropagation();
                    onDelete?.();
                }}
                className="text-red-500 hover:text-red-700 transition-colors p-1"
                title="Delete address"
            >
                <Delete fontSize="small" />
            </button>
        </div>
    );
};

export default AddressCard;