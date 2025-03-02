import React, { useEffect, useState } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, CircularProgress, Button, LinearProgress } from '@mui/material';
import { useLocation, useNavigate } from 'react-router-dom'
import { fetchOrderList } from '../service/orderApi';

const OrderLists = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const location = useLocation();

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
        return (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <CircularProgress />
            </div>
        );
    }
    const statusColors = {
        'CREATED': { color: 'blue', progress: 10 },
        'RECEIVED': { color: 'yellow', progress: 20 },
        'IN_PROCESS': { color: 'orange', progress: 40 },
        'HALF_PROCESSED': { color: 'lightgreen', progress: 60 },
        'PROCESSED': { color: 'green', progress: 80 },
        'HALF_DELIVERED': { color: 'gray', progress: 90 },
        'DELIVERED': { color: 'gray', progress: 100 },
        'PENDING': { color: 'gray', progress: 0 },
        'HOLD': { color: 'gray', progress: 0 },
        'CANCELLED': { color: 'gray', progress: 0 }
    };
    const getOrderStatus = (status) => {
        return statusColors[status] || 'black';
    };

    return (
        <>
        {location.state?.message && <div className='top-full m-1 rounded-md text-center'><span className='px-10 py-2 bg-green-100' >{location.state.message}</span></div>}
        <TableContainer component={Paper}>
            <Table onClick={handleTableClick}>
                <TableHead>
                    <TableRow>
                        <TableCell>Sr. No</TableCell>
                        <TableCell>Order Name</TableCell>
                        <TableCell>Order Status</TableCell>
                        <TableCell>Details</TableCell>
                        <TableCell>Action</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {orders.map((order, index) => (
                        <TableRow key={order.id}>
                            <TableCell>{index + 1}</TableCell>
                            <TableCell>{order.orderNo}</TableCell>
                            <TableCell>
                                <span style={{ color: getOrderStatus(order.orderStatusType).color }}>
                                    {order.orderStatusType}
                                </span>
                                <LinearProgress variant="determinate" value={getOrderStatus(order.orderStatusType).progress} />
                            </TableCell>
                            <TableCell>{order.itemsWithQuantities ? order.itemsWithQuantities.split(',')
                            .map((itemWithQuantity, index) => {
                                const [item, quantity] = itemWithQuantity.split(':');
                                return (
                                    <div key={index} style={{ display: 'flex', marginBottom: '3px' }}>
                                        <span style={{ fontWeight: 'bold' }}>{item}</span>
                                        <span style={{ marginLeft: '10px' }}>{quantity}</span>
                                    </div>
                                );
                            }) : ''}</TableCell>
                            <TableCell>
                                <Button 
                                    onClick={() => navigate('/orders/save', {
                                        state: { id : order.orderId }
                                        })
                                    }>Edit
                                </Button>
                                <br/>
                                <Button className="delete-button" data-id={order.orderId} color="error">
                                    Delete
                                </Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
        </>
    );
};

export default OrderLists;