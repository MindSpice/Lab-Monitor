import { Box } from "@mui/material";
import { useCallback, useEffect, useState } from "react";
import LineChart from "../../components/LineChart";
import Header from "../../components/Header";
import { historySize, apiEndpoint } from "../../settings";

const nutObj = {
  lastTime: -1,
  voltageIn: [],
  voltageOut: [],
  power: [],
  load: [],
  charge: [],
  runtime: [],
  temperature: [],
  online: [],
};

const NutDashboard = () => {
  const [nutData, setNutData] = useState(nutObj);
  const [isViewLoaded, setViewLoaded] = useState(false);


  const updateNutData = useCallback(
    (newData) => {

      if (newData.lastTime <= nutData.lastTime) {
        return;
      }

      const updatedData = { ...nutData };

      if (updatedData.lastTime < 0) {
        updatedData.voltageIn = newData.voltageIn;
        updatedData.voltageOut = newData.voltageOut;
        updatedData.power = newData.power;
        updatedData.load = newData.load;
        updatedData.charge = newData.charge;
        updatedData.runtime = newData.runtime;
        updatedData.temperature = newData.temperature;
        updatedData.online = newData.online;
      } else {
        updatedData.voltageIn[0].data = updatedData.voltageIn[0].data.slice(-historySize - 1);
        updatedData.voltageIn[0].data.push(newData.voltageIn[0].data[0]);

        updatedData.voltageOut[0].data = updatedData.voltageOut[0].data.slice(-historySize - 1);
        updatedData.voltageOut[0].data.push(newData.voltageOut[0].data[0]);

        updatedData.power[0].data = updatedData.power[0].data.slice(-historySize - 1);
        updatedData.power[0].data.push(newData.power[0].data[0]);

        updatedData.load[0].data = updatedData.load[0].data.slice(-historySize - 1);
        updatedData.load[0].data.push(newData.load[0].data[0]);

        updatedData.charge[0].data = updatedData.charge[0].data.slice(-historySize - 1);
        updatedData.charge[0].data.push(newData.charge[0].data[0]);

        updatedData.runtime[0].data = updatedData.runtime[0].data.slice(-historySize - 1);
        updatedData.runtime[0].data.push(newData.runtime[0].data[0]);

        updatedData.temperature[0].data = updatedData.temperature[0].data.slice(-historySize - 1);
        updatedData.temperature[0].data.push(newData.temperature[0].data[0]);

        updatedData.online[0].data = updatedData.online[0].data.slice(-historySize - 1);
        updatedData.online[0].data.push(newData.online[0].data[0]);
      }
      
      updatedData.lastTime = newData.lastTime;
      setNutData(updatedData);
    },
    [nutData]
  );


  const fetchNutData = useCallback(async () => {
    const abortController = new AbortController();
    const signal = abortController.signal;

    try {
      const response =
        nutData.lastTime < 0
          ? await fetch(apiEndpoint + "/nut_full_all", {
              signal,
            })
          : await fetch(apiEndpoint + "/nut_full", {
              signal,
            });

      const data = await response.json();
      if (response.status === 200) {
        updateNutData(data);
        setViewLoaded(true);
      }
    } catch (error) {
      if (error.name === "AbortError") {
        console.log("Fetch request aborted");
      } else {
        console.error("Fetch failed:", error);
      }
    }
    return () => {
      abortController.abort();
    };
  }, [nutData.lastTime, updateNutData]);


  useEffect(() => {
    fetchNutData();
  }, [fetchNutData]);


  useEffect(() => {
    const interval = setInterval(() => {
      fetchNutData();
    }, 60000);

    return () => clearInterval(interval);
  }, [fetchNutData]);


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
