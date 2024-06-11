import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { AppBar, Toolbar, Typography, Button } from "@mui/material";

const MenuBar = ({ userRole , handleLogout}) => {
console.log("MenuBar rendered with userRole:", userRole);
  const navigate = useNavigate();

  const handleLogout2 = () => {
    handleLogout();
    userRole = null;
    navigate("/login");
  };
  const renderMenuItems = () => {
    switch (userRole) {
      case "ROLE_FIREFIGHTER":
        return (
          <Button color="inherit" component={Link} to="/history">
            History
          </Button>
        );
      case "ROLE_CAPTAIN":
        return (
          <>
            <Button color="inherit" component={Link} to="/fire-incident">
              Fire Incident
            </Button>
            <Button color="inherit" component={Link} to="/history">
              History
            </Button>
            <Button color="inherit" component={Link} to="/validate-report">
              Validate Report
            </Button>
            <Button color="inherit" component={Link} to="/register">
              Register
            </Button>
          </>
        );
      case "ROLE_CHIEF":
        return (
          <>
            <Button color="inherit" component={Link} to="/history">
              History
            </Button>
            <Button color="inherit" component={Link} to="/validate-report">
              Validate Report
            </Button>
            <Button color="inherit" component={Link} to="/fire-company">
              Register Company
            </Button>
            <Button color="inherit" component={Link} to="/register">
              Register
            </Button>
          </>
        );
      default:
        return null;
    }
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Firefighting
        </Typography>
        {renderMenuItems()}
        {userRole && (
          <Button color="inherit" onClick={handleLogout2}>
            Logout
          </Button>
        )}
      </Toolbar>
    </AppBar>
  );
};

export default MenuBar;
