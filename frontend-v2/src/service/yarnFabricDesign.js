import API from "./api";

const API_URL = '/yarnFabricDesign';

export const saveYarnFabricDesign = async (fabricDesign) => {
    console.log('saveYarnFabricDesign() Entry: ', fabricDesign);
    try {
        const response = await API.post(`${API_URL}/submit`, fabricDesign);
        console.log('saveYarnFabricDesign() Exit: ', response);
        return response;
    } catch (error) {
        console.error('saveYarnFabricDesign() Error saving fabric design:', error);
        throw error;
    }
}

export const findYarnFabricDesignById = async (id) => {
    console.log('findYarnFabricDesignById() Entry: ', id);
    try {
        const response = await API.get(`${API_URL}/${id}`);
        console.log('findYarnFabricDesignById() Exit: ', response);
        return response;
    } catch (error) {
        console.error('findYarnFabricDesignById() Error finding fabric design:', error);
        throw error;
    }
}