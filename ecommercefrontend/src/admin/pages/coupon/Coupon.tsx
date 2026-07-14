import { useEffect, useMemo, useState } from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Select,
    MenuItem,
    IconButton,
    FormControl,
    Chip,
    CircularProgress,
    Snackbar,
    Alert,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import dayjs from 'dayjs';
import { useAppDispatch, useAppSelector } from "../../../state/hooks.ts";
import { deleteCoupon, fetchAllCoupons } from "../../../state/customer/couponSlice.ts";

type StatusFilter = 'All' | 'Active' | 'Inactive' | 'Expired';
const statuses: StatusFilter[] = ['All', 'Active', 'Inactive', 'Expired'];

const Coupon = () => {
    const dispatch = useAppDispatch();
    const couponState = useAppSelector((state) => state.coupon);
    const coupons = couponState?.coupons || [];
    const loading = couponState?.loading || false;
    const error = couponState?.error || null;
    const jwt = useAppSelector((state) => state.auth.jwt);

    const [filter, setFilter] = useState<StatusFilter>('All');
    const [deletingId, setDeletingId] = useState<number | null>(null);

    // We only need local state for tracking local actions (like successful deletions)
    const [successSnackbar, setSuccessSnackbar] = useState<{ open: boolean; message: string }>({
        open: false,
        message: '',
    });

    useEffect(() => {
        if (jwt) dispatch(fetchAllCoupons(jwt));
    }, [dispatch, jwt]);

    useEffect(() => {
        console.log('Coupons in state:', coupons);
    }, [coupons]);

    const isExpired = (endDate: string) => dayjs(endDate).isBefore(dayjs());

    const filteredCoupons = useMemo(() => {
        const couponsArray = Array.isArray(coupons) ? coupons : [];
        return couponsArray.filter((c) => {
            const expired = isExpired(c.validityEndDate);
            switch (filter) {
                case 'Active':
                    return c.active && !expired;
                case 'Inactive':
                    return !c.active && !expired;
                case 'Expired':
                    return expired;
                default:
                    return true;
            }
        });
    }, [coupons, filter]);

    const handleDelete = async (id: number) => {
        if (!jwt) return;
        setDeletingId(id);
        const result = await dispatch(deleteCoupon({ id, jwt }));
        setDeletingId(null);
        if (deleteCoupon.fulfilled.match(result)) {
            setSuccessSnackbar({ open: true, message: 'Coupon deleted' });
        }
    };

    const statusLabel = (coupon: (typeof coupons)[number]) => {
        if (isExpired(coupon.validityEndDate)) return { label: 'Expired', color: 'default' as const };
        return coupon.active
            ? { label: 'Active', color: 'success' as const }
            : { label: 'Inactive', color: 'warning' as const };
    };

    const snackbarOpen = Boolean(error) || successSnackbar.open;
    const snackbarMessage = error || successSnackbar.message;
    const snackbarSeverity = error ? 'error' : 'success';

    const handleSnackbarClose = () => {
        if (error) {
            console.log(error)
        }
        setSuccessSnackbar((s) => ({ ...s, open: false }));
    };

    return (
        <div className="p-4 sm:p-5 flex flex-col gap-3">
            <FormControl size="small" className="mb-4 sm:mb-5 w-full sm:w-auto" fullWidth={true}>
                <Select value={filter} onChange={(e) => setFilter(e.target.value as StatusFilter)}>
                    {statuses.map((s) => (
                        <MenuItem key={s} value={s}>
                            {s}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
            <TableContainer component={Paper} className="overflow-x-auto">
                <Table sx={{ minWidth: 300 }}>
                    <TableHead sx={{ backgroundColor: '#000' }}>
                        <TableRow>
                            {['Coupon Code', 'Start Date', 'End Date', 'Min Order Value', 'Discount %', 'Status', 'Delete'].map((head) => (
                                <TableCell key={head} sx={{ color: '#fff', fontWeight: 'bold' }}>
                                    {head}
                                </TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {loading && coupons.length === 0 && (
                            <TableRow>
                                <TableCell colSpan={7} align="center" sx={{ py: 4 }}>
                                    <CircularProgress size={24} />
                                </TableCell>
                            </TableRow>
                        )}
                        {!loading && filteredCoupons.length === 0 && (
                            <TableRow>
                                <TableCell colSpan={7} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                                    No coupons found.
                                </TableCell>
                            </TableRow>
                        )}
                        {filteredCoupons.map((row) => {
                            const status = statusLabel(row);
                            return (
                                <TableRow key={row.id}>
                                    <TableCell sx={{ fontWeight: 600 }}>{row.code}</TableCell>
                                    <TableCell>{dayjs(row.validityStartDate).format('DD MMM YYYY')}</TableCell>
                                    <TableCell>{dayjs(row.validityEndDate).format('DD MMM YYYY')}</TableCell>
                                    <TableCell>₹{row.minimumOrderValue}</TableCell>
                                    <TableCell>{row.discountPercentage}%</TableCell>
                                    <TableCell>
                                        <Chip label={status.label} color={status.color} size="small" variant="outlined" />
                                    </TableCell>
                                    <TableCell>
                                        <IconButton
                                            color="error"
                                            disabled={deletingId === row.id}
                                            onClick={() => handleDelete(row.id)}
                                        >
                                            {deletingId === row.id ? <CircularProgress size={18} /> : <DeleteIcon />}
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            );
                        })}
                    </TableBody>
                </Table>
            </TableContainer>

            <Snackbar
                open={snackbarOpen}
                autoHideDuration={4000}
                onClose={handleSnackbarClose}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
            >
                <Alert severity={snackbarSeverity} variant="filled" onClose={handleSnackbarClose}>
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </div>
    );
};

export default Coupon;