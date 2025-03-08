import API from "./api";

const API_URL = "/orderProduct";

export const fetchProductIdAndMachineByYarnFabricDesignId = async ( yarnFabricDesignId ) => {
    console.log('fetchProductIdAndMachineByYarnFabricDesignId() Entry: ', yarnFabricDesignId)
    try {
      const response = await API.get(`${API_URL}/searchBy?yarnFabricDesignId=${yarnFabricDesignId}`);
      console.log('fetchProductIdAndMachineByYarnFabricDesignId() Exit: ', response)
      return response;
    } catch (error) {
      console.log('fetchProductIdAndMachineByYarnFabricDesignId() Error: ', error)
      return error;
    }
  }