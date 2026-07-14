import axios from "axios";

export const API_URL = "http://localhost:8080/api/v1";

export const api = axios.create({
    baseURL: API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem("jwt");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            console.log("401 ERROR ON:", error.config.url);
            console.log("AUTH HEADER SENT:", error.config.headers.Authorization);
            const jwt = localStorage.getItem("jwt");
            if (jwt) {
                localStorage.removeItem("jwt");
                window.location.href = "/login?session=expired";
            }
        }
        return Promise.reject(error);
    }
);