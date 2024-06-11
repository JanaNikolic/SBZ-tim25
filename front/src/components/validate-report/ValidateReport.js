import React, { useState } from "react";
import {
  Box,
  Button,
  Container,
  MenuItem,
  Select,
  Typography,
  Grid,
  Alert,
} from "@mui/material";
import { DragDropContext, Droppable, Draggable } from "react-beautiful-dnd";
import axios from "axios";

const stepsOptions = [
  "Shutting Off Gas/Electricity",
  "Evacuation",
  "Ventilation",
  "Fire Localization",
  "Extinguishing Fire",
];

const ValidateReport = () => {
  const [selectedSteps, setSelectedSteps] = useState([]);
  const [stepOrder, setStepOrder] = useState([]);
  const [fireId, setFireId] = useState(1);
  const [errorMessage, setErrorMessage] = useState("");
  const [validMessage, setValidMessage] = useState("");

  const handleSelectChange = (event) => {
    const { value } = event.target;
    setSelectedSteps(value);
    setStepOrder(value);
  };

  const onDragEnd = (result) => {
    if (!result.destination) return;
    const items = Array.from(stepOrder);
    const [reorderedItem] = items.splice(result.source.index, 1);
    items.splice(result.destination.index, 0, reorderedItem);
    setStepOrder(items);
  };

  const handleSubmit = async () => {
    const accessToken = localStorage.getItem("accessToken");
    const body = {
      fireId: fireId,
      steps: stepOrder.map((step, index) => ({
        name: step,
        order: index + 1,
        fireId: fireId,
      })),
    };

    try {
      const response = await axios.post(
        "http://localhost:8080/api/fire-incident/validate-report",
        body,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      console.log(response.data);
      if (response.data.message.includes("Invalid")) {
        setErrorMessage("Invalid Incident Report!");
        setValidMessage("");
      } else if (response.data.message.includes("Valid")) {
        setValidMessage("Valid Incident Report!");
        setErrorMessage("");
      }
    } catch (error) {
      console.error("Failed to validate report", error);
    }
  };

  return (
    <Container sx={{ mt: 8 }}>
      <Typography variant="h4" gutterBottom>
        Arrange Fire Incident Steps
      </Typography>
      <Select
        multiple
        value={selectedSteps}
        onChange={handleSelectChange}
        renderValue={(selected) => selected.join(", ")}
        sx={{ marginBottom: 2, minWidth: 300 }}
      >
        {stepsOptions.map((step) => (
          <MenuItem key={step} value={step}>
            {step}
          </MenuItem>
        ))}
      </Select>
      <DragDropContext onDragEnd={onDragEnd}>
        <Droppable droppableId="steps">
          {(provided) => (
            <Box
              {...provided.droppableProps}
              ref={provided.innerRef}
              sx={{ display: "flex", flexDirection: "column", gap: 2, mb: 2 }}
            >
              {stepOrder.map((step, index) => (
                <Draggable key={step} draggableId={step} index={index}>
                  {(provided) => (
                    <Box
                      ref={provided.innerRef}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                      sx={{
                        padding: 2,
                        bgcolor: "primary.light",
                        borderRadius: 1,
                        textAlign: "center",
                      }}
                    >
                      <Typography variant="body1">
                        <strong>{step}</strong>
                      </Typography>
                    </Box>
                  )}
                </Draggable>
              ))}
              {provided.placeholder}
            </Box>
          )}
        </Droppable>
      </DragDropContext>
      <Grid container justifyContent="flex-end">
        <Button
          variant="contained"
          color="primary"
          onClick={handleSubmit}
          disabled={stepOrder.length < 3 || stepOrder.length > 5}
        >
          Submit Report
        </Button>
      </Grid>
      {errorMessage && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {errorMessage}
        </Alert>
      )}
      {validMessage && (
        <Alert severity="success" sx={{ mb: 2 }}>
          {validMessage}
        </Alert>
      )}
    </Container>
  );
};

export default ValidateReport;
