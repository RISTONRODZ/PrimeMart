const AddressCard = () => {
    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        console.log(event.target.checked);
    };

    return (
        <div className="flex items-start gap-4 p-4 border border-blue-200 rounded-md mb-4 hover:border-blue-700 transition-colors">
            <div>
                <input
                    type="radio"
                    name="radio-button"
                    className="accent-blue-700 mt-1"
                    onChange={handleChange}
                />
            </div>
            <div className='space-y-3 pt-3'>
                <h1 className="font-semibold text-blue-900">riston</h1>
                <p className='w-[320px] text-sm text-slate-600'>
                   mumbai
                </p>
                <p className="text-sm">
                    <strong className="text-blue-800">Mobile :</strong> 9023379136
                </p>
            </div>
        </div>
    );
};

export default AddressCard;