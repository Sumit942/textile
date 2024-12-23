
import API from "./api";

const API_URL = '/states'

export const getStates = async () => {
    console.log('getStates() Entry')
    const response = await API.get(API_URL);
    console.log('getState() Exit[', response.data, ']')
    return response.data;
}

export const saveState = async (state) => {
    console.log('saveState() Entry', API.defaults.headers.common, state)
    const response = await API.post(API_URL, state);
    console.log('saveState() Exit[', response.data, ']')
    return response.data;
}

export const deleteState = async (id) => {
    console.log('deleteState() Entry', id)
    const response = await API.delete(`${API_URL}/${id}`)
    console.log('deleteState() Exit[', response.data, ']')
    return response.data;
}