import { Box } from "@mui/material";
import Header from "../../components/Header";

const MainDashboard = () => {
  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="Home" subtitle="Dashboard overview" />
      </Box>
    </Box>
  );
};

export default MainDashboard;
