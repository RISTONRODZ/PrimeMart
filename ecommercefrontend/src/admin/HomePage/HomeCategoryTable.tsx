import {
    Avatar,
    Box,
    IconButton,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";

const rows = [
    {
        id: 8,
        image:
            "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=200",
        category: "women_lehenga_cholis",
        name: "",
    },
    {
        id: 9,
        image:
            "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=200",
        category: "men_formal_shoes",
        name: "",
    },
    {
        id: 10,
        image:
            "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=200",
        category: "women_lehenga_cholis",
        name: "",
    },
    {
        id: 11,
        image:
            "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200",
        category: "men_sherwanis",
        name: "",
    },
];

const HomeCategoryTable = () => {
    return (
        <Box sx={{padding: {xs: 2, sm: 3}}}>
            <Paper elevation={2}>
                <TableContainer className="overflow-x-auto">
                    <Table sx={{ minWidth: 300 }}>
                        <TableHead>
                            <TableRow
                                sx={{
                                    backgroundColor: "#000",
                                    "& th": {
                                        color: "#fff",
                                        fontWeight: 600,
                                    },
                                }}
                            >
                                <TableCell>No</TableCell>
                                <TableCell>Id</TableCell>
                                <TableCell>Image</TableCell>
                                <TableCell>Category</TableCell>
                                <TableCell>Name</TableCell>
                                <TableCell align="center">Action</TableCell>
                            </TableRow>
                        </TableHead>

                        <TableBody>
                            {rows.map((row, index) => (
                                <TableRow
                                    key={row.id}
                                    hover
                                    sx={{
                                        height: 90,
                                    }}
                                >
                                    <TableCell>{index + 1}</TableCell>

                                    <TableCell>{row.id}</TableCell>

                                    <TableCell>
                                        <Avatar
                                            variant="rounded"
                                            src={row.image}
                                            sx={{
                                                width: 50,
                                                height: 70,
                                                borderRadius: 1,
                                            }}
                                        />
                                    </TableCell>

                                    <TableCell>
                                        <Typography variant="body2">
                                            {row.category}
                                        </Typography>
                                    </TableCell>

                                    <TableCell>{row.name || "-"}</TableCell>

                                    <TableCell align="center">
                                        <IconButton color="warning">
                                            <EditIcon fontSize="small" />
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Paper>
        </Box>
    );
};

export default HomeCategoryTable;