import React from 'react';
import { Box, Typography, useTheme } from '@mui/material';
import { tokens } from '../scenes/global/theme';
import PropTypes from 'prop-types';

const Header = ({ title, subtitle }) => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  return (
    <Box mb="30px">
      <Typography variant="h2" color={colors.grey[100]} fontWeight="bold" sx={{ m: '0 0 5px 0' }}>
        {title}
      </Typography>
      <Typography variant="h5" color={colors.greenAccent[400]}>
        {subtitle}
      </Typography>
    </Box>
  );
};

Header.propTypes = {
  title: PropTypes.func.isRequired,
  subtitle: PropTypes.func.isRequired,
};

export default Header;
