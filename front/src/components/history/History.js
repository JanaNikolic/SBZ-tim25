import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {
  Container,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper
} from '@mui/material';

const transformText = (text) => {
  return text
    .toLowerCase()
    .replace(/_/g, ' ')
    .replace(/(?:^\w|[A-Z]|\b\w)/g, (word) => word.toUpperCase())
    .replace(/\s+/g, ' ');
};

const History = () => {
  const [fireIncidents, setFireIncidents] = useState([]);

  useEffect(() => {
    const fetchFireIncidents = async () => {
      try {
        const accessToken = localStorage.getItem('accessToken');
        const response = await axios.get('http://localhost:8080/api/fire-incident/all-company', {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });
        setFireIncidents(response.data);
      } catch (error) {
        console.error('Failed to fetch fire incidents', error);
      }
    };

    fetchFireIncidents();
  }, []);

  return (
    <Container sx={{mt:8, width: "100%"}}>
      <Typography variant="h4" gutterBottom>Fire Incidents History</Typography>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><strong>Incident ID</strong></TableCell>
              <TableCell><strong>Matter</strong></TableCell>
              <TableCell><strong>Structure</strong></TableCell>
              <TableCell><strong>Flames</strong></TableCell>
              <TableCell><strong>Volume</strong></TableCell>
              <TableCell><strong>Smoke</strong></TableCell>
              <TableCell><strong>Wind Speed</strong></TableCell>
              <TableCell><strong>Wind Direction</strong></TableCell>
              <TableCell><strong>Proximity to Residential Area</strong></TableCell>
              <TableCell><strong>Room Placement</strong></TableCell>
              <TableCell><strong>Voltage</strong></TableCell>
              <TableCell><strong>People in Vicinity</strong></TableCell>
              <TableCell><strong>Proximity of People to Fire</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {fireIncidents.map((incident) => (
              <TableRow key={incident.id}>
                <TableCell>{incident.id}</TableCell>
                <TableCell>{transformText(incident.matter)}</TableCell>
                <TableCell>{transformText(incident.structure)}</TableCell>
                <TableCell>{transformText(incident.flames)}</TableCell>
                <TableCell>{incident.volume}</TableCell>
                <TableCell>{transformText(incident.smoke)}</TableCell>
                <TableCell>{incident.windSpeed}</TableCell>
                <TableCell>{transformText(incident.windDirection)}</TableCell>
                <TableCell>{incident.proximityToResidentialArea}</TableCell>
                <TableCell>{transformText(incident.roomPlacement)}</TableCell>
                <TableCell>{incident.voltage !== null ? incident.voltage : 'N/A'}</TableCell>
                <TableCell>{incident.peopleInVicinity ? 'Yes' : 'No'}</TableCell>
                <TableCell>{incident.proximityOfPeopleToFire}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
};

export default History;
