import React, {useEffect} from 'react';
import {Navigate, useLocation} from 'react-router-dom'

const OAuth2Token = () => {
    const location = useLocation();

    useEffect(() => {
        const {info} = location.state || {};
        console.log(info);
    }, []);

    return <Navigate to="/dashboard"/>;
};

export default OAuth2Token;
