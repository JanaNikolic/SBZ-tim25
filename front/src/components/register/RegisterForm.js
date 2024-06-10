import React, { useState } from "react";
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

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

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
    <Container maxWidth="xs" sx={{ mt: 10 }}>
      <Box sx={{ mt: 8 }}>
        <Typography variant="h4" component="h1" gutterBottom textAlign="center">
          Register Employe
        </Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <FormControl component="fieldset" sx={{ mb: 2 }}>
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
          </FormControl>
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
