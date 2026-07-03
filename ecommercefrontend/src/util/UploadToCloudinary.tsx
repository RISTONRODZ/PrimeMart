import { API_URL } from "../config/Api";

export const uploadToCloudinary = async (pics: File, folder: 'products' | 'reviews') => {
    const jwt = localStorage.getItem("jwt");
    const data = new FormData();
    data.append("file", pics);
    data.append("folder", folder);

    const res = await fetch(`${API_URL}/upload/image`, {
        method: "POST",
        headers: {
            Authorization: `Bearer ${jwt}`,
        },
        body: data,
    });

    if (!res.ok) {
        const errorData = await res.text().catch(() => "Unknown error");
        throw new Error(`Image upload failed: ${res.statusText}`);
    }

    return await res.text();
};