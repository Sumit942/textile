import React, { useState, useEffect } from 'react';
import { 
    Table, 
    TableBody, 
    TableCell, 
    TableContainer, 
    TableHead, 
    TableRow, 
    Paper,
    IconButton,
    Collapse,
    Box,
    Typography
} from '@mui/material';
import { KeyboardArrowDown, KeyboardArrowUp } from '@mui/icons-material';
import { fetchAllOrders } from '../service/companyYarnOrder';


const Row = (props) => {
    const { row } = props;
    const [open, setOpen] = useState(false);

    return (
        <>
            <TableRow>
                <TableCell>
                    <IconButton
                        size="small"
                        onClick={() => setOpen(!open)}
                    >
                        {open ? <KeyboardArrowUp /> : <KeyboardArrowDown />}
                    </IconButton>
                </TableCell>
                <TableCell>{row.orderNo}</TableCell>
                <TableCell>{new Date(row.orderDt).toLocaleDateString()}</TableCell>
            </TableRow>
            <TableRow>
                <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
                    <Collapse in={open} timeout="auto" unmountOnExit>
                        <Box sx={{ margin: 1 }}>
                            <Table size="small">
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Item Name</TableCell>
                                        <TableCell>Quantity</TableCell>
                                        <TableCell>Quantity Allocated</TableCell>
                                        <TableCell>Quantity Left</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {row.yarnOrderItems?.map((item, index) => (
                                        <TableRow key={index}>
                                            <TableCell>{item.yarn?.type}</TableCell>
                                            <TableCell>{item.quantity}</TableCell>
                                            <TableCell>{item.qtyAllocated}</TableCell>
                                            <TableCell>{item.qtyUsed}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </Box>
                    </Collapse>
                </TableCell>
            </TableRow>
        </>
    );
};

const CompanyYarnOrderList = () => {
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        const getOrders = async () => {
            try {
                const response = await fetchAllOrders();
                setOrders(response.data);
            } catch (error) {
                console.error('Error fetching orders:', error);
            }
        };

        getOrders();
    }, []);

    return (
        <TableContainer component={Paper}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell />
                        <TableCell>Order No</TableCell>
                        <TableCell>Order Date</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {orders.map((order) => (
                        <Row key={order.id} row={order} />
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default CompanyYarnOrderList;