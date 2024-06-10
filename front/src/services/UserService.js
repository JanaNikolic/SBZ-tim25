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
    const accessToken = localStorage.getItem("accessToken");
    const response = await axios.post(`${API_URL}/register-firefighter`, user,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || "Registration failed");
  }
};

export const registerCaptain = async (user) => {
  try {
    const accessToken = localStorage.getItem("accessToken");
    const response = await axios.post(`${API_URL}/register-captain`, user,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || "Registration failed");
  }
};

export const logout = () => {
    localStorage.removeItem('accessToken');
    // Optionally, you can redirect the user to the login page after logout
    // history.push('/login');
};