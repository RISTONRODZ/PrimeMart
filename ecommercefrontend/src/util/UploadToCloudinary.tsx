export const uploadToCloudinary = async (pics: File) => {
    const cloudName = import.meta.env.VITE_CLOUDINARY_CLOUD_NAME;
    const uploadPreset = import.meta.env.VITE_CLOUDINARY_UPLOAD_PRESET;

    const data = new FormData();
    data.append("file", pics);
    data.append("upload_preset", uploadPreset);

    const res = await fetch(
        `https://api.cloudinary.com/v1_1/${cloudName}/image/upload`,
        {
            method: "POST",
            body: data,
        }
    );

    if (!res.ok) {
        throw new Error("Image upload failed");
    }

    const fileData = await res.json();
    return fileData.secure_url;
};