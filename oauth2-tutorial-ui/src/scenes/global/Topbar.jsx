import React, {useRef, useState} from "react";
import {Box, IconButton} from "@mui/material";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";
import PersonOutlinedIcon from "@mui/icons-material/PersonOutlined";
import Dropdown from "./Dropdown";
import "./Dropdown.css"

const Topbar = () => {
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);

    const handleButtonClick = () => {
        setIsDropdownOpen(!isDropdownOpen);
    };

    const handleClickOutside = (event) => {
        if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
            setIsDropdownOpen(false);
        }
    };

    return (
        <div>
            <Box display="flex" justifyContent="space-between" p={3}>
                <Box>
                </Box>

                <Box display="flex">
                    <IconButton>
                        <SettingsOutlinedIcon/>
                    </IconButton>
                    <IconButton onClick={handleButtonClick}>
                        <div>{isDropdownOpen && (
                            <div ref={dropdownRef} className="dropdown-container">
                                <Dropdown/></div>)}</div>
                        <PersonOutlinedIcon/>
                    </IconButton>
                </Box>
            </Box>
            <div class="line-topbar"></div>
        </div>
    );
};

export default Topbar;
