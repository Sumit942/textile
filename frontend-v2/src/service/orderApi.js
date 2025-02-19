import API from "./api";

const API_URL = '/order/submit';

export const saveOrder = async (order) => {
    console.log('saveOrder() Entry: ', order);
    try {
        const response = await API.post(API_URL, order);
        console.log('saveOrder() Exit: ', response);
        return response;
    } catch (error) {
        console.error('saveOrder() Error saving order:', error);
        return error;
    }
};