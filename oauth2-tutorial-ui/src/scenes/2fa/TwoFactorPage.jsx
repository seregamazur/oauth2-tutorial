import React, {useEffect, useState} from 'react';
import {enableTwoFactor, getToken} from '../../utils/Common';
import './TwoFactorPage.css'
import {Button, Form} from "react-bootstrap";
import OtpInput from "react-otp-input";
import Topbar from "../global/Topbar";
import {CssBaseline, ThemeProvider} from "@mui/material";
import {ColorModeContext, useMode} from "../global/theme";
import {useNavigate} from "react-router-dom";

const TwoFactorPage = () => {

    const [theme, colorMode] = useMode();
    const navigate = useNavigate();

    const [showQRCode, setShowQRCode] = useState(false);
    const [authenticatorCode, setAuthenticatorCode] = useState('');
    const [qrCodeImage, setQRCodeImage] = useState('');

    const onClose = () => {
        navigate('/dashboard');
    }

    useEffect(() => {
        // Call getUser() when the component mounts
        handleEnable2FA();
    }, []); // Empty dependency array means this effect runs only once on mount

    const handleEnable2FA = async () => {
        // Make a request to enable 2FA and get the QR code image
        try {
            const response = await enableTwoFactor();
            if (response.ok) {
                const qrCodeImageBlob = await response.blob();
                const qrCodeImageUrl = URL.createObjectURL(qrCodeImageBlob);
                setQRCodeImage(qrCodeImageUrl);
                setShowQRCode(true);
            } else {
                // Handle error
            }
        } catch (error) {
            // Handle network or other errors
        }
    };

    const handleVerify2FA = async () => {
        // Make a request to verify 2FA using the entered authenticator code
        try {
            const response = await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/verify-2fa?totpCode=' + authenticatorCode, {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + getToken(),
                    'Content-Type': 'application/json',
                }
            });
            if (response.ok) {
                onClose();
                console.log('2FA successful');
            } else {
                // Handle verification error
            }
        } catch (error) {
            // Handle network or other errors
        }
    };

    return (
        <>
            <ColorModeContext.Provider value={colorMode}>
                <ThemeProvider theme={theme}>
                    <CssBaseline/>
                    <Topbar switchStyleButtonOnly={true}/>
                    <div className="two-factor-page">
                        <h2>Enable 2FA</h2>
                        <>
                            <div className="info-text">
                                <h3>Setup authenticator app</h3>
                                <p>
                                    Authenticator apps and browser extensions like 1Password, Authy, Microsoft Authenticator, etc. generate
                                    one-time
                                    passwords that are used as a second factor to verify your identity when prompted during sign-in.
                                </p>
                                <h3>Scan the QR code</h3>
                                <p>
                                    Use an authenticator app or browser extension to scan. Learn more about enabling 2FA.
                                </p>
                            </div>
                            <img src={qrCodeImage} alt="QR Code"/>
                            <br/>
                            <h3>Enter Authenticator Code</h3>
                            <Form.Group controlId="authenticatorCode">
                                <OtpInput
                                    shouldAutoFocus={true}
                                    value={authenticatorCode}
                                    onChange={setAuthenticatorCode}
                                    isInputNum={true}
                                    numInputs={6}
                                    inputStyle="inputStyle"
                                    renderInput={(props) => <input {...props} />}
                                />
                            </Form.Group>

                            <div className="modal-button-row">
                                <Button variant="primary" className="btn-register" onClick={handleVerify2FA}>
                                    Verify
                                </Button>
                                <Button variant="secondary" className="btn-cancel" onClick={onClose}>
                                    Cancel
                                </Button>
                            </div>
                        </>
                    </div>
                </ThemeProvider>
            </ColorModeContext.Provider>
        </>
    );
};
export default TwoFactorPage;
