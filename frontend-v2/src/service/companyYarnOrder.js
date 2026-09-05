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

export const fetchByYarnInvoiceNo = async (yarnInvoiceNo) => {
    console.log('fetchByYarnInvoiceNo() Entry: ', yarnInvoiceNo);
    try {
        const response = await API.get(`${API_URL}/search/yarnInvoiceNo?yarnInvoiceNo=${yarnInvoiceNo}`);
        console.log('fetchByYarnInvoiceNo() Exit: ', response);
        return response;
    } catch (error) {
        console.error('fetchByYarnInvoiceNo() Error: ', error);
        return error;
    }
}