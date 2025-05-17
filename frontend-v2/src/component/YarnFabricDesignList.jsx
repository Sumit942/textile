import React, { useState, useEffect } from 'react';
import { 
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    CircularProgress,
    Typography,
    IconButton
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { fetchAllYarnFabricDesign } from '../service/yarnFabricDesign';
import EditIcon from '@mui/icons-material/Edit';

const YarnFabricDesignList = () => {
    const [designs, setDesigns] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchDesigns();
    }, []);

    const fetchDesigns = async () => {
        try {
            const response = await fetchAllYarnFabricDesign();
            setDesigns(response.data);
            setError(null);
        } catch (err) {
            setError('Error fetching fabric designs');
            console.error('Error fetching fabric designs:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleEditClick = (e, design) => {
        e.stopPropagation(); // Prevent row click event
        navigate('/yarnFabricDesign/save', {
            state: { design }
        });
    };

    if (loading) {
        return (
            <div className="flex justify-center items-center h-screen">
                <CircularProgress />
            </div>
        );
    }

    if (error) {
        return (
            <div className="text-red-500 text-center p-4">
                <Typography variant="h6">{error}</Typography>
            </div>
        );
    }

    return (
        <div className="p-4">
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Sr. No</TableCell>
                            <TableCell>Quality Name</TableCell>
                            <TableCell align="center">Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {designs.map((design, index) => (
                            <TableRow
                                key={design.id}
                                hover
                                sx={{ cursor: 'pointer' }}
                            >
                                <TableCell>{index + 1}</TableCell>
                                <TableCell>{design.qualityName}</TableCell>
                                <TableCell align="center">
                                    <IconButton
                                        color="primary"
                                        onClick={(e) => handleEditClick(e, design)}
                                        size="small"
                                    >
                                        <EditIcon />
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                        {designs.length === 0 && (
                            <TableRow>
                                <TableCell colSpan={3} align="center">
                                    No designs found
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
};

export default YarnFabricDesignList;