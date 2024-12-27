import API from "./api";


const API_URL = '/states'

export const getStates = async () => {


    console.log('getStates() Entry')
    const response = await API.get(API_URL)
    console.log('getStates() Exit [', response, ']')
    

    return response.data;
}
