import { Box, Typography, useTheme } from "@mui/material";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { tokens } from "../../theme";
import CheckOutlinedIcon from "@mui/icons-material/CheckOutlined";
import BlockOutlinedIcon from "@mui/icons-material/BlockOutlined";
import { useEffect, useState } from "react";
import LineChart from "../../components/LineChart";
import BarChart from "../../components/BarChart";
import { apiEndpoint, historySize } from "../../settings";

const columns = [
  {
    field: "isConnected",
    headerName: "STATUS",
    headerAlign: "center",
    align: "center",
    flex: <div className="2"></div>,
    cellClassName: "status-column-cell",
    renderCell: ({ row: isConnected }) => {
      return (
        <Box width="60%" m="0 auto" p="5px" display="flex" borderRadius="0px">
          {isConnected ? (
            <CheckOutlinedIcon style={{ color: "green" }} />
          ) : (
            <BlockOutlinedIcon style={{ color: "red" }} />
          )}
        </Box>
      );
    },
  },
  {
    field: "name",
    headerName: "NAME",
    flex: 1,
    headerAlign: "center",
    align: "center",
  },
  {
    field: "address",
    headerName: "ADDRESS",
    headerAlign: "center",
    align: "center",
    flex: 0.5,
  },
  ,
  {
    field: "os",
    headerName: "SYS INFO",
    headerAlign: "center",
    align: "center",
    flex: 1,
  },
  {
    field: "cpuTemp",
    headerName: "TEMP",
    headerAlign: "center",
    align: "center",
    flex: 0.5,
  },
  {
    field: "cpuAvgSpeed",
    headerName: "CPU SPEED",
    headerAlign: "center",
    align: "center",
    flex: 0.5,
  },
  {
    field: "cpuAvgUsage",
    headerName: "CPU USAGE",
    headerAlign: "center",
    align: "center",
    flex: 0.5,
  },
  {
    field: "cpuThreads",
    headerName: "THREADS",
    headerAlign: "center",
    align: "center",
    flex: 0.5,
  },
  {
    field: "cpuProcesses",
    headerName: "PROCESSES",
    headerAlign: "center",
    align: "center",
    flex: 0.5,
  },
  {
    field: "memoryUsage",
    headerName: "MEMORY",
    headerAlign: "center",
    align: "center",
    flex: 0.5,
  },
  {
    field: "swapUsage",
    headerName: "SWAP",
    headerAlign: "center",
    align: "center",
    flex: 0.5,
  }
];

const clientObj = {
  lastTime: -1,
  name: "",
  address: "",
  cpuSpeed: [],
  cpuUsage: [],
  memSwap: [],
  diskData: [],
}

const ClientDashboard = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const [isSelectLoaded, setSelectLoaded] = useState(false);
  const [isViewLoaded, setViewLoaded] = useState(false);
  const [selectedClient, setSelectedClient] = useState(null);
  const [viewData, setViewData] = useState([]);
  const [clientData, setClientData] = useState(clientObj);



  function updateClientData(newData) {

    // if (newData.name != clientData.name) {
    //   setClientData(clientObj);
    // }

    if (newData.lastTime <= clientData.lastTime) {
      return;
    }
    const updatedData = { ...clientData };
    const maxSize = historySize * 1.2;

    if (updatedData.lastTime < 0) {
      updatedData.cpuSpeed = newData.cpuSpeed;
      updatedData.cpuUsage = newData.cpuUsage;
      updatedData.memSwap = newData.memSwap;
    } else {
      for (let i = 0; i < updatedData.cpuSpeed.length; ++i) {
      updatedData.cpuSpeed[i].data.push(newData.cpuSpeed[i].data[0]);
      updatedData.cpuUsage[i].data.push(newData.cpuUsage[i].data[0]);
      }
      updatedData.memSwap[0].data.push(newData.memSwap[0].data[0]);
      updatedData.memSwap[1].data.push(newData.memSwap[1].data[0]);
    }

    if (updatedData.cpuSpeed.length > maxSize) {
      for (let i = 0; i < updatedData.cpuSpeed.length; ++i) {
        updatedData.cpuSpeed[i].data = updatedData.cpuSpeed[i].data.slice(-maxSize);
        updatedData.cpuUsage[i].data = updatedData.cpuUsage[i].data.slice(-maxSize);
      }
      updatedData.memSwap[0].data = updatedData.memSwap[0].data.slice(-maxSize);
      updatedData.memSwap[1].data = updatedData.memSwap[1].data.slice(-maxSize);
    }
    updatedData.lastTime = newData.lastTime;
    updatedData.diskData = newData.diskData;
    updatedData.name = newData.name;
    setClientData(updatedData)
  }

  const  updateSelectedClient = (params, event) => {
    setSelectedClient(params.row.name);
    fetchSelected();
  }



  const fetchOverview = async () => {
    const response = await fetch(apiEndpoint + "/client_overview");
    const data = await response.json();
    if (data && data.length && response.status === 200) {
      setViewData(data);
      setViewLoaded(true);
    } else {
      setViewLoaded(false);
    }
  };

  const fetchSelected = async () => {
    if (selectedClient == null) {
      return;
    }
    const clientName = encodeURIComponent(selectedClient);
    console.log(selectedClient)

    const response = 
    clientData.lastTime < 0  && selectedClient 
    ? await fetch(apiEndpoint + `/client_full_all?name=${clientName}`)
    : await fetch(apiEndpoint + `/client_full?name=${clientName}`)

    const data = await response.json();

    if (response.status === 200) {
      updateClientData(data);

      setSelectLoaded(true);
    } else {
      setSelectLoaded(false);
    }
  };


  useEffect (() => {
    fetchOverview();
    fetchSelected();
  }, []);

  useEffect(() => {

    const interval = setInterval(() => {
      fetchOverview();
      fetchSelected();
    }, 60000);

    return () => clearInterval(interval);
  }, [clientData, updateClientData, selectedClient]);

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
            rows={viewData}
            columns={columns}
            onRowClick={updateSelectedClient}
            //components={{ Toolbar: GridToolbar }}
          />
        ) : (
          <p>Loading</p>
        )}
      </Box>
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
            <p>Loading</p>
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
            <p>Loading</p>
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
                data={clientData.memSwap}
              />
            ) : (
              <p>Loading</p>
            )}

            <h3>Disk Space GB</h3>
            {isSelectLoaded ? (
              <BarChart
                xLedge={"Mount"}
                yLedge={"GB"}
                data={clientData.diskData}
              />
            ) : (
              <p>Loading</p>
            )}
          </Box>
        </Box>
      </Box>
    </Box>
  );
};

export default ClientDashboard;

