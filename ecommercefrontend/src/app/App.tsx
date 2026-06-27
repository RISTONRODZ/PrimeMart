import { ThemeProvider } from "@mui/material";
import customTheme from "./theme/customTheme.tsx";
import Navbar from "../components/layout/Navbar.tsx";
import Home from "../features/customer/home/Home.tsx";


const App = () => {
    return (
        <ThemeProvider theme={customTheme}>
            <div>
                <Navbar />
                <Home />
            </div>
        </ThemeProvider>
    );
};

export default App;
