import * as React from 'react';
import { useEffect, useState } from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { Button, FormControl, Menu, MenuItem, Select, styled, Alert, CircularProgress, IconButton, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions } from "@mui/material";
import { Delete } from "@mui/icons-material";
import type { SelectChangeEvent } from "@mui/material/Select";
import { useAppDispatch, useAppSelector } from "../../../state/hooks.ts";
import { fetchSellers, updateSellerStatus, deleteSeller } from "../../../state/admin/SellerSlice.ts";
// import type { Seller } from "../../../types/SellerTypes.ts";
import { useSnackbar } from "../../../components/ui/Snackbar.tsx";

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
    '&:nth-of-type(odd)': {
        backgroundColor: theme.palette.action.hover,
    },
    '&:last-child td, &:last-child th': {
        border: 0,
    },
}));

const accountStatuses = [
    { status: 'PENDING_VERIFICATION', title: 'Pending Verification' },
    { status: 'ACTIVE', title: 'Active' },
    { status: 'SUSPENDED', title: 'Suspended' },
    { status: 'DEACTIVATED', title: 'Deactivated' },
    { status: 'BANNED', title: 'Banned' },
    { status: 'CLOSED', title: 'Closed' }
];

export default function SellersTable() {
    const dispatch = useAppDispatch();
    const { showSnackbar } = useSnackbar();
    const adminSeller = useAppSelector((store) => store.adminSeller);
    const { sellers, loading, error, statusUpdated, sellerDeleted } = adminSeller;

    const [accountStatus, setAccountStatus] = React.useState("ACTIVE");
    const [anchorEl, setAnchorEl] = React.useState<{ [key: number]: HTMLElement | null }>({});
    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
    const [sellerToDelete, setSellerToDelete] = useState<number | null>(null);

    useEffect(() => {
        dispatch(fetchSellers(accountStatus));
    }, [dispatch, accountStatus, statusUpdated, sellerDeleted]);

    const handleAccountStatusChange = (event: SelectChangeEvent<string>) => {
        setAccountStatus(event.target.value);
    };

    const handleClick = (event: React.MouseEvent<HTMLButtonElement>, sellerId: number) => {
        setAnchorEl((prev) => ({ ...prev, [sellerId]: event.currentTarget }));
    };

    const handleClose = (sellerId: number) => {
        setAnchorEl((prev) => ({ ...prev, [sellerId]: null }));
    };

    const handleUpdateSellerAccountStatus = (sellerId: number, status: string) => {
        dispatch(updateSellerStatus({ sellerId, status }));
        handleClose(sellerId);
    };

    const handleDeleteSeller = (sellerId: number) => {
        setSellerToDelete(sellerId);
        setDeleteDialogOpen(true);
    };

    const handleConfirmDelete = async () => {
        if (sellerToDelete) {
            try {
                const result = await dispatch(deleteSeller(sellerToDelete));
                
                // Check Redux state for errors
                const currentState = adminSeller;
                
                if (deleteSeller.fulfilled.match(result)) {
                    showSnackbar("Seller deleted successfully", "success");
                } else {
                    console.error("Delete seller error:", result);
                    console.error("Current error state:", currentState.error);
                    
                    // Check both the result payload and the Redux state error
                    const errorMessage = (result.payload as string) || currentState.error || "Failed to delete seller";
                    const errorString = String(errorMessage).toLowerCase();
                    
                    if (errorString.includes("foreign key") || errorString.includes("constraint") || errorString.includes("seller_report")) {
                        showSnackbar("Cannot delete seller: Seller has associated reports. Please delete reports first.", "error");
                    } else {
                        showSnackbar(errorMessage, "error");
                    }
                }
            } catch (error: any) {
                console.error("Unexpected error in handleConfirmDelete:", error);
                const errorString = String(error?.message || error).toLowerCase();
                
                if (errorString.includes("foreign key") || errorString.includes("constraint") || errorString.includes("seller_report")) {
                    showSnackbar("Cannot delete seller: Seller has associated reports. Please delete reports first.", "error");
                } else {
                    showSnackbar(error?.message || "Failed to delete seller", "error");
                }
            }
        }
        setDeleteDialogOpen(false);
        setSellerToDelete(null);
    };

    const handleCancelDelete = () => {
        setDeleteDialogOpen(false);
        setSellerToDelete(null);
    };

    return (
        <>
            <div className='pb-4 sm:pb-5 w-full sm:w-60'>
                <FormControl color='primary' fullWidth>
                    <Select
                        value={accountStatus}
                        onChange={handleAccountStatusChange}
                    >
                        {accountStatuses.map((status) => (
                            <MenuItem key={status.status} value={status.status}>{status.title}</MenuItem>
                        ))}
                    </Select>
                </FormControl>
            </div>

            {error && (
                <Alert severity="error" className="mb-4">{error}</Alert>
            )}

            {loading ? (
                <div className="flex justify-center items-center py-10">
                    <CircularProgress />
                </div>
            ) : (
                <TableContainer component={Paper} className="overflow-x-auto">
                    <Table sx={{ minWidth: 300 }}>
                        <TableHead>
                            <TableRow>
                                <StyledTableCell>Seller Name</StyledTableCell>
                                <StyledTableCell>Email</StyledTableCell>
                                <StyledTableCell>Mobile</StyledTableCell>
                                <StyledTableCell>GSTIN</StyledTableCell>
                                {/*<StyledTableCell>Business Name</StyledTableCell>*/}
                                <StyledTableCell align="right">Account Status</StyledTableCell>
                                <StyledTableCell align="right">Change Status</StyledTableCell>
                                {/*<StyledTableCell align="right">Delete</StyledTableCell>*/}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {Array.isArray(sellers) && sellers.length > 0 ? (
                                sellers.map((seller) => (
                                    <StyledTableRow key={seller.id}>
                                        <StyledTableCell>{seller.sellerName}</StyledTableCell>
                                        <StyledTableCell>{seller.email}</StyledTableCell>
                                        <StyledTableCell>{seller.mobile}</StyledTableCell>
                                        <StyledTableCell>{seller.gstin}</StyledTableCell>
                                        {/*<StyledTableCell>{seller.businessDetails?.businessName}</StyledTableCell>*/}
                                        <StyledTableCell align="right">{seller.accountStatus}</StyledTableCell>
                                        <StyledTableCell align="right">
                                            <Button onClick={(e) => handleClick(e, seller.id || 0)}>
                                                Change Status
                                            </Button>
                                            <Menu
                                                anchorEl={anchorEl[seller.id || 0]}
                                                open={Boolean(anchorEl[seller.id || 0])}
                                                onClose={() => handleClose(seller.id || 0)}
                                            >
                                                {accountStatuses.map((status) => (
                                                    <MenuItem
                                                        key={status.status}
                                                        onClick={() => handleUpdateSellerAccountStatus(seller.id || 0, status.status)}
                                                    >
                                                        {status.title}
                                                    </MenuItem>
                                                ))}
                                            </Menu>
                                        </StyledTableCell>
                                        {/*<StyledTableCell align="right">*/}
                                        {/*    <IconButton onClick={() => handleDeleteSeller(seller.id || 0)} color="error">*/}
                                        {/*        <Delete />*/}
                                        {/*    </IconButton>*/}
                                        {/*</StyledTableCell>*/}
                                    </StyledTableRow>
                                ))
                            ) : (
                                <TableRow>
                                    <TableCell colSpan={8} align="center">
                                        No sellers found for this status
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}

            <Dialog
                open={deleteDialogOpen}
                onClose={handleCancelDelete}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">
                    Delete Seller
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        Are you sure you want to delete this seller permanently? This action cannot be undone.
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
}