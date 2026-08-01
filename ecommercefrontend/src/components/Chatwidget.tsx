import { useEffect, useRef, useState } from 'react';
import { useSelector } from 'react-redux';
import {
    Avatar,
    Box,
    Fab,
    IconButton,
    Paper,
    Slide,
    Stack,
    TextField,
    Typography,
} from '@mui/material';
import ChatIcon from '@mui/icons-material/Chat';
import CloseIcon from '@mui/icons-material/Close';
import SendIcon from '@mui/icons-material/Send';
import SmartToyOutlinedIcon from '@mui/icons-material/SmartToyOutlined';
import PersonOutlineOutlinedIcon from '@mui/icons-material/PersonOutlineOutlined';
import type {RootState} from "../state/slice/Store.ts";


interface ChatMessage {
    role: 'user' | 'assistant';
    content: string;
}

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? '';

const WELCOME_MESSAGE: ChatMessage = {
    role: 'assistant',
    content: "Hi! I'm the PrimeMart assistant. Ask me about products, prices, or your recent orders.",
};

const ChatWidget = () => {
    const token = useSelector((state: RootState) => state.auth.jwt);
    const role = useSelector((state: RootState) => state.auth.role);
    const normalizedRole = role?.replace('ROLE_', '') ?? null;
    const isGuest = normalizedRole === null;
    const isCustomer = normalizedRole === 'CUSTOMER';
    const canChat = isGuest || isCustomer;

    const [open, setOpen] = useState(false);
    const [messages, setMessages] = useState<ChatMessage[]>([WELCOME_MESSAGE]);
    const [input, setInput] = useState('');
    const [isStreaming, setIsStreaming] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const scrollAnchorRef = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        scrollAnchorRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages, open]);

    if (!canChat) {
        return null;
    }

    const sendMessage = async () => {
        const question = input.trim();
        if (!question || isStreaming) {
            return;
        }

        setError(null);
        setInput('');
        setMessages((prev) => [
            ...prev,
            { role: 'user', content: question },
            { role: 'assistant', content: '' },
        ]);
        setIsStreaming(true);

        try {
            const response = await fetch(
                `${API_BASE_URL}/api/v1/ai/search?q=${encodeURIComponent(question)}`,
                {
                    method: 'GET',
                    headers: {
                        Authorization: token ? `Bearer ${token}` : '',
                    },
                    credentials: 'include',
                },
            );

            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error('AI_UNAVAILABLE');
                }
                throw new Error('Assistant request failed');
            }

            if (!response.body) {
                throw new Error('Assistant request failed');
            }

            const reader = response.body.getReader();
            const decoder = new TextDecoder();
            let accumulated = '';

            while (true) {
                const { value, done } = await reader.read();
                if (done) {
                    break;
                }
                accumulated += decoder.decode(value, { stream: true });
                setMessages((prev) => {
                    const updated = [...prev];
                    updated[updated.length - 1] = { role: 'assistant', content: accumulated };
                    return updated;
                });
            }
        } catch (err) {
            const errorMessage = err instanceof Error && err.message === 'AI_UNAVAILABLE'
                ? 'AI assistant is currently unavailable. This feature is only available in local development environments.'
                : 'Something went wrong. Please try again.';
            
            setError(errorMessage);
            console.log(err);
            setMessages((prev) => {
                const updated = [...prev];
                updated[updated.length - 1] = {
                    role: 'assistant',
                    content: err instanceof Error && err.message === 'AI_UNAVAILABLE'
                        ? "I apologize, but the AI assistant is currently disabled. This feature is only available in local development environments."
                        : "Sorry, I couldn't process that. Please try again.",
                };
                return updated;
            });
        } finally {
            setIsStreaming(false);
        }
    };

    const handleKeyDown = (event: React.KeyboardEvent<HTMLDivElement>) => {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            sendMessage();
        }
    };

    return (
        <>
            <Fab
                color="primary"
                onClick={() => setOpen((prev) => !prev)}
                sx={{
                    position: 'fixed',
                    bottom: 24,
                    right: 24,
                    zIndex: 1300,
                }}
                aria-label="Open assistant chat"
            >
                {open ? <CloseIcon /> : <ChatIcon />}
            </Fab>

            <Slide direction="up" in={open} mountOnEnter unmountOnExit>
                <Paper
                    elevation={8}
                    sx={{
                        position: 'fixed',
                        bottom: 96,
                        right: 24,
                        width: 360,
                        maxWidth: '90vw',
                        height: 520,
                        maxHeight: '75vh',
                        display: 'flex',
                        flexDirection: 'column',
                        borderRadius: 3,
                        overflow: 'hidden',
                        zIndex: 1300,
                    }}
                >
                    <Box
                        sx={{
                            px: 2,
                            py: 1.5,
                            bgcolor: 'primary.main',
                            color: 'primary.contrastText',
                        }}
                    >
                        <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
                            PrimeMart Assistant
                        </Typography>
                        <Typography variant="caption">Ask about products, prices, or your orders</Typography>
                    </Box>

                    <Box
                        sx={{
                            flex: 1,
                            overflowY: 'auto',
                            px: 2,
                            py: 2,
                            bgcolor: 'grey.50',
                        }}
                    >
                        <Stack spacing={1.5}>
                            {messages.map((message, index) => (
                                <Stack
                                    key={index}
                                    sx={{
                                        flexDirection: message.role === 'user' ? 'row-reverse' : 'row',
                                        alignItems: 'flex-start',
                                        gap: 1,
                                    }}
                                >
                                    {message.role === 'assistant' && (
                                        <Avatar sx={{ width: 28, height: 28, bgcolor: 'primary.main' }}>
                                            <SmartToyOutlinedIcon sx={{ fontSize: 16 }} />
                                        </Avatar>
                                    )}
                                    <Paper
                                        elevation={0}
                                        sx={{
                                            px: 1.5,
                                            py: 1,
                                            maxWidth: '75%',
                                            bgcolor: message.role === 'user' ? 'primary.main' : 'common.white',
                                            color: message.role === 'user' ? 'primary.contrastText' : 'text.primary',
                                            borderRadius: 2,
                                            border: message.role === 'assistant' ? '1px solid' : 'none',
                                            borderColor: 'divider',
                                        }}
                                    >
                                        <Typography variant="body2" sx={{ whiteSpace: 'pre-wrap' }}>
                                            {message.content || (isStreaming && index === messages.length - 1 ? '…' : '')}
                                        </Typography>
                                    </Paper>
                                    {message.role === 'user' && (
                                        <Avatar sx={{ width: 28, height: 28, bgcolor: 'grey.400' }}>
                                            <PersonOutlineOutlinedIcon sx={{ fontSize: 16 }} />
                                        </Avatar>
                                    )}
                                </Stack>
                            ))}
                            <div ref={scrollAnchorRef} />
                        </Stack>
                    </Box>

                    {error && (
                        <Typography variant="caption" color="error" sx={{ px: 2, pb: 0.5 }}>
                            {error}
                        </Typography>
                    )}

                    <Box
                        sx={{
                            display: 'flex',
                            alignItems: 'flex-end',
                            gap: 1,
                            px: 1.5,
                            py: 1.5,
                            borderTop: '1px solid',
                            borderColor: 'divider',
                            bgcolor: 'common.white',
                        }}
                    >
                        <TextField
                            fullWidth
                            size="small"
                            multiline
                            maxRows={3}
                            placeholder="Type your question…"
                            value={input}
                            onChange={(event) => setInput(event.target.value)}
                            onKeyDown={handleKeyDown}
                            disabled={isStreaming}
                        />
                        <IconButton
                            color="primary"
                            onClick={sendMessage}
                            disabled={isStreaming || !input.trim()}
                        >
                            <SendIcon />
                        </IconButton>
                    </Box>
                </Paper>
            </Slide>
        </>
    );
};

export default ChatWidget;