import React, {useEffect, useRef, useState} from "react";
import {Box, IconButton} from "@mui/material";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";
import PersonOutlinedIcon from "@mui/icons-material/PersonOutlined";
import Dropdown from "./Dropdown";
import "./Dropdown.css"

const Topbar = () => {

    const [isOpen, setIsOpen] = useState(false);
    const iconButtonRef = useRef(null);

    const handleButtonClick = () => {
        setIsOpen((prevIsOpen) => !prevIsOpen);
    };

    const handleClickOutside = (event) => {
        if (iconButtonRef.current && !iconButtonRef.current.contains(event.target)) {
            setIsOpen(false);
        }
    };

    useEffect(() => {
        document.addEventListener('mousedown', handleClickOutside);

        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    return (
        <>
            <Box display="flex" justifyContent="space-between" p={3}>
                <Box>
                </Box>

                <Box display="flex">
                    <IconButton>
                        <SettingsOutlinedIcon/>
                    </IconButton>
                    <IconButton ref={iconButtonRef} onClick={handleButtonClick}>
                        <div>
                            {isOpen && (<div className="dropdown-container"><Dropdown/></div>)}
                        </div>
                        <PersonOutlinedIcon/>
                    </IconButton>
                </Box>
            </Box>
            <div class="line-topbar"></div>
        </>
    );
};

export default Topbar;
