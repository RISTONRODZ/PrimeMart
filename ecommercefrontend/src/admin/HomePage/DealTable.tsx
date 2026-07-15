import { Box, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, IconButton, Modal, Paper, styled, Table, TableBody, TableCell, tableCellClasses, TableContainer, TableHead, TableRow } from '@mui/material';
import {useEffect, useState} from 'react';
import EditIcon from '@mui/icons-material/Edit';
import { Delete } from '@mui/icons-material';
import {useAppDispatch, useAppSelector} from "../../state/hooks.ts";
import {getAllDeals, deleteDeal} from "../../state/admin/DealSlice.ts";
import UpdateDealForm from "./UpdateDealForm.tsx";

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
    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
    const [dealToDelete, setDealToDelete] = useState<number>();

    const handleOpen = (id: number) => () => {
        setSelectedDealId(id);
        setOpen(true);
    };
    const dispatch = useAppDispatch();
    const {deal} = useAppSelector(store=>store)
    const deals = deal.deals || [];
    console.log("Deals from store:", deals);
    console.log("Deal state:", deal);
    const handleClose = () => setOpen(false);

    const handleDelete = (id: number) => () => {
        setDealToDelete(id);
        setDeleteDialogOpen(true);
    };

    const handleConfirmDelete = () => {
        if (dealToDelete) {
            dispatch(deleteDeal(dealToDelete));
        }
        setDeleteDialogOpen(false);
        setDealToDelete(undefined);
    };

    const handleCancelDelete = () => {
        setDeleteDialogOpen(false);
        setDealToDelete(undefined);
    };
    useEffect(() => {
        dispatch(getAllDeals());
    }, []);
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
                        {Array.isArray(deals) && deals.map((deal, index: number) => {
                            console.log("Mapping deal:", deal);
                            return (
                            <StyledTableRow key={deal.id || index}>
                                <StyledTableCell component="th" scope="row">{index + 1}</StyledTableCell>
                                <StyledTableCell>
                                    {deal.homeCategory?.imageUrl ? (
                                        <img className="w-20 rounded-md" src={deal.homeCategory.imageUrl} alt="category" />
                                    ) : (
                                        <span className="text-gray-400">No image</span>
                                    )}
                                </StyledTableCell>
                                <StyledTableCell>
                                    {deal.homeCategory?.name || "N/A"}
                                </StyledTableCell>
                                <StyledTableCell>{deal.discount}%</StyledTableCell>
                                <StyledTableCell align="right">
                                    <IconButton onClick={handleOpen(deal.id || 0)}>
                                        <EditIcon className="text-orange-400" />
                                    </IconButton>
                                </StyledTableCell>
                                <StyledTableCell align="right">
                                    <IconButton onClick={handleDelete(deal.id || 0)}>
                                        <Delete className="text-red-600" />
                                    </IconButton>
                                </StyledTableCell>
                            </StyledTableRow>
                            );
                        })}
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
                    {selectedDealId && <UpdateDealForm id={selectedDealId} onClose={handleClose} />}
                </Box>
            </Modal>

            <Dialog
                open={deleteDialogOpen}
                onClose={handleCancelDelete}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">
                    Delete Deal
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        Are you sure you want to delete this deal? This action cannot be undone.
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCancelDelete} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={handleConfirmDelete} color="error" variant="contained" autoFocus>
                        Delete
                    </Button>
                </DialogActions>
            </Dialog>
        </>
    );
};

export default DealsTable;