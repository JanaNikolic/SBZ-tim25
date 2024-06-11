import React, { useState } from "react";
import { useForm, useFieldArray, Controller } from "react-hook-form";
import axios from "axios";
import {
  Container,
  Box,
  Typography,
  TextField,
  Button,
  Grid,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";

const FirefighterObservationForm = ({ fireId, onClose }) => {
  const { control, handleSubmit } = useForm({
    defaultValues: {
      observations: Array.from({ length: 3 }, () => ({
        flameIntensity: "",
        fireArea: "",
        smokeVolume: "",
        fireId,
        numOfFirePoints: "",
      })),
    },
  });

  const { fields, append, remove } = useFieldArray({
    control,
    name: "observations",
  });

  const onSubmit = async (data) => {
    try {
      const accessToken = localStorage.getItem("accessToken");
      const response = await axios.post(
        `http://localhost:8080/api/fire-incident/${fireId}/observation`,
        data,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      console.log(response.data);
      onClose(response.data);
    } catch (error) {
      console.error("Failed to submit observations", error);
    }
  };

  return (
    <Dialog open onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Firefighter Observations</DialogTitle>
      <DialogContent>
        <form onSubmit={handleSubmit(onSubmit)}>
          {fields.map((field, index) => (
            <Box key={field.id} mb={2}>
              <Typography variant="h6">Observation {index + 1}</Typography>
              <Grid container spacing={2}>
                <Grid item xs={6}>
                  <Controller
                    name={`observations.${index}.flameIntensity`}
                    control={control}
                    render={({ field }) => (
                      <TextField
                        {...field}
                        label="Flame Intensity"
                        type="number"
                        fullWidth
                        required
                      />
                    )}
                  />
                </Grid>
                <Grid item xs={6}>
                  <Controller
                    name={`observations.${index}.fireArea`}
                    control={control}
                    render={({ field }) => (
                      <TextField
                        {...field}
                        label="Fire Area"
                        type="number"
                        fullWidth
                        required
                      />
                    )}
                  />
                </Grid>
                <Grid item xs={6}>
                  <Controller
                    name={`observations.${index}.smokeVolume`}
                    control={control}
                    render={({ field }) => (
                      <TextField
                        {...field}
                        label="Smoke Volume"
                        type="number"
                        fullWidth
                        required
                      />
                    )}
                  />
                </Grid>
                <Grid item xs={6}>
                  <Controller
                    name={`observations.${index}.numOfFirePoints`}
                    control={control}
                    render={({ field }) => (
                      <TextField
                        {...field}
                        label="Number of Fire Points"
                        type="number"
                        fullWidth
                        required
                      />
                    )}
                  />
                </Grid>
              </Grid>
              <Button
                variant="contained"
                color="secondary"
                onClick={() => remove(index)}
                disabled={fields.length <= 1}
                sx={{ mt: 1 }}
              >
                Remove Observation
              </Button>
            </Box>
          ))}
          <Button
            variant="contained"
            color="primary"
            onClick={() =>
              append({
                flameIntensity: "",
                fireArea: "",
                smokeVolume: "",
                fireId,
                numOfFirePoints: "",
              })
            }
            disabled={fields.length >= 10}
            sx={{ mt: 2 }}
          >
            Add Observation
          </Button>
          <DialogActions>
            <Button onClick={onClose} color="secondary">
              Cancel
            </Button>
            <Button type="submit" color="primary">
              Submit
            </Button>
          </DialogActions>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default FirefighterObservationForm;
