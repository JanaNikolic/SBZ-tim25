import React, { useState } from "react";
import { useForm, Controller } from "react-hook-form";
import axios from "axios";
import {
  Container,
  Box,
  Typography,
  TextField,
  Button,
  Snackbar,
  MenuItem,
  Select,
  InputLabel,
  FormControl,
  Grid,
  InputAdornment,
} from "@mui/material";
import Autocomplete from "@mui/material/Autocomplete";

const burningMatterOptions = [
  "WOOD",
  "PAPER",
  "PLASTICS",
  "RUBBER",
  "OTHER_SOLID_MATERIAL",
  "OTHER_LIQUID_MATERIAL",
  "OTHER_METAL",
  "OTHER_GAS",
  "ELECTRICAL_INSTALLATION",
  "ELECTRONIC_EQUIPMENT",
  "APPLIANCE",
  "BUTANE",
  "METHANE",
  "PROPANE",
  "SODIUM",
  "MAGNESIUM",
  "TITANIUM",
  "METHANOL",
  "ETHANOL",
  "ACETONE",
  "HEXANE",
  "PENTANE",
  "BUTANOL",
  "TOLUENE",
];

const structureTypeOptions = [
  "AIRPORTS",
  "REFINERIES",
  "INDUSTRIAL_PLANTS",
  "WAREHOUSES_OF_PETROLEUM_PRODUCTS",
  "MOTOR_VEHICLES",
  "WAREHOUSE_OF_FLAMMABLE_LIQUIDS_AND_GASES",
  "PAINT_AND_VARNISH_FACTORY",
  "PAINT_SHOP",
  "LIBRARY",
  "ARCHIVE",
  "MINE",
  "POWER_PLANT",
  "SHIP",
  "ELECTRICAL_INSTALLATION",
  "ELECTRONIC_DEVICE",
  "LABORATORY",
  "PHARMACY",
  "RESIDENTIAL_BUILDING",
  "OFFICE_BUILDING",
];

const flamesTypeOptions = ["HIGH", "MEDIUM", "LOW"];
const smokeTypeOptions = ["THICK", "MODERATE", "THIN"];
const windDirectionOptions = ["NORTH", "SOUTH", "WEST", "EAST", "VARIABLE"];
const roomPlacementOptions = [
  "BASEMENT",
  "FIRST_FLOOR",
  "FLOOR2_4",
  "FLOOR4_7",
  "FLOOR8_15",
  "FLOOR16_MORE",
  "ATTIC",
  "ROOF",
];

