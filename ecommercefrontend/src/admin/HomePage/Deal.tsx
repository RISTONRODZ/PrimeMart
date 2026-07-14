import { Button } from "@mui/material";
import { useState } from "react";
import DealTable from "./DealTable";
import DealCategoryTable from "./DealCategoryTable";
import CreateDealForm from "./CreateDealForm";

const tabs = [
    { name: "Deals" },
    { name: "Create Deal" },
];

const Deal = () => {
    const [activeTab, setActiveTab] = useState(tabs[0].name);

    return (
        <div>
            <div className="flex flex-wrap gap-2 sm:gap-4">
                {tabs.map((item) => (
                    <Button
                        key={item.name}
                        onClick={() => setActiveTab(item.name)}
                        variant={activeTab === item.name ? "contained" : "outlined"}
                        size={window.innerWidth < 640 ? "small" : "medium"}
                    >
                        {item.name}
                    </Button>
                ))}
            </div>

            <div className="mt-4 sm:mt-5">
                {activeTab === "Deals" ? (
                    <DealTable />
                )  : (
                    <CreateDealForm />
                )}
            </div>
        </div>
    );
};

export default Deal;