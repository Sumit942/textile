import API from './api';

const API_URL = '/yarnOrderItem';

export const fetchYarnOrderItemsByOrderAndYarnType = async (companyId, yarnId) => {
    try {
        const response = await API.get(`${API_URL}`, {
            params: {
                companyId,
                yarnId,
                isUsed: true
            }
        });
        return response;
    } catch (error) {
        console.error('Error fetching yarn order items:', error);
        throw error;
    }
};