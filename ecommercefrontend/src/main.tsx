import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router";
import { Provider } from "react-redux";
import "./index.css";
import App from "./app/App";
import ScrollToTop from "./features/ScrollToTop";
import store from "./state/slice/Store.ts";

createRoot(document.getElementById("root")!).render(
    <StrictMode>
        <Provider store={store}>
            <BrowserRouter>
                <ScrollToTop />
                <App />
            </BrowserRouter>
        </Provider>
    </StrictMode>
);