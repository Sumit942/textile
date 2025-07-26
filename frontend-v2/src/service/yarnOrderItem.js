import API from './api';

const API_URL = '/yarnOrderItem';

export const fetchYarnOrderItemsByOrderAndYarnType = async (companyId, yarnId) => {
    console.log("fetchYarnOrderItemsByOrderAndYarnType() Entry:", companyId, yarnId);
    try {
        const response = await API.get(`${API_URL}`, {
            params: {
                companyId,
                yarnId,
                isUsed: true
            }
        });
        console.log("fetchYarnOrderItemsByOrderAndYarnType() Exit:", response);
        return response;
    } catch (error) {
        console.error('Error fetching yarn order items:', error);
        throw error;
    }
};