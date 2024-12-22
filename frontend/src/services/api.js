import axios from 'axios';

const API = axios.create({
    baseURL: '/textile',
    withCredentials: true
})

export const setupCSRF = async () => {
    console.log('setupCSRF() Entry')
    const response = await API.get('/csrf')
    console.log('setupCSRF() response: ', response);
    const csrfToken = response.data.token;
    console.log('CSRF Token fetched:', csrfToken);
    API.defaults.headers.common[response.data.headerName] = csrfToken;
}

export default API;