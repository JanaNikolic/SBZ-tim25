import React from 'react';
import { Navigate } from 'react-router-dom';

const RoleBasedGuard = ({ component: Component, allowedRoles }) => {
  const accessToken = localStorage.getItem('accessToken');

  const getUserRoleFromToken = () => {
    try {
      const base64Url = accessToken.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      const decodedToken = JSON.parse(jsonPayload);
      return decodedToken.role[0].authority;
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  };

  const userRole = accessToken ? getUserRoleFromToken() : null;

  if (!accessToken) {
    return <Navigate to="/login" replace />;
  }

  if (!allowedRoles.includes(userRole)) {
    return <Navigate to="/history" replace />;
  }

  return <Component />;
};

export default RoleBasedGuard;
