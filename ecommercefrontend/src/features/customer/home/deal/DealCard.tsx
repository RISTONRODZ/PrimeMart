const DealCard = () => {
    return (
        <div className="w-full rounded-2xl overflow-hidden border border-blue-100 shadow-sm">
            <div className="relative">
                <img
                    className="w-full h-48 object-cover"
                    src="https://i.pinimg.com/736x/07/71/ed/0771edee4246abc5f034c3be2e739e74.jpg"
                    alt="Perfumes"
                />
                <span className="absolute top-3 right-3 bg-blue-900 text-white text-xs font-semibold px-3 py-1 rounded-full">
                    20% off
                </span>
                <div className="absolute bottom-0 left-0 right-0 h-12 bg-linear-to-t from-white to-transparent" />
            </div>
            <div className="bg-white px-4 pb-4">
                <p className="text-xs font-semibold uppercase tracking-widest text-blue-900 mb-0.5">
                    Fragrance
                </p>
                <p className="text-base font-semibold text-gray-900 mb-3">
                    Perfumes
                </p>
                <button className="w-full bg-blue-900 hover:bg-blue-800 text-white text-sm font-medium py-2 rounded-lg transition-colors">
                    Shop now
                </button>
            </div>
        </div>
    );
};

export default DealCard;
