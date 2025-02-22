import API from "./api";

const API_URL = '/order';

export const saveOrder = async (order) => {
    console.log('saveOrder() Entry: ', order);
    try {
        const response = await API.post(`${API_URL}/submit`, order);
        console.log('saveOrder() Exit: ', response);
        return response;
    } catch (error) {
        console.error('saveOrder() Error saving order:', error);
        return error;
    }
};

export const fetchOrderList = async () => {
    console.log('fetchOrderList() Entry');
    try {
        const response = await API.get(`${API_URL}/view`);
        console.log('fetchOrderList() Exit: ', response);
        return response;
    } catch (error) {
        console.error('fetchOrderList() Error fetching order list:', error);
        return error;
    }
};

export const fetchOrderByID = async (orderId) => {
    console.log('fetchOrderByID() Entry: ', orderId);
    try {
        const response = await API.get(`${API_URL}/${orderId}`);
        console.log('fetchOrderByID() Exit: ', response);
        return response;
    } catch (error) {
        console.error('fetchOrderByID() Error fetching order:', error);
        return error;
    }
}