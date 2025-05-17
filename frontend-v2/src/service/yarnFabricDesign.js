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

export const fetchFabricDesignYarnMappingById = async (id) => {
    console.log('fetchFabricDesignYarnMappingById() Entry: ', id);
    try {
        const response = await API.get(`${API_URL}/yarn/${id}`);
        console.log('fetchFabricDesignYarnMappingById() Exit: ', response);
        return response;
    } catch (error) {
        console.error('fetchFabricDesignYarnMappingById() Error finding fabric design:', error);
        throw error;
    }
}

export const fetchYarnFabricDesignByYarnsAndDesigns = async (yarnsAndDesignName) => {
    console.log('fetchYarnFabricDesignByYarnsAndDesigns() Entry: ', yarnsAndDesignName);
    try {
        const response = await API.get(`${API_URL}/searchBy?yarnsAndDesignName=${yarnsAndDesignName}`);
        console.log('fetchYarnFabricDesignByYarnsAndDesigns() Exit: ', response);
        return response;
    } catch (error) {
        console.error('fetchYarnFabricDesignByYarnsAndDesigns() Error finding fabric design:', error);
        throw error;
    }
}

export const fetchAllYarnFabricDesign = async (yarnsAndDesignName) => {
    console.log('fetchAll() Entry: ', yarnsAndDesignName);
    try {
        const response = await API.get(`${API_URL}`);
        console.log('fetchAll() Exit: ', response);
        return response;
    } catch (error) {
        console.error('fetchAll() Error finding fabric design:', error);
        throw error;
    }
}

export const fetchYarnFabricDesignById = async (id) => {
    console.log('fetchYarnFabricDesignById() Entry: ', id);
    try {
        const response = await API.get(`${API_URL}/${id}`);
        console.log('fetchYarnFabricDesignById() Exit: ', response);
        return response;
    } catch (error) {
        console.error('fetchYarnFabricDesignById() Error finding fabric design:', error);
        throw error;
    }
}