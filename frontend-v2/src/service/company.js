import API from "./api";

const API_URL = "/company";

export const saveCompany = async (company) => {
    console.log('saveCompany() Entry: ',company)
    try {
        const response = await API.post(`${API_URL}/save`, company);
        console.log('saveCompany() Exit: ', response)
        return response;
    } catch (err) {
        console.log('saveCompany() Error: ', err)
        return err;
    }
};

export const getCompanyList = async ( companyName ) => {
  console.log('getCompanyList() Entry: ', companyName)
  try {
    const response = await API.get(`${API_URL}/search/${companyName}`);
    console.log('getCompanyList() Exit: ', response)
    return response;
  } catch (error) {
    console.log('getCompanyList() Error: ', error)
    return error;
  }
}
