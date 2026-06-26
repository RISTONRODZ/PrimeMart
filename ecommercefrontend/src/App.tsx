import {Button} from "@mui/material";

const App = () => {
    return (
        <div >
           <Button sx={{backgroundColor: '#cde300',borderRadius: '1rem',padding:'4px',margin:'5px'}} onClick={() =>alert("Hi there")}>Code With Riston</Button>
        </div>
    );
};

export default App;