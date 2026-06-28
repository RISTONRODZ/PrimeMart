import { ThemeProvider } from "@mui/material";
import customTheme from "./theme/customTheme.tsx";
import Navbar from "../components/layout/Navbar.tsx";
import AppRoutes from "../AppRoutes.tsx";
import Footer from "../components/layout/Footer.tsx";


const App = () => {
    return (
        <ThemeProvider theme={customTheme}>
            <div>
                <Navbar />
                <AppRoutes />
                <Footer/>
            </div>
        </ThemeProvider>
    );
};

export default App;
