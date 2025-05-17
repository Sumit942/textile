import API from './api';

const API_URL = '/orderProductMapping';

export const saveOrderProductMapping = async (orderProductMapping) => {
    console.log('saveOrderProductMapping() Entry: ', orderProductMapping);
    try {
        const response = await API.post(`${API_URL}/submit`, orderProductMapping);
        console.log('saveOrderProductMapping() Exit: ', response);
        return response;
    } catch (error) {
        console.error('saveOrderProductMapping() Error: ', error);
        return error;
    }
};