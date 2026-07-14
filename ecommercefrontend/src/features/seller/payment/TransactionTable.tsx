import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TablePagination from '@mui/material/TablePagination';
import Paper from '@mui/material/Paper';
import Chip from '@mui/material/Chip';
import CircularProgress from '@mui/material/CircularProgress';
import { useAppDispatch, useAppSelector } from "../../../state/hooks.ts";
import { fetchTransactionsBySeller } from "../../../state/slice/TransactionSlice.ts";
import type { Transaction } from "../../../types/TransactionTypes.ts";

const paymentStatusColor: Record<string, { color: string; label: string }> = {
    PENDING: { color: '#FFA500', label: 'PENDING' },
    SUCCESS: { color: '#32CD32', label: 'SUCCESS' },
    FAILED: { color: '#FF0000', label: 'FAILED' },
    CANCELLED: { color: '#FF0000', label: 'CANCELLED' },
    REFUNDED: { color: '#1E90FF', label: 'REFUNDED' },
};

export default function TransactionTable() {
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(5);
    const { transaction } = useAppSelector(store => store);
    const dispatch = useAppDispatch();

    React.useEffect(() => {
        dispatch(fetchTransactionsBySeller(localStorage.getItem("jwt") || ""));
    }, [dispatch]);

    function readableDateTime(date: string) {
        const d = new Date(date);
        if (isNaN(d.getTime())) return "Invalid date";
        return d.toLocaleString("en-IN", {
            day: "2-digit", month: "short", year: "numeric",
            hour: "2-digit", minute: "2-digit", hour12: true,
        });
    }

    const handleChangePage = (_event: unknown, newPage: number) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const transactions = transaction.transactions;

    const paginatedTransactions = transactions.slice(
        page * rowsPerPage,
        page * rowsPerPage + rowsPerPage
    );

    if (transaction.loading) {
        return (
            <div className='flex justify-center items-center py-10'>
                <CircularProgress />
            </div>
        );
    }

    if (transaction.error) {
        return (
            <div className='text-red-600 font-medium py-6 text-center'>
                {transaction.error}
            </div>
        );
    }

    return (
        <>
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 700 }} aria-label="customized table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Date</TableCell>
                            <TableCell>Order</TableCell>
                            <TableCell>Seller</TableCell>
                            <TableCell>Payment Status</TableCell>
                            <TableCell align="right">Amount</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {paginatedTransactions.map((item: Transaction) => {
                            const statusInfo = paymentStatusColor[item.paymentStatus];
                            return (
                                <TableRow key={item.id}>
                                    <TableCell align="left">
                                        <div className='space-y-1'>
                                            <h1 className='font-medium'>{readableDateTime(item.date).split("at")[0]}</h1>
                                            <h1 className='text-xs text-gray-600 font-semibold'>{readableDateTime(item.date).split("at")[1]}</h1>
                                        </div>
                                    </TableCell>
                                    <TableCell component="th" scope="row">
                                        <div className='space-y-1'>
                                            <h1>Tracking ID: <strong>{item.orderTrackingId}</strong></h1>
                                            <h1 className='text-xs text-gray-600'>Order #{item.orderId}</h1>
                                        </div>
                                    </TableCell>
                                    <TableCell>
                                        {item.sellerName}
                                    </TableCell>
                                    <TableCell>
                                        {statusInfo ? (
                                            <Chip
                                                label={statusInfo.label}
                                                size="small"
                                                sx={{
                                                    backgroundColor: statusInfo.color,
                                                    color: '#fff',
                                                    fontWeight: 600,
                                                }}
                                            />
                                        ) : (
                                            <Chip label={item.paymentStatus} size="small" />
                                        )}
                                    </TableCell>
                                    <TableCell align="right">
                                        ₹{item.totalSellingPrice}
                                    </TableCell>
                                </TableRow>
                            );
                        })}
                    </TableBody>
                </Table>
                <TablePagination
                    rowsPerPageOptions={[5, 10, 25]}
                    component="div"
                    count={transactions.length}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onPageChange={handleChangePage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />
            </TableContainer>
        </>
    );
}