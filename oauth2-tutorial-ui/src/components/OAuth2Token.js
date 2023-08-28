import React, {useEffect, useState} from 'react';
import {useLocation, useNavigate} from 'react-router-dom'
import {setToken, twoFactorEnabled} from "../utils/Common";
import TwoFactorModal from "../scenes/login/TwoFactorModal";

const OAuth2Token = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const token = searchParams.get('token');
    const [show2fa, setShow2fa] = useState(false);

    const close2fa = () => {
        setShow2fa(false);
    };

    useEffect(() => {
        setToken(token);
        const twoFactor = twoFactorEnabled();
        if (twoFactor === false) {
            navigate('/2fa');
        } else {
            navigate('/dashboard');
        }
    }, []);
    return <TwoFactorModal showModal={show2fa} onClose={close2fa}/>
};

export default OAuth2Token;
