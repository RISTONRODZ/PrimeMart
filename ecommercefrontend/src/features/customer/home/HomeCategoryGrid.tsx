
const HomeCategoryGrid = () => {
    return (
        <div className="hidden md:grid grid-cols-4 grid-rows-2 gap-4 px-20 h-200 py-10">
            <div className="col-span-1 row-span-2">
                <img className="w-full h-full object-cover rounded-md overflow-hidden" src="https://i.pinimg.com/1200x/0c/5c/c0/0c5cc0364161dff88a57b35449ab13c4.jpg" alt="Man in a formal suit" />
            </div>
            <div className="col-span-1 row-span-1">
                <img className="w-full h-full object-fit rounded-lg overflow-hidden" src="https://i.pinimg.com/736x/a0/2c/83/a02c83b8809a3d8a6c40b800cb3869d7.jpg" alt="Watches men" />
            </div>
            <div className="col-span-1 row-span-2">
                <img className="w-full h-full object-cover rounded-lg overflow-hidden" src="https://www.karagiri.com/cdn/shop/products/kanjivaram-saree-nightshade-purple-kanjivaram-saree-silk-saree-online-30933858713793.jpg?v=1754985637" alt="silk saree" />
            </div>
            <div className="col-span-1 row-span-1">
                <img className="w-full h-full object-fit rounded-md overflow-hidden" src="https://i.pinimg.com/736x/95/21/1c/95211c86b36e999bcc00a7de384a7f57.jpg" alt="Intricate traditional jewelry detail" />
            </div>
            <div className="col-span-1 ">
                <img className="w-full h-full object-fit rounded-md overflow-hidden" src="https://i.pinimg.com/1200x/8f/76/32/8f76326bed50b682b11cd6da85d61444.jpg" alt="Men shoes" />
            </div>
            <div className="col-span-1 row-span-1">
                <img className="w-full h-full object-fit rounded-lg overflow-hidden" src="https://i.pinimg.com/736x/2c/b8/5e/2cb85e6429d8ceb161bd497a0267abe7.jpg" alt="Women shoes" />
            </div>
        </div>
    );
};

export default HomeCategoryGrid;
