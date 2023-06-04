import React, {useState} from 'react';
import {Box, CssBaseline, ThemeProvider} from "@mui/material";
import {ColorModeContext, useMode} from "./theme";
import {Route, Routes} from "react-router-dom";
import Topbar from "./scenes/global/Topbar";
import Dashboard from "./scenes/dashboard";
import Sidebar from "./scenes/global/Sidebar";
import Calendar from "./scenes/calendar";

function App() {
    const [theme, colorMode] = useMode();
    const [isSidebar, setIsSidebar] = useState(true);

    return (
        <ColorModeContext.Provider value={colorMode}>
            <ThemeProvider theme={theme}>
                <CssBaseline />
                <div className="app">
                    <Topbar setIsSidebar={setIsSidebar} />
                    <main className="content" style={{ display: "flex" }}>
                        {isSidebar && <Sidebar isSidebar={isSidebar} />}
                        <Box flexGrow={1}>
                            <Routes>
                                <Route path="/" element={<Dashboard />} />
                                <Route path="/calendar" element={<Calendar />} />
                            </Routes>
                        </Box>
                    </main>
                </div>
            </ThemeProvider>
        </ColorModeContext.Provider>
    );
}

export default App;
