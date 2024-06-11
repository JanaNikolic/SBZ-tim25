import React, { useState, useEffect } from "react";
import {
  Box,
  Button,
  Container,
  MenuItem,
  Select,
  Typography,
  Grid,
  Alert,
  FormControl,
  InputLabel,
} from "@mui/material";
import {
  DragDropContext,
  Droppable,
  Draggable,
  DroppableProps,
} from "react-beautiful-dnd";
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

  const StrictModeDroppable = ({ children, ...props }) => {
    const [enabled, setEnabled] = useState(false);
    useEffect(() => {
      const animation = requestAnimationFrame(() => setEnabled(true));
      return () => {
        cancelAnimationFrame(animation);
        setEnabled(false);
      };
    }, []);
    if (!enabled) {
      return null;
    }
    return <Droppable {...props}>{children}</Droppable>;
  };

  return (
    <Container sx={{ mt: 8 }}>
      <Typography variant="h4" gutterBottom>
        Arrange Fire Incident Steps
      </Typography>
      <FormControl fullWidth>
        <InputLabel id="steps-label">Steps (select 3 to 5)</InputLabel>
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
      </FormControl>
      <DragDropContext onDragEnd={onDragEnd}>
        <StrictModeDroppable droppableId="steps">
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
                        bgcolor: "secondary.light",
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
        </StrictModeDroppable>
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
        <Alert severity="error" sx={{ mb: 2, width: 200 }}>
          {errorMessage}
        </Alert>
      )}
      {validMessage && (
        <Alert severity="success" sx={{ mb: 2, width: 200 }}>
          {validMessage}
        </Alert>
      )}
    </Container>
  );
};

export default ValidateReport;
