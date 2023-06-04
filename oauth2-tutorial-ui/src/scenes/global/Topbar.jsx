import React, {useContext} from "react";
import {Box, IconButton, useTheme} from "@mui/material";
import {ColorModeContext, tokens} from "../../theme";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";
import PersonOutlinedIcon from "@mui/icons-material/PersonOutlined";

const Topbar = () => {

    return (
        <Box display="flex" justifyContent="space-between" p={3}>
            <Box>
            </Box>

            <Box display="flex">
                <IconButton>
                    <SettingsOutlinedIcon/>
                </IconButton>
                <IconButton>
                    <PersonOutlinedIcon/>
                </IconButton>
            </Box>
        </Box>
    );
};

export default Topbar; 
