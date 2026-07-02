import { useState } from 'react';
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
    FormControl
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

const data = [
    { id: 'ZOSH10', start: '2024-09-25', end: '2024-09-29', min: 699, discount: 10, status: 'Active' },
    { id: 'ZOSH11', start: '2024-09-25', end: '2024-09-29', min: 699, discount: 10, status: 'Active' },
    { id: 'ZOSH12', start: '2024-09-25', end: '2024-09-29', min: 699, discount: 10, status: 'Active' },
    { id: 'ZOSH13', start: '2024-09-25', end: '2024-09-29', min: 699, discount: 10, status: 'Active' },
];

const statuses = ['Active', 'Pending Verification', 'Suspended', 'Deactivated', 'Banned', 'Closed'];

const Coupon = () => {
    const [filter, setFilter] = useState('Active');

    return (
        <div className="p-4 sm:p-5">
            <FormControl size="small" className="mb-4 sm:mb-5 w-full sm:w-auto" fullWidth={true}>
                <Select
                    value={filter}
                    onChange={(e) => setFilter(e.target.value)}
                >
                    {statuses.map((s) => (
                        <MenuItem key={s} value={s}>{s}</MenuItem>
                    ))}
                </Select>
            </FormControl>

            <TableContainer component={Paper} className="overflow-x-auto">
                <Table sx={{ minWidth: 300 }}>
                    <TableHead sx={{ backgroundColor: '#000' }}>
                        <TableRow>
                            {['Coupon Code', 'Start Date', 'End Date', 'Min Order Value', 'Discount %', 'Status', 'Delete'].map((head) => (
                                <TableCell key={head} sx={{ color: '#fff', fontWeight: 'bold' }}>{head}</TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.map((row) => (
                            <TableRow key={row.id}>
                                <TableCell>{row.id}</TableCell>
                                <TableCell>{row.start}</TableCell>
                                <TableCell>{row.end}</TableCell>
                                <TableCell>{row.min}</TableCell>
                                <TableCell>{row.discount}</TableCell>
                                <TableCell>{row.status}</TableCell>
                                <TableCell>
                                    <IconButton color="error">
                                        <DeleteIcon />
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
};

export default Coupon;