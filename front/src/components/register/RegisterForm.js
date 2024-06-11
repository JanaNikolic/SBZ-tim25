import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import {
  registerFirefighter,
  registerCaptain,
} from "../../services/UserService";
import {
  TextField,
  Button,
  Container,
  Typography,
  Box,
  Radio,
  RadioGroup,
  FormControlLabel,
  FormControl,
  FormLabel,
  Snackbar,
} from "@mui/material";

const RegisterForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const [role, setRole] = useState("firefighter");
  const [logedInRole, setLogedInRole] = useState(false);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  useEffect(() => {
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      const decodedToken = decodeToken(accessToken);
      const role = decodedToken.role[0].authority;
      if (role === "ROLE_CHIEF") setLogedInRole(true);
      else if (role === "ROLE_CAPTAIN") setLogedInRole(false);
    } else {
      setLogedInRole(false);
    }
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

  const onSubmit = async (data) => {
    try {
      let response;
      if (role === "firefighter") {
        response = await registerFirefighter(data);
      } else {
        response = await registerCaptain(data);
      }
      console.log("Registration successful:", response);
      setSnackbarMessage("Registration successful");
      setSnackbarOpen(true);
    } catch (error) {
      console.error("Registration error:", error.message);
      setSnackbarMessage("Registration failed. Try again!");
      setSnackbarOpen(true);
    }
  };

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  return (
    <Container maxWidth="xs">
      <Box sx={{ mt: 8 }}>
        <Typography variant="h4" component="h1" gutterBottom textAlign="center">
          Register Employe
        </Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          {logedInRole && (<FormControl component="fieldset" sx={{ mb: 2 }}>
            <FormLabel component="legend">Role</FormLabel>
            <RadioGroup
              row
              aria-label="role"
              value={role}
              onChange={(e) => setRole(e.target.value)}
            >
              <FormControlLabel
                value="firefighter"
                control={<Radio />}
                label="Firefighter"
              />
              <FormControlLabel
                value="captain"
                control={<Radio />}
                label="Captain"
              />
            </RadioGroup>
          </FormControl>)}
          <Box sx={{ mb: 2 }}>
            <TextField
              fullWidth
              id="name"
              label="Name"
              {...register("name", {
                required: "Name is required",
                pattern: {
                  value: /^([a-zA-Zčćđžš ]*)$/,
                  message: "Invalid format",
                },
              })}
              error={!!errors.name}
              helperText={errors.name ? errors.name.message : ""}
            />
          </Box>
          <Box sx={{ mb: 2 }}>
            <TextField
              fullWidth
              id="lastname"
              label="Last name"
              {...register("lastname", {
                required: "Last name is required",
                pattern: {
                  value: /^([a-zA-Zčćđžš ]*)$/,
                  message: "Invalid format",
                },
              })}
              error={!!errors.lastname}
              helperText={errors.lastname ? errors.lastname.message : ""}
            />
          </Box>
          <Box sx={{ mb: 2 }}>
            <TextField
              fullWidth
              id="email"
              label="Email"
              type="email"
              {...register("email", {
                required: "Email is required",
                pattern: {
                  value: /^[\w-\\.]+@([\w-]+\.)+[\w-]{2,4}$/,
                  message: "Invalid email format",
                },
              })}
              error={!!errors.email}
              helperText={errors.email ? errors.email.message : ""}
            />
          </Box>
          <Box sx={{ mb: 2 }}>
            <TextField
              fullWidth
              id="password"
              label="Password"
              type="password"
              {...register("password", { required: "Password is required" })}
              error={!!errors.password}
              helperText={errors.password ? errors.password.message : ""}
            />
          </Box>
          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            sx={{ mt: 3, mb: 2 }}
          >
            Register
          </Button>
        </form>
        <Snackbar
          open={snackbarOpen}
          autoHideDuration={6000}
          onClose={handleSnackbarClose}
          message={snackbarMessage}
        />
      </Box>
    </Container>
  );
};

export default RegisterForm;
