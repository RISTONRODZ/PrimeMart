import {
    Button,
    Divider,
    FormControl,
    FormControlLabel,
    FormLabel,
    Radio,
    RadioGroup,
} from "@mui/material";
import {type ChangeEvent, useState } from "react";
import { colors } from "../../data/Filter/color.ts";
import { teal } from "@mui/material/colors";
import { price } from "../../data/Filter/price.ts";
import { discount } from "../../data/Filter/discount.ts";
import { brands } from "../../data/Filter/brand.ts";
import { useSearchParams } from "react-router";

const FilterSection = () => {
    const [expendColor, setExpendColor] = useState(false);
    const [expendBrand, setExpendBrand] = useState(false);

    const [searchParams, setSearchParams] = useSearchParams();

    const handleExpendBrand = () => {
        setExpendBrand(!expendBrand);
    };
    const handleExpendColor = () => {
        setExpendColor(!expendColor);
    };

    const updateFilterParams = (e: ChangeEvent<HTMLInputElement>) => {
        const { value, name } = e.target;
        if (value) {
            searchParams.set(name, value);
        } else {
            searchParams.delete(name);
        }
        setSearchParams(searchParams);
    };

    const clearAllFilters = () => {
        const keysToDelete: string[] = [];
        searchParams.forEach((_, key) => {
            keysToDelete.push(key);
        });
        keysToDelete.forEach((key) => {
            searchParams.delete(key);
        });
        setSearchParams(searchParams);
    };

    return (
        <div className="-z-50 space-y-5 bg-white text-slate-800">
            <div className="flex items-center justify-between h-10 px-9 lg:border-r">
                <p className="text-lg font-semibold">Filters</p>
                <Button
                    onClick={clearAllFilters}
                    size="small"
                    className="text-teal-600 cursor-pointer font-semibold"
                >
                    clear all
                </Button>
            </div>
            <Divider />
            <div className="px-9 space-y-6">
                <section>
                    <FormControl>
                        <FormLabel
                            sx={{
                                fontSize: "16px",
                                fontWeight: "bold",
                                pb: "14px",
                                color: teal[600],
                            }}
                            className="text-2xl font-semibold"
                            id="brand"
                        >
                            Brand
                        </FormLabel>
                        <RadioGroup
                            onChange={updateFilterParams}
                            aria-labelledby="brand"
                            value={searchParams.get("brand") || ""}
                            name="brand"
                        >
                            {brands
                                .slice(0, expendBrand ? brands.length : 5)
                                .map((item) => (
                                    <FormControlLabel
                                        key={item.name}
                                        value={item.value}
                                        control={<Radio size="small" />}
                                        label={item.name}
                                    />
                                ))}
                        </RadioGroup>
                    </FormControl>
                    {brands.length > 5 && (
                        <div>
                            <button
                                onClick={handleExpendBrand}
                                className="text-teal-600 cursor-pointer hover:text-teal-900 flex items-center"
                            >
                                {expendBrand ? "hide" : `+ ${brands.length - 5} more`}
                            </button>
                        </div>
                    )}
                </section>
                <Divider />
                <section>
                    <FormControl sx={{ z: 0 }}>
                        <FormLabel
                            sx={{
                                fontSize: "16px",
                                fontWeight: "bold",
                                pb: "14px",
                                color: teal[600],
                            }}
                            className="text-2xl font-semibold"
                            id="color"
                        >
                            Color
                        </FormLabel>
                        <RadioGroup
                            onChange={updateFilterParams}
                            aria-labelledby="color"
                            value={searchParams.get("color") || ""}
                            name="color"
                        >
                            {colors
                                .slice(0, expendColor ? colors.length : 5)
                                .map((item) => (
                                    <FormControlLabel
                                        sx={{ fontSize: "12px" }}
                                        key={item.name}
                                        value={item.name}
                                        control={<Radio size="small" />}
                                        label={
                                            <div className="flex items-center gap-3">
                                                <p>{item.name}</p>
                                                <span
                                                    style={{ backgroundColor: item.hex }}
                                                    className="h-5 w-5 rounded-full border"
                                                ></span>
                                            </div>
                                        }
                                    />
                                ))}
                        </RadioGroup>
                    </FormControl>
                    {colors.length > 5 && (
                        <div>
                            <button
                                onClick={handleExpendColor}
                                className="text-teal-600 cursor-pointer hover:text-teal-900 flex items-center"
                            >
                                {expendColor ? "hide" : `+ ${colors.length - 5} more`}
                            </button>
                        </div>
                    )}
                </section>
                <Divider />

                <section>
                    <FormControl>
                        <FormLabel
                            sx={{
                                fontSize: "16px",
                                fontWeight: "bold",
                                pb: "14px",
                                color: teal[600],
                            }}
                            className="text-2xl font-semibold"
                            id="price"
                        >
                            Price
                        </FormLabel>
                        <RadioGroup
                            name="price"
                            onChange={updateFilterParams}
                            aria-labelledby="price"
                            value={searchParams.get("price") || ""}
                        >
                            {price.map((item) => (
                                <FormControlLabel
                                    key={item.name}
                                    value={item.value}
                                    control={<Radio size="small" />}
                                    label={item.name}
                                />
                            ))}
                        </RadioGroup>
                    </FormControl>
                </section>
                <Divider />
                <section>
                    <FormControl>
                        <FormLabel
                            sx={{
                                fontSize: "16px",
                                fontWeight: "bold",
                                pb: "14px",
                                color: teal[600],
                            }}
                            className="text-2xl font-semibold"
                            id="discount"
                        >
                            Discount
                        </FormLabel>
                        <RadioGroup
                            name="discount"
                            onChange={updateFilterParams}
                            aria-labelledby="discount"
                            value={searchParams.get("discount") || ""}
                        >
                            {discount.map((item) => (
                                <FormControlLabel
                                    key={item.name}
                                    value={item.value}
                                    control={<Radio size="small" />}
                                    label={item.name}
                                />
                            ))}
                        </RadioGroup>
                    </FormControl>
                </section>
            </div>
        </div>
    );
};

export default FilterSection;