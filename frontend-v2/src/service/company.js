import API from "./api";

const API_URL = "/company/save";

export const saveCompany = async (company) => {
    console.log('saveCompany() Entry: ',company)
    try {
        const response = await API.post(API_URL, company);
        console.log('saveCompany() Exit: ', response)
        return response;
    } catch (err) {
        console.log('saveCompany() Error: ', err)
        return err;
    }
};