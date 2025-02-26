import API from "./api";

const API_URL = '/fabricDesign';

export const saveFabricDesign = async (fabricDesign) => {
    console.log('saveFabricDesign() Entry: ', fabricDesign);
    try {
        const response = await API.post(`${API_URL}`, fabricDesign);
        console.log('saveFabricDesign() Exit: ', response);
        return response;
    } catch (error) {
        console.error('saveFabricDesign() Error saving fabric design:', error);
        throw error;
    }
}

export const fetchFabricDesignById = async (orderId) => {
    console.log('fetchOrderByID() Entry: ', orderId);
    try {
        const response = await API.get(`${API_URL}/${orderId}`);
        console.log('fetchOrderByID() Exit: ', response);
        return response;
    } catch (error) {
        console.error('fetchOrderByID() Error fetching order:', error);
        throw error;
    }
}

export const fetchAllFabricDesigns = async () => {
    console.log('fetchAllFabricDesigns() Entry');
    try {
        const response = await API.get(`${API_URL}`);
        console.log('fetchAllFabricDesigns() Exit: ', response);
        return response;
    } catch (error) {
        console.error('fetchAllFabricDesigns() Error fetching fabric design list:', error);
        throw error;
    }
}

export const getFabricDesignsByNameLike = async (name) => {
    console.log('getFabricDesignsByNameLike() Entry');
    try {
        const response = await API.get(`${API_URL}/searchBy?name=${name}`);
        console.log('getFabricDesignsByNameLike() Exit: ', response);
        return response;
    } catch (error) {
        console.error('getFabricDesignsByNameLike() Error fetching fabric design list:', error);
        throw error;
    }
}

export const deleteFabricDesign = async (fabricDesignId) => {
    console.log('deleteFabricDesign() Entry: ', fabricDesignId);
    try {
        const response = await API.delete(`${API_URL}?id=${fabricDesignId}`);
        console.log('deleteFabricDesign() Exit: ', response);
        return response;
    } catch (error) {
        console.error('deleteFabricDesign() Error deleting fabric design:', error);
        throw error;
    }
}