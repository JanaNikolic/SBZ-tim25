import React from "react";
import { Box, Typography, keyframes } from "@mui/material";

const ActionDisplay = ({ actions, firefighters }) => {
  const getColor = (action) => {
    if (action.includes("RESCUE")) return "red";
    if (action.includes("EVACUATE")) return "orange";
    if (action.includes("WARN")) return "yellow";
    return "primary.light";
  };

  const getColorFromPriority = (action) => {
    if (action === 3) return "red";
    if (action === 2) return "orange";
    if (action === 1) return "yellow";
    return "primary.light";
  };

  const transformText = (text) => {
    return text
      .toLowerCase()
      .replace(/_/g, " ")
      .replace(/(?:^\w|[A-Z]|\b\w)/g, (word) => word.toUpperCase())
      .replace(/\s+/g, " ");
  };

  const getPriority = (action) => {
    if (action.includes("RESCUE")) return 3;
    if (action.includes("EVACUATE")) return 2;
    if (action.includes("WARN")) return 1;
    return 0;
  };

  const pulseAnimation = (priority) => keyframes`
    0% { background-color: ${getColorFromPriority(priority)}; }
    50% { background-color: ${getColorFromPriority(priority)}; opacity: 0.5; }
    100% { background-color: ${getColorFromPriority(priority)}; }
  `;

  const filteredActions = actions.reduce((acc, curr) => {
    const existingAction = acc.find((action) => action.id === curr.id);
    if (
      !existingAction ||
      getPriority(curr.action) > getPriority(existingAction.action)
    ) {
      return acc.filter((action) => action.id !== curr.id).concat(curr);
    }
    return acc;
  }, []);

  return (
    <Box
      sx={{
        display: "flex",
        flexWrap: "wrap",
        gap: 2,
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      {firefighters.map((firefighter) => {
        const firefighterActions = filteredActions.filter(
          (action) =>
            action.name === firefighter.name &&
            action.surname === firefighter.surname
        );

        return (
          <Box
            key={firefighter.email}
            sx={{
              width: 225,
              minHeight: 150,
              padding: 2,
              borderRadius: 1,
              border: "solid #727272",
              textAlign: "center",
            }}
          >
            <Typography variant="h6">
              {firefighter.name} {firefighter.surname}
            </Typography>
            {firefighterActions.length > 0 ? (
              firefighterActions.map((action) => {
                const priority = getPriority(action.action);
                const animationDuration =
                  priority === 3 ? "0.5s" : priority === 2 ? "1s" : "1.5s";
                const pulse = pulseAnimation(priority);

                return (
                  <Box
                    key={action.id}
                    sx={{
                      width: 100,
                      height: 100,
                      borderRadius: 1,
                      bgcolor: getColor(action.action),
                      "&:hover": {
                        bgcolor: getColor(action.action),
                      },
                      textAlign: "center",
                      alignContent: "center",
                      margin: 1,
                      display: "flex",
                      justifyContent: "center",
                      alignItems: "center",
                      animation: `${pulse} ${animationDuration} infinite`,
                    }}
                  >
                    <Typography variant="body1">
                      <strong>{transformText(action.action)}</strong>
                    </Typography>
                  </Box>
                );
              })
            ) : (
              <Typography variant="body2">No actions</Typography>
            )}
          </Box>
        );
      })}
    </Box>
  );
};

export default ActionDisplay;
