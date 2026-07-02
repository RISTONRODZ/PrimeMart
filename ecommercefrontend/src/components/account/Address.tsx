import UserAddressCard from "./UserAddressCard.tsx";

const Address = () => {
    return (
        <div className={'space-y-5'}>
            {
                Array.from({ length: 3 }).map((_, index) => (
                    <UserAddressCard key={index} />
                ))
            }
        </div>
    );
};

export default Address;