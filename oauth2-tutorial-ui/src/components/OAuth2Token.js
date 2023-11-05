import React, {useEffect} from 'react';
import {useLocation, useNavigate} from 'react-router-dom'
import {setToken} from "../utils/Common";

const OAuth2Token = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const token = searchParams.get('token');

    useEffect(() => {
        setToken(token);
        navigate('/2fa');
    }, []);
    return <></>
};

export default OAuth2Token;
