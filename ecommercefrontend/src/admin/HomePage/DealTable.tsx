import { Box, IconButton, Modal, Paper, styled, Table, TableBody, TableCell, tableCellClasses, TableContainer, TableHead, TableRow } from '@mui/material';
import { useState } from 'react';
import EditIcon from '@mui/icons-material/Edit';
import { Delete } from '@mui/icons-material';

// Dummy component to fix "Cannot find name 'UpdateDealForm'"
const UpdateDealForm = ({ id }: { id: number }) => <div>Updating Deal {id}</div>;

// Dummy data to fix "Cannot find name 'deal'"
const dummyDeals = {
    deals: [
        { id: 1, discount: 20, category: { categoryId: "Electronics", image: "https://via.placeholder.com/50" } },
        { id: 2, discount: 50, category: { categoryId: "Fashion", image: "https://via.placeholder.com/50" } },
    ]
};

const StyledTableCell = styled(TableCell)(({ theme }) => ({
    [`&.${tableCellClasses.head}`]: {
        backgroundColor: theme.palette.common.black,
        color: theme.palette.common.white,
    },
    [`&.${tableCellClasses.body}`]: {
        fontSize: 14,
    },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
    "&:nth-of-type(odd)": {
        backgroundColor: theme.palette.action.hover,
    },
    "&:last-child td, &:last-child th": {
        border: 0,
    },
}));

const style = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: "90%",
    maxWidth: 400,
    bgcolor: "background.paper",
    boxShadow: 24,
    p: 4,
};

const DealsTable = () => {
    const [selectedDealId, setSelectedDealId] = useState<number>();
    const [open, setOpen] = useState(false);

    const handleOpen = (id: number) => () => {
        setSelectedDealId(id);
        setOpen(true);
    };

    const handleClose = () => setOpen(false);

    // Implemented missing handler
    const handleDelete = (id: number) => () => {
        console.log("Delete deal:", id);
    };

    return (
        <>
            <TableContainer component={Paper} className="overflow-x-auto">
                <Table sx={{ minWidth: 300 }} aria-label="customized table">
                    <TableHead>
                        <TableRow>
                            <StyledTableCell>No</StyledTableCell>
                            <StyledTableCell>Image</StyledTableCell>
                            <StyledTableCell>Category</StyledTableCell>
                            <StyledTableCell>Discount</StyledTableCell>
                            <StyledTableCell align="right">Edit</StyledTableCell>
                            <StyledTableCell align="right">Delete</StyledTableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {dummyDeals.deals.map((deal, index: number) => (
                            <StyledTableRow key={deal.id}>
                                <StyledTableCell component="th" scope="row">{index + 1}</StyledTableCell>
                                <StyledTableCell>
                                    <img className="w-20 rounded-md" src={deal.category.image} alt="category" />
                                </StyledTableCell>
                                <StyledTableCell>{deal.category.categoryId}</StyledTableCell>
                                <StyledTableCell>{deal.discount}%</StyledTableCell>
                                <StyledTableCell align="right">
                                    <IconButton onClick={handleOpen(deal.id)}>
                                        <EditIcon className="text-orange-400" />
                                    </IconButton>
                                </StyledTableCell>
                                <StyledTableCell align="right">
                                    <IconButton onClick={handleDelete(deal.id)}>
                                        <Delete className="text-red-600" />
                                    </IconButton>
                                </StyledTableCell>
                            </StyledTableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    {selectedDealId && <UpdateDealForm id={selectedDealId} />}
                </Box>
            </Modal>
        </>
    );
};

export default DealsTable;