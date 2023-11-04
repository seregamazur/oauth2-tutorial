import React, {useEffect, useState} from 'react';
import {Box, CssBaseline, ThemeProvider} from "@mui/material";
import {ColorModeContext, tokens, useMode} from "../global/theme";
import EmailIcon from "@mui/icons-material/Email";
import PointOfSaleIcon from "@mui/icons-material/PointOfSale";
import PersonAddIcon from "@mui/icons-material/PersonAdd";
import TrafficIcon from "@mui/icons-material/Traffic";
import Header from "../../components/Header";
import StatBox from "../../components/StatBox";
import Topbar from "../global/Topbar";
import Sidebar from "../global/Sidebar";
import {getAccountInfo, getToken} from "../../utils/Common";

const Dashboard = () => {
    const [theme, colorMode] = useMode();
    const colors = tokens(theme.palette.mode);

    const [isSidebar, setIsSidebar] = useState(true);

    const getUser = async () => {
        try {
            const response = await getAccountInfo();

            if (response.ok) {
                const responseData = await response.json();
                console.log(responseData);

                // Map the relevant fields to your Java object structure
                const user = {
                    id: responseData.id,
                    createdDate: new Date(responseData.createdDate),
                    lastModifiedDate: new Date(responseData.lastModifiedDate),
                    firstName: responseData.firstName,
                    lastName: responseData.lastName,
                    email: responseData.email,
                    authProviders: responseData.authProviders,
                    twoFactorEnabled: responseData.twoFactorEnabled
                };
                return user; // Return the mapped user object
            } else if (response.status === 404) {
                throw new Error('No user found with this email.');
            } else {
                throw new Error('Login failed');
            }
        } catch (error) {
            throw new Error('Error: ' + error.message);
        }
    };

    return (
        <>
            <ColorModeContext.Provider value={colorMode}>
                <ThemeProvider theme={theme}>
                    <CssBaseline/>
                    <div className="app">
                        <Topbar switchStyleButtonOnly={false}/>
                        <div className="content" style={{display: "flex"}}>
                            {isSidebar && <Sidebar isSidebar={isSidebar}/>}
                            <Box flexGrow={1}>
                                <Box m="20px">
                                    {/* HEADER */}
                                    <Box display="flex" justifyContent="space-between" alignItems="center">
                                        <Header title="OAuth2 Board" subtitle="Welcome"/>
                                    </Box>

                                    {/* GRID & CHARTS */}
                                    <Box
                                        display="grid"
                                        gridTemplateColumns="repeat(12, 1fr)"
                                        gridAutoRows="140px"
                                        gap="20px"
                                    >
                                        {/* ROW 1 */}
                                        <Box
                                            gridColumn="span 3"
                                            backgroundColor={colors.primary[400]}
                                            display="flex"
                                            alignItems="center"
                                            justifyContent="center"
                                        >
                                            <StatBox
                                                title="12,361"
                                                subtitle="Emails Sent"
                                                progress="0.75"
                                                increase="+14%"
                                                icon={
                                                    <EmailIcon
                                                        sx={{color: colors.greenAccent[600], fontSize: "26px"}}
                                                    />
                                                }
                                            />
                                        </Box>
                                        <Box
                                            gridColumn="span 3"
                                            backgroundColor={colors.primary[400]}
                                            display="flex"
                                            alignItems="center"
                                            justifyContent="center"
                                        >
                                            <StatBox
                                                title="431,225"
                                                subtitle="Sales Obtained"
                                                progress="0.50"
                                                increase="+21%"
                                                icon={
                                                    <PointOfSaleIcon
                                                        sx={{color: colors.greenAccent[600], fontSize: "26px"}}
                                                    />
                                                }
                                            />
                                        </Box>
                                        <Box
                                            gridColumn="span 3"
                                            backgroundColor={colors.primary[400]}
                                            display="flex"
                                            alignItems="center"
                                            justifyContent="center"
                                        >
                                            <StatBox
                                                title="32,441"
                                                subtitle="New Clients"
                                                progress="0.30"
                                                increase="+5%"
                                                icon={
                                                    <PersonAddIcon
                                                        sx={{color: colors.greenAccent[600], fontSize: "26px"}}
                                                    />
                                                }
                                            />
                                        </Box>
                                        <Box
                                            gridColumn="span 3"
                                            backgroundColor={colors.primary[400]}
                                            display="flex"
                                            alignItems="center"
                                            justifyContent="center"
                                        >
                                            <StatBox
                                                title="1,325,134"
                                                subtitle="Traffic Received"
                                                progress="0.80"
                                                increase="+43%"
                                                icon={
                                                    <TrafficIcon
                                                        sx={{color: colors.greenAccent[600], fontSize: "26px"}}
                                                    />
                                                }
                                            />
                                        </Box>
                                    </Box>
                                </Box>
                            </Box>
                        </div>
                    </div>

                </ThemeProvider>

            </ColorModeContext.Provider>
        </>
    );
};

export default Dashboard;
