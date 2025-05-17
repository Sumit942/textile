import API from './api';

const API_URL = '/companyYarnOrder';

export const saveCompanyYarnOrder = async (companyYarnOrder) => {
    console.log('saveCompanyYarnOrder() Entry: ', companyYarnOrder);
    try {
        const response = await API.post(`${API_URL}/submit`, companyYarnOrder);
        console.log('saveCompanyYarnOrder() Exit: ', response);
        return response;
    } catch (error) {
        console.error('saveCompanyYarnOrder() Error: ', error);
        return error;
    }
};

export const fetchAllOrders = async () => {
    console.log('fetchAllOrders() Entry');
    try {
        const response = await API.get(`${API_URL}`);
        console.log('fetchAllOrders() Exit: ', response);
        return response;
    } catch (error) {
        console.error('fetchAllOrders() Error: ', error);
        return error;
    }
};