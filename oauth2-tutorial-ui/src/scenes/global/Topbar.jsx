import React, { useContext, useEffect, useRef, useState } from 'react';
import { Box, IconButton, useTheme } from '@mui/material';
import SettingsOutlinedIcon from '@mui/icons-material/SettingsOutlined';
import PersonOutlinedIcon from '@mui/icons-material/PersonOutlined';
import LightModeOutlinedIcon from '@mui/icons-material/LightModeOutlined';
import DarkModeOutlinedIcon from '@mui/icons-material/DarkModeOutlined';
import Dropdown from './Dropdown';
import './Dropdown.css';
import { ColorModeContext, useMode } from './theme';

const Topbar = ({ switchStyleButtonOnly }) => {
  const theme = useTheme();
  const colorMode = useContext(ColorModeContext);

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
        <IconButton onClick={colorMode.toggleColorMode}>
          {theme.palette.mode === 'dark' ? <DarkModeOutlinedIcon /> : <LightModeOutlinedIcon />}
        </IconButton>
        <Box></Box>

        {!switchStyleButtonOnly && (
          <Box display="flex">
            <IconButton>
              <SettingsOutlinedIcon />
            </IconButton>
            <IconButton ref={iconButtonRef} onClick={handleButtonClick}>
              <div>
                {isOpen && (
                  <div className="dropdown-container">
                    <Dropdown />
                  </div>
                )}
              </div>
              <PersonOutlinedIcon />
            </IconButton>
          </Box>
        )}
      </Box>
      {!switchStyleButtonOnly && <div class="line-topbar"></div>}
    </>
  );
};

export default Topbar;
