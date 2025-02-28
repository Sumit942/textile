import API from "./api";

const API_URL = "/api/yarn";

export const addYarn = async (yarn) => {
    console.log('addYarn() Entry: ',yarn)
    try {
        const response = await API.post(API_URL, yarn);
        console.log('addYarn() Exit: ', response)
        return response;
    } catch (err) {
        console.log('addYarn() Error: ', err)
        return err;
    }
};

export const getYarnById = async ( id ) => {
    console.log('getYarn() Entry')
    try {
        const response = await API.get(`${API_URL}${id ? '/'+id : ''}`)
        console.log('getYarn() Exit ', response)
        return response;
    } catch (err) {
        console.log('getYarn() Error: ', err)
        return err;
    }
}

export const getYarnByType = async (name) => {
    console.log('getYarnByName() Entry: ', name);
    try {
        const response = await API.get(`${API_URL}/searchBy?type=${name}`);
        console.log('getYarnByName() Exit: ', response);
        return response;
    } catch (err) {
        console.log('getYarnByName() Error: ', err);
        throw err;
    }
};