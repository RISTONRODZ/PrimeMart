
import SellerDrawerList from "../../components/SellerDrawerList.tsx";


const SellerDashboard = () => {
    const toggleDrawer = () => {

    };
    return (
        <div className="min-h-screen text-slate-800">
            <section className="lg:flex lg:h-[90vh]">
                <div className="lg:block h-full">
                    <SellerDrawerList toggleDrawer={toggleDrawer}/>
                </div>
                <div className="p-10 w-full lg:w-[80%]  overflow-y-auto">
                   
                </div>
            </section>
        </div>
    );
};

export default SellerDashboard;