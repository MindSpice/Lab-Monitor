import React from 'react';
import { Box, useTheme } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import { tokens } from "../../theme";
import { useEffect, useState, useCallback } from "react";
import LineChart from "../../components/LineChart";
import BarChart from "../../components/BarChart";
import { apiEndpoint, historySize } from "../../settings";
import columns from '../../components/GridColumns';



const clientObj = {
  lastTime: -1,
  name: "",
  address: "",
  cpuSpeed: [
    {data: []}
  ],
  cpuUsage: [
    {data: []}
  ],
  memSwap: [
    {data: []}
  ],
  diskData: [
    {data: []}
  ],
};

const ClientDashboard = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const [isSelectLoaded, setSelectLoaded] = useState(false);
  const [isViewLoaded, setViewLoaded] = useState(false);
  const [selectedClient, setSelectedClient] = useState(null);
  const [overviewData, setOverviewData] = useState([]);
  const [clientData, setClientData] = useState(clientObj);


  const updateClientData = useCallback((newData) => {
    if (newData.lastTime <= clientData.lastTime) {
      return;
    }

    const updatedData = { ...clientData };

    if (updatedData.lastTime < 0) {
      updatedData.cpuSpeed = newData.cpuSpeed;
      updatedData.cpuUsage = newData.cpuUsage;
      updatedData.memSwap = newData.memSwap;
    } else {
      for (let i = 0; i < updatedData.cpuSpeed.length; ++i) {
        updatedData.cpuSpeed[i].data = updatedData.cpuSpeed[i].data.slice(-(historySize -1));
        updatedData.cpuSpeed[i].data.push(newData.cpuSpeed[i].data[0]);

        updatedData.cpuUsage[i].data = updatedData.cpuUsage[i].data.slice(-(historySize -1));
        updatedData.cpuUsage[i].data.push(newData.cpuUsage[i].data[0]);
      }

      updatedData.memSwap[0].data = updatedData.memSwap[0].data.slice(-historySize -1);
      updatedData.memSwap[0].data.push(newData.memSwap[0].data[0]);

      updatedData.memSwap[1].data = updatedData.memSwap[1].data.slice(-historySize -1);
      updatedData.memSwap[1].data.push(newData.memSwap[1].data[0]);
    }

    updatedData.lastTime = newData.lastTime;
    updatedData.name = newData.name;
    updatedData.diskData = newData.diskData;
    setClientData(updatedData);

  }, [clientData]);

  const updateSelectedClient = (params, event) => {
    setSelectedClient(params.row.name);
    setClientData(clientObj);
    fetchSelected(params.row.name);
  };


  const fetchOverview = useCallback(async () => {
    const abortController = new AbortController();
    const signal = abortController.signal;

    try {
      const response = await fetch(apiEndpoint + "/client_overview", {
        signal,
      });
      const data = await response.json();

      if (data && data.length && response.status === 200) {
        setOverviewData(data);
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
  }, []);


  const fetchSelected  = useCallback(async (selection) => {
    if (selectedClient == null) {
      return;
    }
    const abortController = new AbortController();
    const signal = abortController.signal;

    try {
      const clientName = encodeURIComponent(selection);

      const response =
        clientData.lastTime < 0 && selectedClient
          ? await fetch(apiEndpoint + `/client_full_all?name=${clientName}`, {
              signal,
            })
          : await fetch(apiEndpoint + `/client_full?name=${clientName}`, {
              signal,
            });

      const data = await response.json();

      if (response.status === 200) {
        updateClientData(data);
        setSelectLoaded(true);
      } else {
        setSelectLoaded(false);
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
  }, [selectedClient, clientData.lastTime, updateClientData]);


  useEffect(() => {
    fetchOverview();
  }, [fetchOverview]);


  useEffect(() => {
    const interval = setInterval(() => {
      fetchOverview();
      fetchSelected(selectedClient);
    }, 60000);

    return () => clearInterval(interval);
  }, [fetchOverview, fetchSelected, selectedClient]);


  return (
    <Box m="5px" textAlign="center">
      {/* <Header title="Clients" subtitle="Overview of connected clients" /> */}
      <Box
        m="0px 0 0px 0"
        height="40vh"
        width="95.3vw"
        sx={{
          "& .MuiDataGrid-columnHeaders": {
            backgroundColor: colors.blueAccent[600],
          },
          "& .MuiDataGrid-virtualScroller": {
            backgroundColor: colors.primary[400],
          },
          "& .MuiDataGrid-footerContainer": {
            backgroundColor: colors.blueAccent[600],
            height: 25,
            minHeight: 10,
          },
        }}
      >
        {isViewLoaded ? (
          <DataGrid
            density="compact"
            getRowId={(row) => row.address}
            rows={overviewData
      }
            columns={columns}
            onRowClick={updateSelectedClient}
            //components={{ Toolbar: GridToolbar }}
          />
        ) : (
          <p>Awaiting Selection....</p>
        )}
      </Box>
      <Box textAlign="center"> <h2>{selectedClient}</h2></Box>
      <Box display="flex" flexDirection="row">
        <Box height="21vh" width="48.25vw">
          <h3>CPU Speed</h3>
          {isSelectLoaded ? (
            <LineChart
              xLedge={"CPU Speed"}
              yLedge={"GHz"}
              tickSize={15}
              curve="monotoneX"
              data={clientData.cpuSpeed}
            />
          ) : (
            <p>Awaiting Selection....</p>
          )}

          <h3>CPU USage</h3>
          {isSelectLoaded ? (
            <LineChart
              xLedge={"CPU Usage"}
              yLedge={"%"}
              tickSize={15}
              curve="monotoneX"
              data={clientData.cpuUsage}
            />
          ) : (
            <p>Awaiting Selection....</p>
          )}
        </Box>

        <Box>
          <Box height="21vh" width="48.25vw">
            <h3>Memory Usage</h3>
            {isSelectLoaded ? (
              <LineChart
                xLedge={"Memory Usage"}
                yLedge={"GB"}
                tickSize={15}
                curve="monotoneX"
                min={0}
                data={clientData.memSwap}
              />
            ) : (
              <p>Awaiting Selection....</p>
            )}

            <h3>Disk Space GB</h3>
            {isSelectLoaded ? (
              <BarChart
                xLedge={"Mount"}
                yLedge={"GB"}
                barColors = {["#9ACD32", "#17a807"]}
                data={clientData.diskData}
                keys = {["used", "free"]}
                indexBy ={"mount"}
                margin={{ top: 0, right: 110, bottom: 5, left: 60 }}
              />
            ) : (
              <p>Awaiting Selection....</p>
            )}
          </Box>
        </Box>
      </Box>
    </Box>
  );
};

// const MemoizedClientDashboard = React.memo(ClientDashboard);
// export default MemoizedClientDashboard;

export default ClientDashboard;