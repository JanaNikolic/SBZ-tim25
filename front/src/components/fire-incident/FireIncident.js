import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import FireIncidentForm from "./FireIncidentForm";
import { Container, Box, Typography, Alert, Grid, Button } from "@mui/material";
import FirefighterObservationForm from "./FirefighterObservationForm";
import ActionDisplay from "./Actions";

const FireIncident = () => {
  const [activeFireIncident, setActiveFireIncident] = useState(null);
  const [activeFire, setActiveFire] = useState(null);
  const [formVisible, setFormVisible] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [additionalFireData, setAdditionalFireData] = useState([
    "VENTILATION",
    "EVACUATION_LOW",
    "EVACUATION_HIGH",
    "EVACUATION_MEDIUM",
    "ADDITIONAL_UNITS",
  ]);
  const [observationFormVisible, setObservationFormVisible] = useState(false);
  const [actions, setActions] = useState([]);
  const [firefighters, setFirefighters] = useState([]);
  const postStatsIntervalRef = useRef(null);
  const postActionsIntervalRef = useRef(null);
  const [simulation, setSimulation] = useState(true);
  const previousStatsRef = useRef({});

  useEffect(() => {
    const fetchActiveFireIncident = async () => {
      try {
        const accessToken = localStorage.getItem("accessToken");
        const response = await axios.get(
          "http://localhost:8080/api/fire-incident/active",
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );
        setActiveFireIncident(response.data);
        if (
          response.data &&
          response.data.fireCompany &&
          response.data.fireCompany.firefighters
        ) {
          setFirefighters(response.data.fireCompany.firefighters);
          startPostingFirefighterStats(response.data.fireCompany.firefighters, response.data.id);
          startGettingActions(response.data.id);
        }
      } catch (error) {
        if (error.response && error.response.status === 400) {
          setFormVisible(true);
        } else {
          setErrorMessage(error.response.data);
        }
      }
    };

    const fetchActiveFire = async () => {
      try {
        const accessToken = localStorage.getItem("accessToken");
        const response = await axios.get(
          "http://localhost:8080/api/fire-incident/active-decision",
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );
        setActiveFire(response.data);
      } catch (error) {}
    };
    fetchActiveFireIncident();
    fetchActiveFire();

    return () => {
      if (postStatsIntervalRef.current)
        clearInterval(postStatsIntervalRef.current);
      if (postActionsIntervalRef.current)
        clearInterval(postActionsIntervalRef.current);
    };
  }, []);

  const startPostingFirefighterStats = (firefighters, fireId) => {
    postStatsIntervalRef.current = setInterval(() => {
      firefighters.forEach(async (firefighter) => {
        const previousStats = previousStatsRef.current[firefighter.id] || {
          oxygenLevel: 80,
          heartRate: 60,
        };
  
        const oxygenLevel = Math.max(
          0,
          previousStats.oxygenLevel - Math.random() * 2
        ); // Decrease oxygenLevel
        const heartRate =
          previousStats.heartRate + (Math.random() > 0.5 ? 2 : -2); // Increase or decrease heartRate by 2
  
        const body = {
          id: firefighter.id,
          oxygenLevel,
          heartRate,
          fireId,
        };
  
        previousStatsRef.current[firefighter.id] = {
          oxygenLevel,
          heartRate,
        };
        console.log(body);
        try {
          const accessToken = localStorage.getItem("accessToken");
          await axios.post(
            "http://localhost:8080/api/fire-incident/firefighter-stats",
            body,
            {
              headers: {
                Authorization: `Bearer ${accessToken}`,
              },
            }
          );
        } catch (error) {
          console.error("Failed to post firefighter stats", error);
        }
      });
    }, 5000);
  };

  const startGettingActions = (fireId) => {
    postActionsIntervalRef.current = setInterval(async () => {
      try {
        const accessToken = localStorage.getItem("accessToken");
        const response = await axios.get(
          `http://localhost:8080/api/fire-incident/${fireId}/actions`,
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );
        setActions(response.data);
        // console.log(response.data);
      } catch (error) {
        console.error("Failed to get actions", error);
      }
    }, 16000);
  };

  const handleFormSubmit = (data, decision) => {
    window.location.reload();
    setActiveFireIncident(data.fireIncident);
    setAdditionalFireData(data);
    setActiveFire(decision);
    setFormVisible(false);
  };

  const handleObservationsSubmit = (data) => {
    setActiveFire(data);
    setObservationFormVisible(false);
  };

  const finishFire = async () => {
    try {
      const accessToken = localStorage.getItem("accessToken");
      const response = await axios.put(
        `http://localhost:8080/api/fire-incident/${activeFireIncident.id}/finish`,
        {},
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      setActions(null);
      setActiveFireIncident(null);
      setFirefighters(null);
      setActiveFire(null);
      setFormVisible(true);
      if (postStatsIntervalRef.current) {
        clearInterval(postStatsIntervalRef.current);
        postStatsIntervalRef.current = null;
      }
      if (postActionsIntervalRef.current) {
        clearInterval(postActionsIntervalRef.current);
        postActionsIntervalRef.current = null;
      }
      console.log(response.data);
    } catch (error) {
      console.error("Failed to finish fire", error);
    }
  };

  const stopSimulation = () => {
    if (postStatsIntervalRef.current) {
      clearInterval(postStatsIntervalRef.current);
      postStatsIntervalRef.current = null;
    }
    if (postActionsIntervalRef.current) {
      clearInterval(postActionsIntervalRef.current);
      postActionsIntervalRef.current = null;
    }
    if (simulation === false) setSimulation(true);
    else setSimulation(false);
    console.log(simulation);
  };

  const transformText = (text) => {
    if (text === null || text === undefined) return text;
    return text
      .toLowerCase()
      .replace(/_/g, " ")
      .replace(/(?:^\w|[A-Z]|\b\w)/g, (word) => word.toUpperCase());
  };

  const getColor = (action) => {
    if (action.includes("HIGH")) return "red";
    if (action.includes("MEDIUM")) return "orange";
    if (action.includes("LOW")) return "yellow";
    return "secondary.light";
  };

  return (
    <Grid container>
      <Grid item xs={12}>
        {activeFireIncident !== null && activeFireIncident !== undefined && (
          <Grid sx={{ mt: 2 }}>
            <Button
              sx={{ ml: 8 }}
              variant="contained"
              color="primary"
              onClick={() => setObservationFormVisible(true)}
            >
              Add Firefighter Observation
            </Button>

            {observationFormVisible && (
              <FirefighterObservationForm
                fireId={activeFireIncident.id}
                onClose={handleObservationsSubmit}
              />
            )}
            <Button
              sx={{ ml: 8 }}
              variant="contained"
              color="primary"
              onClick={() => finishFire()}
            >
              Finish Fire
            </Button>
            {/* <Button
              sx={{ ml: 8 }}
              variant="contained"
              color="primary"
              onClick={() => stopSimulation()}
            >
              Stop simulation
            </Button> */}
          </Grid>
        )}
      </Grid>
      {formVisible ? (
        <FireIncidentForm onSubmitSuccess={handleFormSubmit} maxWidth="xs" />
      ) : (
        activeFireIncident && (
          <Grid item sx={{ mt: 4, ml: 8 }} xs={3}>
            <Box>
              <Typography
                variant="h4"
                component="h1"
                gutterBottom
                textAlign="center"
              >
                Active Fire Incident
              </Typography>
              <Typography variant="body1">
                <strong>Burning Matter:</strong>{" "}
                {transformText(activeFireIncident.matter)}
              </Typography>
              <Typography variant="body1">
                <strong>Structure Type:</strong>{" "}
                {transformText(activeFireIncident.structure)}
              </Typography>
              <Typography variant="body1">
                <strong>Flames Type:</strong>{" "}
                {transformText(activeFireIncident.flames)}
              </Typography>
              <Typography variant="body1">
                <strong>Volume:</strong> {activeFireIncident.volume}
              </Typography>
              <Typography variant="body1">
                <strong>Smoke Type:</strong>{" "}
                {transformText(activeFireIncident.smoke)}
              </Typography>
              <Typography variant="body1">
                <strong>Wind Speed:</strong> {activeFireIncident.windSpeed}
              </Typography>
              <Typography variant="body1">
                <strong>Wind Direction:</strong>{" "}
                {transformText(activeFireIncident.windDirection)}
              </Typography>
              <Typography variant="body1">
                <strong>Proximity to Residential Area:</strong>{" "}
                {activeFireIncident.proximityToResidentialArea}
              </Typography>
              <Typography variant="body1">
                <strong>Room Placement:</strong>{" "}
                {transformText(activeFireIncident.roomPlacement)}
              </Typography>
              {activeFireIncident.voltage && (
                <Typography variant="body1">
                  <strong>Voltage:</strong> {activeFireIncident.voltage}
                </Typography>
              )}
              <Typography variant="body1">
                <strong>Proximity of People to Fire:</strong>{" "}
                {activeFireIncident.proximityOfPeopleToFire}
              </Typography>
            </Box>
          </Grid>
        )
      )}

      {formVisible
        ? null
        : activeFire && (
            <Grid item sx={{ mt: 4 }} xs={4}>
              <Box>
                <Typography
                  variant="h4"
                  component="h4"
                  gutterBottom
                  textAlign="center"
                >
                  Active Fire Decision
                </Typography>
                <Typography variant="body1">
                  <strong>Fire Class:</strong> {activeFire.fireClass}
                </Typography>
                <Typography variant="body1">
                  <strong>Wind Speed:</strong>{" "}
                  {transformText(activeFire.windSpeed)}
                </Typography>
                {activeFire.spreadRisk !== null && (
                  <Typography variant="body1">
                    <strong>Spread Risk:</strong>{" "}
                    {transformText(activeFire.spreadRisk)}
                  </Typography>
                )}
                <Typography variant="body1">
                  <strong>Spread Direction:</strong>{" "}
                  {transformText(activeFire.spreadDirection)}
                </Typography>
                <Typography variant="body1">
                  <strong>Fire Size:</strong>{" "}
                  {transformText(activeFire.fireSize)}
                </Typography>
                <Typography variant="body1">
                  <strong>Numbew Of Fire Points:</strong>{" "}
                  {activeFire.numOfFirePoints}
                </Typography>
                <Typography variant="body1">
                  <strong>Extinguisher:</strong>
                </Typography>
                <Grid container sx={{ mt: 2 }}>
                  <Box
                    sx={{
                      width: 100,
                      height: 100,
                      borderRadius: 1,
                      bgcolor: "secondary.light",
                      "&:hover": {
                        bgcolor: "secondary.light",
                      },
                      textAlign: "center",
                      alignContent: "center",
                      marginLeft: 4,
                    }}
                  >
                    {activeFire.extinguisher == null && (
                      <Typography variant="body1">
                        <strong>Water</strong>
                      </Typography>
                    )}
                    {activeFire.extinguisher != null && (
                      <Typography variant="body1">
                        <strong>
                          {transformText(activeFire.extinguisher)}
                        </strong>
                      </Typography>
                    )}
                  </Box>
                  {activeFire.typeOfExtinguisher !== null && (
                    <Box
                      sx={{
                        width: 100,
                        height: 100,
                        borderRadius: 1,
                        bgcolor: "secondary.light",
                        "&:hover": {
                          bgcolor: "secondary.light",
                        },
                        textAlign: "center",
                        alignContent: "center",
                        marginLeft: 4,
                      }}
                    >
                      <Typography variant="body1">
                        <strong>
                          {transformText(activeFire.typeOfExtinguisher)}
                        </strong>
                      </Typography>
                    </Box>
                  )}
                </Grid>
              </Box>
            </Grid>
          )}
      {!formVisible && activeFire && (
        <Grid item xs={4}>
          {activeFire && activeFire.additionalSteps && (
            <Grid item sx={{ mt: 1, ml: 8 }} xs={12}>
              <Box sx={{ mt: 4 }}>
                <Typography
                  variant="h4"
                  component="h2"
                  gutterBottom
                  textAlign="center"
                >
                  Additional Steps
                </Typography>
                <Grid container sx={{ mt: 4 }}>
                  {activeFire.shutOffGas && (
                    <Box
                      sx={{
                        width: 100,
                        height: 100,
                        borderRadius: 1,
                        bgcolor: "primary.light",
                        "&:hover": {
                          bgcolor: "primary.light",
                        },
                        textAlign: "center",
                        alignContent: "center",
                      }}
                    >
                      <Typography variant="body1">
                        <strong>Shut Off Gas</strong>
                      </Typography>
                    </Box>
                  )}
                  {activeFire.shutOffElectricity && (
                    <Box
                      sx={{
                        width: 100,
                        height: 100,
                        borderRadius: 1,
                        bgcolor: "primary.light",
                        "&:hover": {
                          bgcolor: "primary.light",
                        },
                        textAlign: "center",
                        alignContent: "center",
                        marginLeft: 2,
                      }}
                    >
                      <Typography variant="body1">
                        <strong>Shut Off Electricity</strong>
                      </Typography>
                    </Box>
                  )}
                </Grid>
                <Grid container sx={{ mt: 1 }} spacing={3}>
                  {Object.entries(activeFire.additionalSteps).map(
                    ([key, value]) => {
                      if (value === null || value === undefined) return null;
                      return (
                        <Box
                          sx={{
                            width: 100,
                            height: 100,
                            minHeight: "fit-content",
                            borderRadius: 1,
                            bgcolor: getColor(value),
                            textAlign: "center",
                            alignContent: "center",
                            marginRight: 2,
                            marginBottom: 1,
                          }}
                        >
                          <Typography key={key} variant="body1">
                            <strong>{transformText(value)}</strong>
                          </Typography>
                        </Box>
                      );
                    }
                  )}
                </Grid>
              </Box>
            </Grid>
          )}
        </Grid>
      )}
      {activeFire !== null && activeFire !== undefined && (
        <Grid item xs={12}>
          <Box sx={{ mt: 4 }}>
            <Typography
              variant="h4"
              component="h2"
              gutterBottom
              textAlign="center"
            >
              Actions
            </Typography>
            {activeFire !== null &&
              activeFire !== undefined &&
              actions !== null &&
              actions !== undefined && (
                <ActionDisplay actions={actions} firefighters={firefighters} />
              )}
          </Box>
        </Grid>
      )}
    </Grid>
  );
};

export default FireIncident;
