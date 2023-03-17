import { useState } from "react";
import { ColorModeContext, useMode } from "./theme";
import { CssBaseline, ThemeProvider } from "@mui/material";
import TopBar from "./scenes/topbar/topbar";
import SideBar from "./scenes/sidebar/sidebar"
import MainDashboard from "./scenes/main-dashboard";
import NutDashboard from "./scenes/nut-dashboard";
import ClientDashboard from "./scenes/client-dashboard";
import { Routes, Route } from "react-router-dom";
function App() {
  const [theme, colorMode] = useMode();
  const [isSideBar, setIsSideBar] = useState(true);

  return (
    <ColorModeContext.Provider value={colorMode}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <div className="app">
          <SideBar isSideBar={isSideBar} />
          <main className="content">
            <TopBar setIsSideBar={setIsSideBar} />
            <Routes>
              <Route path="/" element = {<MainDashboard />} />
              <Route path="/nut" element = {<NutDashboard />} />
              <Route path="/clients" element ={<ClientDashboard />} />
            </Routes>
          </main>
        </div>
      </ThemeProvider>
    </ColorModeContext.Provider>
  );
}

export default App;
