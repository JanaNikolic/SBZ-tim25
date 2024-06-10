// src/components/CreateFireCompanyForm.js
import React, { useState, useEffect } from "react";
import {
  TextField,
  Button,
  Container,
  Typography,
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  FormControlLabel,
  Checkbox,
  Snackbar,
  Chip,
  OutlinedInput,
} from "@mui/material";
import { useForm } from "react-hook-form";
import axios from "axios";

const ITEM_HEIGHT = 100;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
  PaperProps: {
    style: {
      maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
      width: 500,
    },
  },
};

const CreateFireCompanyForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const [captains, setCaptains] = useState([]);
  const [firefighters, setFirefighters] = useState([]);
  const [selectedCaptain, setSelectedCaptain] = useState("");
  const [selectedFirefighters, setSelectedFirefighters] = useState([]);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  useEffect(() => {
    const fetchCaptains = async () => {
      try {
        const accessToken = localStorage.getItem("accessToken");
        const response = await axios.get(
          "http://localhost:8080/api/user/captain-without-fire-company",
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );
        setCaptains(response.data);
      } catch (error) {
        console.error("Error fetching captains:", error);
      }
    };

    const fetchFirefighters = async () => {
      try {
        const accessToken = localStorage.getItem("accessToken");
        const response = await axios.get(
          "http://localhost:8080/api/user/firefighter-without-fire-company",
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );
        setFirefighters(response.data);
      } catch (error) {
        console.error("Error fetching firefighters:", error);
      }
    };

    fetchCaptains();
    fetchFirefighters();
  }, []);

  const onSubmit = async (data) => {
    try {
      const accessToken = localStorage.getItem("accessToken");
      const response = await axios.post(
        "http://localhost:8080/api/user/register-company",
        {
          captain: selectedCaptain,
          firefighters: selectedFirefighters,
        },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      console.log("Fire company created successfully:", response.data);
      setSnackbarMessage("Fire company created successfully");
      setSnackbarOpen(true);
    } catch (error) {
      console.error("Error creating fire company:", error);
      setSnackbarMessage("Failed to create fire company. Try again!");
      setSnackbarOpen(true);
    }
  };

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  return (
    <Container maxWidth="xs" sx={{ mt: 20 }}>
      <Box sx={{ mt: 8 }}>
        <Typography variant="h4" component="h1" gutterBottom textAlign="center">
          Create Fire Company
        </Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Box sx={{ mb: 2 }}>
            <FormControl fullWidth>
              <InputLabel id="captain-label">Captain</InputLabel>
              <Select
                labelId="captain-label"
                id="captain"
                value={selectedCaptain}
                onChange={(e) => setSelectedCaptain(e.target.value)}
              >
                {captains.map((captain) => (
                  <MenuItem key={captain.email} value={captain.email}>
                    {`${captain.name} ${captain.surname}`}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Box>
          <Box sx={{ mb: 2 }}>
            <FormControl fullWidth>
              <InputLabel id="firefighters-label">
                Firefighters (select 3 to 5)
              </InputLabel>
              <Select
                labelId="firefighters-label"
                id="firefighters"
                multiple
                value={selectedFirefighters}
                onChange={(e) => setSelectedFirefighters(e.target.value)}
                input={<OutlinedInput id="select-multiple-chip" label="Chip" />}
                renderValue={(selected) => (
                  <Box sx={{ display: "flex", flexWrap: "wrap", gap: 0.5 }}>
                    {selected.map((email) => (
                      <Chip
                        key={email}
                        label={`${
                          firefighters.find(
                            (firefighter) => firefighter.email === email
                          ).name
                        } ${
                          firefighters.find(
                            (firefighter) => firefighter.email === email
                          ).surname
                        }`}
                      />
                    ))}
                  </Box>
                )}
                MenuProps={MenuProps}
              >
                {firefighters.map((firefighter) => (
                  <MenuItem key={firefighter.email} value={firefighter.email}>
                    <FormControlLabel
                      control={
                        <Checkbox
                          checked={
                            selectedFirefighters.indexOf(firefighter.email) > -1
                          }
                        />
                      }
                      label={`${firefighter.name} ${firefighter.surname}`}
                    />
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Box>
          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            sx={{ mt: 3, mb: 2 }}
          >
            Create Fire Company
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

export default CreateFireCompanyForm;
