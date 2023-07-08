import React, {useEffect} from 'react';
import {Navigate, useLocation} from 'react-router-dom'
import {setUserSession} from "../utils/Common";

const OAuth2Token = () => {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const token = searchParams.get('token');

    useEffect(() => {
        console.log(token);

        localStorage.setItem('token', token);
        setUserSession(token)

    }, []);
    return <Navigate to="/dashboard"/>;
};

export default OAuth2Token;
