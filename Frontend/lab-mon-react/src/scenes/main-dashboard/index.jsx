import { Box, useTheme } from "@mui/material";
import { tokens } from "../../theme";
import RadialChart from "../../components/RadialChart";
import ListBox from "../../components/ListBox";
import columns from "../../components/GridColumns";
import { apiEndpoint } from "../../settings";
import { useEffect, useState, useCallback } from "react";
import { DataGrid } from "@mui/x-data-grid";
import BarChart from "../../components/BarChart";

const MainDashboard = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);

  const [isViewLoaded, setViewLoaded] = useState(false);
  const [nutRadialData, setNutRadialData] = useState({});
  const [nutListData, setNutListData] = useState([]);
  const [overviewData, setOverviewData] = useState([]);
  const [clientBarData, setClientBarData] = useState([]);

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
      } else {
        setViewLoaded(false);
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

  const fetchNutRadial = useCallback(async () => {
    const abortController = new AbortController();
    const signal = abortController.signal;

    try {
      const response = await fetch(apiEndpoint + "/nut_radial_data", {
        signal,
      });
      const data = await response.json();
      if (data && response.status === 200) {
        setNutRadialData(data);
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

  const fetchNutList = useCallback(async () => {
    const abortController = new AbortController();
    const signal = abortController.signal;

    try {
      const response = await fetch(apiEndpoint + "/nut_info_list", {
        signal,
      });
      const data = await response.json();

      if (data && data.length && response.status === 200) {
        setNutListData(data);
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

  const fetchClientBar = useCallback(async () => {
    const abortController = new AbortController();
    const signal = abortController.signal;

    try {
      const response = await fetch(apiEndpoint + "/client_bar_data", {
        signal,
      });
      const data = await response.json();
      console.log(data);
      if (data && response.status === 200) {
        setClientBarData(data);
        console.log(clientBarData);

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
  }, [clientBarData]);

  useEffect(() => {
    fetchOverview();
    fetchNutRadial();
    fetchNutList();
    fetchClientBar();
  }, [fetchOverview, fetchNutRadial, fetchNutList, fetchClientBar]);

  useEffect(() => {
    const interval = setInterval(() => {
      fetchOverview();
      fetchNutRadial();
      fetchNutList();
      fetchClientBar();
    }, 60000);

    return () => clearInterval(interval);
  }, [fetchOverview, fetchNutRadial, fetchNutList, fetchClientBar]);

  return (
    <Box>
      <Box
        height="60vh"
        width="96vw"
        display="flex"
        flexDirection="row"
        alignContent="center"
        textAlign="center"
        justifyContent="center"
      >
        <Box
          height="50vh"
          width="48vw"
          m="85px 0px 0px 0px"
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
          <DataGrid
            density="compact"
            getRowId={(row) => row.address}
            rows={overviewData}
            columns={columns}
          />
        </Box>

        <Box
          display="inline-root"
          flexDirection="column"
          textAlign="center"
          width="33vw"
          height="95%" 
        >
          {nutRadialData.online ? (
            <h3 style={{ color: "green" }}>
              <b>Online</b>
            </h3>
          ) : (
            <h3 style={{ color: "red" }}>
              <b>Offline</b>
            </h3>
          )}

          <div>
            {nutRadialData.testResult ? (
              <p>Test Result: {nutRadialData.testResult}</p>
            ) : (
              <p>Test Result: No Test Status</p>
            )}
          </div>

          {isViewLoaded ? (
            <RadialChart
              data={nutRadialData.data}
              nullColor={colors.primary[400]}
            />
          ) : (
            <p>Loading</p>
          )}
        </Box>

        <Box
          display="inline-flex"
          flexDirection="column"
          textAlign="center"
          width="14vw"
          height="100%" 
        >
          <h3>
            <b>UPS Values</b>
          </h3>
          <ListBox
            data={nutListData}
            style={{
              maxHeight: "50vh",
              width: "12vw",
              overflowY: "auto",
            }}
          />
        </Box>
      </Box>
      <Box height="36vh" width="93.5vw">
        <BarChart
          xLedge={"Client"}
          yLedge={"%"}
          data={clientBarData}
          keys={["CPU_Usage", "Memory_Usage", "Swap_Usage"]}
          indexBy={"name"}
          groupMode={"grouped"}
          barColors={["#81C784", "#4DD0E1", "#FF8A65"]}
          maxValue={100}
          margin={{ top: 5, right: 125, bottom: 20, left: 30 }}
        />
      </Box>
    </Box>
  );
};

export default MainDashboard;
