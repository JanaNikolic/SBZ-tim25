import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import LoginForm from "./components/login/LoginForm";
import RegisterForm from "./components/register/RegisterForm";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import CreateFireCompanyForm from "./components/fire-company/CreateFireCompanyForm";
import MenuBar from "./components/menu-bar/MenuBar";
import ValidateReport from "./components/validate-report/ValidateReport";
import History from "./components/history/History";
import FireIncident from "./components/fire-incident/FireIncident";
import RoleBasedGuard from "./RoleBaseGuard";

const theme = createTheme({
  palette: {
    primary: {
      light: "#f09b9c",
      main: "#b91f1f",
      dark: "#531212",
      contrastText: "#fff",
    },
    secondary: {
      light: "#D2F1F1",
      main: "#b96c1f",
      dark: "#ba000d",
      contrastText: "#000",
    },
  },
});

const App = () => {
  const [userRole, setUserRole] = useState(null);

  useEffect(() => {
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      const decodedToken = decodeToken(accessToken);
      const role = decodedToken.role[0].authority;
      setUserRole(role);
    } else {
      setUserRole(null);
    }
    console.log("User role in App component:", userRole);
  }, []);

  const decodeToken = (token) => {
    try {
      const base64Url = token.split(".")[1];
      const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split("")
          .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
          .join("")
      );
      return JSON.parse(jsonPayload);
    } catch (error) {
      console.error("Error decoding token:", error);
      return {};
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    setUserRole(null);
  };

  return (
    <ThemeProvider theme={theme}>
      <Router>
        {userRole && <MenuBar userRole={userRole} handleLogout={handleLogout} />}
        <Routes>
          <Route path="/login" element={<LoginForm />} />
          <Route
            path="/register"
            element={
              <RoleBasedGuard
                component={RegisterForm}
                allowedRoles={["ROLE_CAPTAIN", "ROLE_CHIEF"]}
              />
            }
          />
          <Route
            path="/fire-company"
            element={
              <RoleBasedGuard
                component={CreateFireCompanyForm}
                allowedRoles={["ROLE_CHIEF"]}
              />
            }
          />
          <Route
            path="/fire-incident"
            element={
              <RoleBasedGuard
                component={FireIncident}
                allowedRoles={["ROLE_CAPTAIN"]}
              />
            }
          />
          <Route
            path="/history"
            element={
              <RoleBasedGuard
                component={History}
                allowedRoles={["ROLE_CAPTAIN", "ROLE_CHIEF"]}
              />
            }
          />
          <Route
            path="/validate-report"
            element={
              <RoleBasedGuard
                component={ValidateReport}
                allowedRoles={["ROLE_CAPTAIN", "ROLE_CHIEF"]}
              />
            }
          />
        </Routes>
      </Router>
    </ThemeProvider>
  );
};

export default App;
