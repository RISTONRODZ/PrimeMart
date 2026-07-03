import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { Button, FormControl, Menu, MenuItem, Select, styled } from "@mui/material";
import type { SelectChangeEvent } from "@mui/material/Select";
interface Seller {
    id: number;
    sellerName: string;
    email: string;
    mobile: string;
    gstin: string;
    accountStatus: string;
    businessDetails?: {
        businessName: string;
    };
}

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
    const [sellers] = React.useState<{ sellers: Seller[] }>({ sellers: [] });

    const [accountStatus, setAccountStatus] = React.useState("ACTIVE");
    const [anchorEl, setAnchorEl] = React.useState<{ [key: number]: HTMLElement | null }>({});

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
        console.log(`Updating ${sellerId} to ${status}`);
        handleClose(sellerId);
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

            <TableContainer component={Paper} className="overflow-x-auto">
                <Table sx={{ minWidth: 300 }}>
                    <TableHead>
                        <TableRow>
                            <StyledTableCell>Seller Name</StyledTableCell>
                            <StyledTableCell>Email</StyledTableCell>
                            <StyledTableCell>Mobile</StyledTableCell>
                            <StyledTableCell>GSTIN</StyledTableCell>
                            <StyledTableCell>Business Name</StyledTableCell>
                            <StyledTableCell align="right">Account Status</StyledTableCell>
                            <StyledTableCell align="right">Change Status</StyledTableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {sellers.sellers?.map((seller) => (
                            <StyledTableRow key={seller.id}>
                                <StyledTableCell>{seller.sellerName}</StyledTableCell>
                                <StyledTableCell>{seller.email}</StyledTableCell>
                                <StyledTableCell>{seller.mobile}</StyledTableCell>
                                <StyledTableCell>{seller.gstin}</StyledTableCell>
                                <StyledTableCell>{seller.businessDetails?.businessName}</StyledTableCell>
                                <StyledTableCell align="right">{seller.accountStatus}</StyledTableCell>
                                <StyledTableCell align="right">
                                    <Button onClick={(e) => handleClick(e, seller.id)}>
                                        Change Status
                                    </Button>
                                    <Menu
                                        anchorEl={anchorEl[seller.id]}
                                        open={Boolean(anchorEl[seller.id])}
                                        onClose={() => handleClose(seller.id)}
                                    >
                                        {accountStatuses.map((status) => (
                                            <MenuItem
                                                key={status.status}
                                                onClick={() => handleUpdateSellerAccountStatus(seller.id, status.status)}
                                            >
                                                {status.title}
                                            </MenuItem>
                                        ))}
                                    </Menu>
                                </StyledTableCell>
                            </StyledTableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </>
    );
}