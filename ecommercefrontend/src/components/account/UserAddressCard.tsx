
const UserAddressCard = () => {
    return (
        <div className="p-4 sm:p-5 border rounded-md flex">
            <div className="space-y-3 pt-3 w-full">
                <h1 className='text-sm sm:text-base'>Riston</h1>

                <p className='w-full sm:w-[320px] text-sm sm:text-base'>
                    <strong>Address :</strong> mumbai
                </p>

                <p className='text-sm sm:text-base'>
                    <strong>Mobile :</strong> 9023379136
                </p>
            </div>
        </div>
    );
};

export default UserAddressCard;