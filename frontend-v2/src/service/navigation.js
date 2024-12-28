import API from "./api"

const API_URI = '/navigations'


export const getNavigation = async () => {
  
    console.log('getNavigation() Entry', API_URI)
    try {
        const response = await API.get(API_URI)
        return response.data;
    } catch (error) {
        console.error('getNavigation()', error)
        return []
    }
    
}