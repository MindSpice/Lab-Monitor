import { Box, Typography, useTheme } from "@mui/material";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { tokens } from "../../theme";
import CheckOutlinedIcon from "@mui/icons-material/CheckOutlined";
import BlockOutlinedIcon from "@mui/icons-material/BlockOutlined";
import { useEffect, useState } from "react";
import LineChart from "../../components/LineChart";
import mockCpu from "../../data/mockCpu";

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
  },
];

const ClientDashboard = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const [viewData, setViewData] = useState([]);
  const [clientData, setClientData] = useState([]);
  const [isSelectLoaded, setSelectLoaded] = useState(false);

  useEffect(() => {
    const fetchOverview = async () => {
      const response = await fetch("http://127.0.0.1:7568/client_overview");
      const data = await response.json();
      if (data && data.length && response.status === 200) {
        console.log("setting overview data");
        setViewData(data);
      }
    };
    const fetchSelected = async () => {
      const clientName = encodeURIComponent("changeMe");
      const response = await fetch(
        `http://127.0.0.1:7568/client_full?name=${clientName}`
      );
      const data = await response.json();
      if (response.status === 200) {
        setClientData(data);
        setSelectLoaded(true);
      }
    };

    const interval = setInterval(() => {
      fetchOverview();
      fetchSelected();
    }, 30000);

    return () => clearInterval(interval);
  }, []);

  return (
  
    <Box m="20px">
      {/* <Header title="Clients" subtitle="Overview of connected clients" /> */}
      <Box
        m="0px 0 0 0"
        height="40vh"
        width="95.3vw"
        sx={{
          "& .MuiDataGrid-columnHeaders": {
            backgroundColor: colors.blueAccent[700],
          },
          "& .MuiDataGrid-virtualScroller": {
            backgroundColor: colors.primary[400],
          },
          "& .MuiDataGrid-footerContainer": {
            backgroundColor: colors.blueAccent[700],
          },
        }}
      >
        <DataGrid
          density="compact"
          getRowId={(row) => row.address}
          rows={viewData}
          columns={columns}
          //components={{ Toolbar: GridToolbar }}
        />
      </Box>
      <Box display="flex" flexDirection="row">
        <Box height="25vh" width="47vw">
          {isSelectLoaded ? (
            <LineChart
              height="20vh"
              xLedge={"CPU Speed"}
              yLedge={"GHz"}
              data={clientData.cpuSpeed}
            />
          ) : (
            <p>Loading</p>
          )}

          {isSelectLoaded ? (
            <LineChart
              height="20vh"
              xLedge={"CPU Usage"}
              yLedge={"%"}
              data={clientData.cpuUsage}
            />
          ) : (
            <p>Loading</p>
          )}
        </Box>

        <Box>
        <Box height="25vh" width="48vw">
          {isSelectLoaded ? (
            <LineChart
              height="20vh"
              xLedge={"Memory Usage"}
              yLedge={"GB"}
              data={clientData.memSwap}
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
