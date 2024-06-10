import axios from "axios";

const API_URL = "http://localhost:8080/api/user";

export const login = async (credentials) => {
  try {
    const response = await axios.post(`${API_URL}/login`, credentials);
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || "Login failed");
  }
};

export const registerFirefighter = async (user) => {
  try {
    const response = await axios.post(`${API_URL}/register-firefighter`, user);
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || "Registration failed");
  }
};

export const registerCaptain = async (user) => {
  try {
    const response = await axios.post(`${API_URL}/register-captain`, user);
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || "Registration failed");
  }
};
