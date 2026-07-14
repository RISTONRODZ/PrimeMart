import * as React from 'react';
import { useEffect } from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { Button, FormControl, Menu, MenuItem, Select, styled, Alert, CircularProgress, IconButton } from "@mui/material";
import { Delete } from "@mui/icons-material";
import type { SelectChangeEvent } from "@mui/material/Select";
import { useAppDispatch, useAppSelector } from "../../../state/hooks.ts";
import { fetchSellers, updateSellerStatus, deleteSeller } from "../../../state/admin/SellerSlice.ts";
import type { Seller } from "../../../types/SellerTypes.ts";

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
    const adminSeller = useAppSelector((store) => store.adminSeller);
    const { sellers, loading, error, statusUpdated } = adminSeller;

    const [accountStatus, setAccountStatus] = React.useState("ACTIVE");
    const [anchorEl, setAnchorEl] = React.useState<{ [key: number]: HTMLElement | null }>({});

    useEffect(() => {
        dispatch(fetchSellers(accountStatus));
    }, [dispatch, accountStatus, statusUpdated]);

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
        if (window.confirm("Are you sure you want to delete this seller permanently? This action cannot be undone.")) {
            dispatch(deleteSeller(sellerId));
        }
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
                                <StyledTableCell align="right">Delete</StyledTableCell>
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
                                        <StyledTableCell align="right">
                                            <IconButton onClick={() => handleDeleteSeller(seller.id || 0)} color="error">
                                                <Delete />
                                            </IconButton>
                                        </StyledTableCell>
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
        </>
    );
}