const FireIncidentForm = ({ onSubmitSuccess }) => {
  const { control, handleSubmit, watch } = useForm();
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const matter = watch("matter");

  const onSubmit = async (data) => {
    try {
      console.log(data);
      const accessToken = localStorage.getItem("accessToken");
      const response = await axios.post(
        "http://localhost:8080/api/fire-incident",
        data,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      setSnackbarMessage("Fire incident reported successfully");
      setSnackbarOpen(true);
      onSubmitSuccess(response.data, data);
    } catch (error) {
      setSnackbarMessage("Failed to report fire incident. Try again!");
      setSnackbarOpen(true);
    }
  };

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  return (
    <Container sx={{mt:8}}>
      <Box>
        <Typography variant="h4" component="h1" gutterBottom textAlign="center">
          Report Fire Incident
        </Typography>
        <form
          onSubmit={handleSubmit(onSubmit)}
          style={{ textAlign: "center", marginTop: "8vh" }}
        >
          <Grid container spacing={12} rowSpacing={4}>
            <Grid item xs={6}>
              <Controller
                style={{ marginBottom: "16px" }}
                name="matter"
                control={control}
                defaultValue=""
                render={({ field }) => (
                  <Autocomplete
                    style={{ marginBottom: "16px" }}
                    {...field}
                    options={burningMatterOptions}
                    onChange={(_, value) => field.onChange(value)}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        label="Burning Matter"
                        variant="outlined"
                        fullWidth
                      />
                    )}
                  />
                )}
              />
              <Controller
                name="structure"
                control={control}
                defaultValue=""
                render={({ field }) => (
                  <Autocomplete
                    style={{ marginBottom: "16px" }}
                    {...field}
                    options={structureTypeOptions}
                    onChange={(_, value) => field.onChange(value)}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        label="Structure Type"
                        variant="outlined"
                        fullWidth
                      />
                    )}
                  />
                )}
              />
              <FormControl fullWidth sx={{ mb: 2 }}>
                <InputLabel>Flames Type</InputLabel>
                <Controller
                  name="flames"
                  control={control}
                  defaultValue=""
                  render={({ field }) => (
                    <Select {...field} style={{ textAlign: "initial" }}>
                      {flamesTypeOptions.map((option) => (
                        <MenuItem key={option} value={option}>
                          {option}
                        </MenuItem>
                      ))}
                    </Select>
                  )}
                />
              </FormControl>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <InputLabel>Smoke Type</InputLabel>
                <Controller
                  name="smoke"
                  control={control}
                  defaultValue=""
                  render={({ field }) => (
                    <Select {...field} style={{ textAlign: "initial" }}>
                      {smokeTypeOptions.map((option) => (
                        <MenuItem key={option} value={option}>
                          {option}
                        </MenuItem>
                      ))}
                    </Select>
                  )}
                />
              </FormControl>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <InputLabel>Wind Direction</InputLabel>
                <Controller
                  name="windDirection"
                  control={control}
                  defaultValue=""
                  render={({ field }) => (
                    <Select {...field} style={{ textAlign: "initial" }}>
                      {windDirectionOptions.map((option) => (
                        <MenuItem key={option} value={option}>
                          {option}
                        </MenuItem>
                      ))}
                    </Select>
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <InputLabel>Room Placement</InputLabel>
                <Controller
                  name="roomPlacement"
                  control={control}
                  defaultValue=""
                  render={({ field }) => (
                    <Select {...field} style={{ textAlign: "initial" }}>
                      {roomPlacementOptions.map((option) => (
                        <MenuItem key={option} value={option}>
                          {option}
                        </MenuItem>
                      ))}
                    </Select>
                  )}
                />
              </FormControl>
              <Controller
                name="volume"
                control={control}
                defaultValue=""
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Volume"
                    fullWidth
                    type="number"
                    sx={{ mb: 2 }}
                    inputProps={{ min: 0, max: 1000 }}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          m<sup>2</sup>
                        </InputAdornment>
                      ),
                    }}
                  />
                )}
              />
              <Controller
                name="windSpeed"
                control={control}
                defaultValue=""
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Wind Speed"
                    fullWidth
                    type="number"
                    sx={{ mb: 2 }}
                    inputProps={{ min: 0, max: 400 }}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">km/h</InputAdornment>
                      ),
                    }}
                  />
                )}
              />
              <Controller
                name="proximityToResidentialArea"
                control={control}
                defaultValue=""
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Proximity to Residential Area"
                    fullWidth
                    type="number"
                    sx={{ mb: 2 }}
                    inputProps={{ min: 0, max: 500 }}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">m</InputAdornment>
                      ),
                    }}
                  />
                )}
              />
              <Controller
                name="proximityOfPeopleToFire"
                control={control}
                defaultValue=""
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Proximity of People to Fire"
                    fullWidth
                    type="number"
                    sx={{ mb: 2 }}
                    inputProps={{ min: 0, max: 500 }}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">m</InputAdornment>
                      ),
                    }}
                  />
                )}
              />

              {matter === "ELECTRICAL_INSTALLATION" ||
              matter === "ELECTRONIC_EQUIPMENT" ||
              matter === "APPLIANCE" ? (
                <Controller
                  name="voltage"
                  control={control}
                  defaultValue=""
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="Voltage"
                      fullWidth
                      type="number"
                      sx={{ mb: 2 }}
                      inputProps={{ min: 0, max: 100000 }}
                    />
                  )}
                />
              ) : null}
            </Grid>
          </Grid>
          <Grid container>
            <Grid item xs={6}></Grid>
          </Grid>
          <Button
            style={{ width: "40%" }}
            type="submit"
            variant="contained"
            color="primary"
            sx={{ mt: 3, mb: 2 }}
          >
            Report
          </Button>
        </form>
      </Box>
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={6000}
        onClose={handleSnackbarClose}
        message={snackbarMessage}
      />
    </Container>
  );
};

export default FireIncidentForm;
