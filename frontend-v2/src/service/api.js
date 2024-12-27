import axios from 'axios';

const API = axios.create({
    baseURL: '/textile',
    withCredentials: true
})

export const setupCSRF = async () => {
    console.log('setupCSRF() Entry', API.defaults)
    try {
        const response = await API.get('/csrf')
        console.log('setupCSRF() response: ', response);

        const csrfToken = response.data.token;
        console.log('CSRF Token fetched:', csrfToken);

        API.defaults.headers.common[response.data.headerName] = csrfToken;
    } catch (err) {
        console.error('setupCSRF() error: ', err)
    }
}

export default API;