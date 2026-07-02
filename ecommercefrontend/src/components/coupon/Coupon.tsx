import { useState } from 'react';
import { Box, Typography, TextField, Button, Chip, Stack } from '@mui/material';
import LocalOfferIcon from '@mui/icons-material/LocalOffer';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';

export interface CouponResult {
    code: string;
    discountType: 'percentage' | 'flat';
    discountValue: number;
}

interface CouponProps {
    onApply: (coupon: CouponResult) => void;
    onRemove: () => void;
}

const VALID_COUPONS: Record<string, Omit<CouponResult, 'code'>> = {
    SAVE10: { discountType: 'percentage', discountValue: 10 },
    FLAT50: { discountType: 'flat', discountValue: 50 },
    PRIMEMART20: { discountType: 'percentage', discountValue: 20 },
};

const Coupon = ({ onApply, onRemove }: CouponProps) => {
    const [input, setInput] = useState('');
    const [applied, setApplied] = useState<CouponResult | null>(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleApply = () => {
        const code = input.trim().toUpperCase();
        if (!code) {
            setError('Enter a coupon code.');
            return;
        }

        setLoading(true);
        setError('');

        setTimeout(() => {
            const match = VALID_COUPONS[code];
            if (match) {
                const result: CouponResult = { code, ...match };
                setApplied(result);
                onApply(result);
                setInput('');
            } else {
                setError('Invalid or expired coupon code.');
            }
            setLoading(false);
        }, 500);
    };

    const handleRemove = () => {
        setApplied(null);
        setError('');
        onRemove();
    };

    const discountLabel = applied
        ? applied.discountType === 'percentage'
            ? `${applied.discountValue}% off`
            : `₹${applied.discountValue} off`
        : '';

    return (
        <Box sx={{ mt: 2 }}>
            <Stack direction="row" spacing={1} sx={{ mb: 1, alignItems: 'center' }}>
                <LocalOfferIcon sx={{ fontSize: 16, color: '#1447e6' }} />
                <Typography variant="body2" sx={{ fontWeight: 600, color: 'text.primary' }}>
                    Apply Coupon
                </Typography>
            </Stack>

            {applied ? (
                <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
                    <Chip
                        icon={<CheckCircleIcon sx={{ fontSize: 16 }} />}
                        label={`${applied.code} — ${discountLabel}`}
                        onDelete={handleRemove}
                        size="small"
                        sx={{
                            color: '#e8f5e9',
                            color: '#2e7d32',
                            fontWeight: 600,
                            '& .MuiChip-deleteIcon': { color: '#2e7d32' },
                            '& .MuiChip-icon': { color: '#2e7d32' },
                        }}
                    />
                    <Typography variant="caption" sx={{ color: 'success.main', fontWeight: 500 }}>
                        Coupon applied!
                    </Typography>
                </Stack>
            ) : (
                <Stack direction="row" spacing={1}>
                    <TextField
                        size="small"
                        placeholder="Enter code"
                        value={input}
                        onChange={(e) => {
                            setInput(e.target.value);
                            setError('');
                        }}
                        onKeyDown={(e) => e.key === 'Enter' && handleApply()}
                        error={!!error}
                        helperText={error}
                        sx={{
                            flexGrow: 1,
                            '& .MuiInputBase-input': { textTransform: 'uppercase', fontSize: 13 },
                            '& .MuiOutlinedInput-root': {
                                borderRadius: 1.5,
                                '&.Mui-focused fieldset': { borderColor: '#1447e6' },
                            },
                        }}
                    />
                    <Button
                        variant="outlined"
                        size="small"
                        onClick={handleApply}
                        disabled={loading}
                        sx={{
                            borderColor: '#1447e6',
                            color: '#1447e6',
                            fontWeight: 600,
                            textTransform: 'none',
                            borderRadius: 1.5,
                            whiteSpace: 'nowrap',
                            '&:hover': { color: '#eef1fd', borderColor: '#1447e6' },
                        }}
                    >
                        {loading ? 'Checking…' : 'Apply'}
                    </Button>
                </Stack>
            )}
        </Box>
    );
};

export default Coupon;