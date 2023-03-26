import { Box } from "@mui/material";
import CheckOutlinedIcon from "@mui/icons-material/CheckOutlined";
import BlockOutlinedIcon from "@mui/icons-material/BlockOutlined";

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
            <CheckOutlinedIcon style={{ color: "#AAFF00" }} />
          ) : (
            <BlockOutlinedIcon style={{ color: "#D22B2B" }} />
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
  },
];

export default columns;
