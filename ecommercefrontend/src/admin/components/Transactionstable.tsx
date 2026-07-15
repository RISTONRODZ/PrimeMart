import { useEffect, useState, useMemo } from "react";
import {
    Box,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography,
    Chip,
    IconButton,
    Tooltip,
    TextField,
    InputAdornment,
    Alert,
    Skeleton,
    Stack,
} from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import SearchIcon from "@mui/icons-material/Search";
import ReceiptLongIcon from "@mui/icons-material/ReceiptLong";
import { useSelector } from "react-redux";
import {useAppDispatch} from "../../state/hooks.ts";
import {fetchAllTransactions} from "../../state/slice/TransactionSlice.ts";
import type {RootState} from "../../state/slice/Store.ts";
const STATUS_COLOR: Record<
    string,
    "success" | "warning" | "error" | "default" | "info"
> = {
    PAID: "success",
    COMPLETED: "success",
    SUCCESS: "success",
    PENDING: "warning",
    UNPAID: "warning",
    FAILED: "error",
    CANCELLED: "error",
    REFUNDED: "info",
    UNKNOWN: "default",
};

function formatCurrency(value: number): string {
    return new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
        maximumFractionDigits: 0,
    }).format(value ?? 0);
}

function formatDate(value: string): string {
    if (!value) return "—";
    const d = new Date(value);
    if (Number.isNaN(d.getTime())) return value;
    return d.toLocaleString(undefined, {
        year: "numeric",
        month: "short",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
    });
}
export default function TransactionsTable() {
    const dispatch = useAppDispatch();
    const { transactions, loading, error } = useSelector((state: RootState) => state.transaction);
    const [search, setSearch] = useState<string>("");

    useEffect(() => {
        dispatch(fetchAllTransactions());
    }, [dispatch]);

    const filtered = useMemo(() => {
        const q = search.trim().toLowerCase();
        if (!q) return transactions;
        return transactions.filter((t: any) =>
            [t.orderCode, t.sellerName, t.paymentStatus, String(t.id)]
                .filter(Boolean)
                .some((field) => field!.toLowerCase().includes(q))
        );
    }, [search, transactions]);

    return (
        <Box sx={{ p: { xs: 2, sm: 3 } }}>
            <Stack
               sx={{direction: { xs: "column", sm: "row" }, justifyContent: "space-between", mb: 2, gap: 2}}
            >
                <Stack sx={{direction:"row" ,spacing:1.5, alignItems:"center"}}>
                    <ReceiptLongIcon color="primary" />
                    <Box>
                        <Typography variant="h6" sx={{ fontWeight: 600 }}>
                            Transactions
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            {loading ? "Loading…" : `${filtered.length} of ${transactions.length} shown`}
                        </Typography>
                    </Box>
                </Stack>

                <Stack sx={{ width: { xs: "100%", sm: "auto" } }} direction="row" spacing={1}>
                    <TextField
                        size="small"
                        placeholder="Search order, seller, status…"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        slotProps={{
                            input: {
                                startAdornment: (
                                    <InputAdornment position="start">
                                        <SearchIcon fontSize="small" />
                                    </InputAdornment>
                                ),
                            },
                        }}
                    />
                    <Tooltip title="Refresh">
                        <IconButton
                            onClick={() => dispatch(fetchAllTransactions())}
                            disabled={loading}
                        >
                            <RefreshIcon />
                        </IconButton>
                    </Tooltip>
                </Stack>
            </Stack>

            {error && (
                <Alert severity="error" sx={{ mb: 2 }}>
                    {error}
                </Alert>
            )}

            <TableContainer component={Paper} variant="outlined">
                <Table size="medium">
                    <TableHead>
                        <TableRow sx={{ "& th": { fontWeight: 600, backgroundColor: "grey.50" } }}>
                            <TableCell>Transaction ID</TableCell>
                            <TableCell>Order</TableCell>
                            <TableCell>Seller</TableCell>
                            <TableCell align="right">Amount</TableCell>
                            <TableCell>Status</TableCell>
                            <TableCell>Date</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {loading &&
                            Array.from({ length: 6 }).map((_, i) => (
                                <TableRow key={`skeleton-${i}`}>
                                    {Array.from({ length: 6 }).map((__, j) => (
                                        <TableCell key={j}>
                                            <Skeleton variant="text" />
                                        </TableCell>
                                    ))}
                                </TableRow>
                            ))}

                        {!loading && filtered.length === 0 && !error && (
                            <TableRow>
                                <TableCell colSpan={6} align="center" sx={{ py: 6 }}>
                                    <Typography color="text.secondary">
                                        {transactions.length === 0
                                            ? "No transactions yet."
                                            : "No transactions match your search."}
                                    </Typography>
                                </TableCell>
                            </TableRow>
                        )}

                        {!loading &&
                            filtered.map((t: any) => (
                                <TableRow key={t.id} hover>
                                    <TableCell>{t.id}</TableCell>
                                    <TableCell>
                                        {t.orderCode ?? (t.orderId ? `${t.orderId}` : "—")}
                                    </TableCell>
                                    <TableCell>{t.sellerName ?? "—"}</TableCell>
                                    <TableCell align="right">
                                        {formatCurrency(t.totalSellingPrice)}
                                    </TableCell>
                                    <TableCell>
                                        <Chip
                                            label={t.paymentStatus}
                                            size="small"
                                            color={STATUS_COLOR[t.paymentStatus] ?? "default"}
                                            variant="outlined"
                                        />
                                    </TableCell>
                                    <TableCell>{formatDate(t.date)}</TableCell>
                                </TableRow>
                            ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
}