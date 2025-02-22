import React, { useEffect, useState } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, CircularProgress, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom'
import { fetchOrderList } from '../service/orderApi';

const OrderLists = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const response = await fetchOrderList();
                setOrders(response.data);
            } catch (error) {
                console.error('Error fetching orders:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchOrders();
    }, []);

    const handleDelete = (orderId) => {
        // Implement delete functionality here
        console.log('Delete order with ID:', orderId);
    };

    const handleTableClick = (event) => {
        const { target } = event;
        if (target.matches('.delete-button')) {
            const orderId = target.getAttribute('data-id');
            handleDelete(orderId);
        }
    };

    if (loading) {
        return <CircularProgress />;
    }

    return (
        <TableContainer component={Paper}>
            <Table onClick={handleTableClick}>
                <TableHead>
                    <TableRow>
                        <TableCell>Sr. No</TableCell>
                        <TableCell>Order Name</TableCell>
                        <TableCell>Order Status</TableCell>
                        <TableCell>Action</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {orders.map((order, index) => (
                        <TableRow key={order.id}>
                            <TableCell>
                                <Button onClick={() => navigate(`/edit-order/${order.id}`)}>
                                    {index + 1}
                                </Button>
                            </TableCell>
                            <TableCell>{order.order_name}</TableCell>
                            <TableCell>{order.order_status}</TableCell>
                            <TableCell>
                                <Button className="delete-button" data-id={order.id} color="secondary">
                                    Delete
                                </Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default OrderLists;