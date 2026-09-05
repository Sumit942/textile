import API from './api';

const API_URL = '/yarnBuilty';

export const saveYarnBuilty = async (yarnBuilty) => {
    console.log('saveYarnBuilty() Entry: ', yarnBuilty);
    try {
        const response = await API.post(`${API_URL}`, yarnBuilty);
        console.log('saveYarnBuilty() Exit: ', response);
        return response;
    } catch (error) {
        console.error('saveYarnBuilty() Error: ', error);
        return error;
    }
};

export const fetchAllYarnBuilties = async () => {
    console.log('fetchAllYarnBuilties() Entry');
    try {
        const response = await API.get(`${API_URL}`);
        console.log('fetchAllYarnBuilties() Exit: ', response);
        return response;
    } catch (error) {
        console.error('fetchAllYarnBuilties() Error: ', error);
        return error;
    }
};
