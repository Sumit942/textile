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