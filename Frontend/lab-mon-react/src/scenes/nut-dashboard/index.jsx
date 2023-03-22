import { Box, Typography, useTheme } from "@mui/material";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { tokens } from "../../theme";
import CheckOutlinedIcon from "@mui/icons-material/CheckOutlined";
import BlockOutlinedIcon from "@mui/icons-material/BlockOutlined";
import { useEffect, useState } from "react";
import LineChart from "../../components/LineChart";
import Header from "../../components/Header";

const NutDashboard = () => {
  const theme = useTheme();
  const [nutData, setNutData] = useState([]);
  const [isViewLoaded, setViewLoaded] = useState(false);

  useEffect(() => {
    const fetchNutData = async () => {
      const response = await fetch("http://127.0.0.1:7568/nut_full");
      const data = await response.json();
      if (response.status === 200) {
        setNutData(data);
        setViewLoaded(true);
      } else {
        setViewLoaded(false);
      }
    };

    fetchNutData();

    const interval = setInterval(() => {
      fetchNutData();
    }, 60000);

    return () => clearInterval(interval);
  }, []);

  return (
    <Box m="5px" textAlign="center">
      <Header title="UPS History" />

      <Box display="flex" flexDirection="row">
        <Box height="18.4vh" width="48.25vw">
          <h3>Online Status</h3>
          {isViewLoaded ? (
            <LineChart
              xLedge={"Online Status"}
              yLedge={"Online"}
              tickSize={15}
              data={nutData.online}
            />
          ) : (
            <p>Loading</p>
          )}

          <h3>Battery Charge</h3>
          {isViewLoaded ? (
            <LineChart
              xLedge={"Charge Status"}
              yLedge={"%"}
              tickSize={15}
              data={nutData.charge}
              curve={"monotoneX"}
              min={0}
              max={100}
            />
          ) : (
            <p>Loading</p>
          )}

          <h3>Runtime Left</h3>
          {isViewLoaded ? (
            <LineChart
              xLedge={"Runtime Status"}
              yLedge={"Seconds"}
              tickSize={15}
              data={nutData.runtime}
              curve={"monotoneX"}
              min={0}
              max={"auto"}
            />
          ) : (
            <p>Loading</p>
          )}

          <h3>Temperature</h3>
          {isViewLoaded ? (
            <LineChart
              xLedge={"Temperature"}
              yLedge={"C"}
              tickSize={15}
              data={nutData.temperature}
              curve={"monotoneX"}
              min={0}
              max={60}
            />
          ) : (
            <p>Loading</p>
          )}
        </Box>

        <Box height="18.4vh" width="48.25vw">
          <h3>Power Usage</h3>
          {isViewLoaded ? (
            <LineChart
              xLedge={"Power Usage"}
              yLedge={"Watts"}
              tickSize={15}
              curve={"monotoneX"}
              data={nutData.power}
              min={0}
            />
          ) : (
            <p>Loading</p>
          )}

          <h3>Input Voltage</h3>
          {isViewLoaded ? (
            <LineChart
              xLedge={"Voltage"}
              yLedge={"Volts"}
              tickSize={15}
              data={nutData.voltageIn}
              curve={"monotoneX"}
              min={0}
            />
          ) : (
            <p>Loading</p>
          )}

          <h3>Output voltage</h3>
          {isViewLoaded ? (
            <LineChart
              xLedge={"Voltage"}
              yLedge={"Volts"}
              tickSize={15}
              data={nutData.voltageOut}
              curve={"monotoneX"}
              min={0}
            />
          ) : (
            <p>Loading</p>
          )}

          <h3>UPS Load</h3>
          {isViewLoaded ? (
            <LineChart
              xLedge={"Load Percentage"}
              yLedge={"%"}
              tickSize={15}
              curve={"monotoneX"}
              data={nutData.load}
              min={0}
              max={100}
            />
          ) : (
            <p>Loading</p>
          )}
        </Box>
      </Box>
    </Box>
  );
};

export default NutDashboard;
