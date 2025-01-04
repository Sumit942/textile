import React from "react";
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
