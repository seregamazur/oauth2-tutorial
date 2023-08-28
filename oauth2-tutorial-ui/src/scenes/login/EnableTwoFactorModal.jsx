import React, {useEffect, useState} from 'react';
import {Button, Form, Modal} from 'react-bootstrap';
import {getToken} from "../../utils/Common";
import OtpInput from 'react-otp-input';
import "./EnableTwoFactorModal.css";
import {createTheme} from "@mui/material/styles";
import {themeSettings} from "../global/theme";
import {Paper} from "@mui/material";

const EnableTwoFactorAuthenticationModal = ({showModal, onClose}) => {

    // const  [theme, colorMode ] = useMode();
    // const inputStyle = themeSettings(theme.palette.mode);
    const theme = createTheme();
    const inputStyle = themeSettings(theme.palette.mode);

    const [showQRCode, setShowQRCode] = useState(false);
    const [authenticatorCode, setAuthenticatorCode] = useState('');
    const [qrCodeImage, setQRCodeImage] = useState('');

    useEffect(() => {
        // Call getUser() when the component mounts
        handleEnable2FA();
    }, []); // Empty dependency array means this effect runs only once on mount

    const handleEnable2FA = async () => {
        // Make a request to enable 2FA and get the QR code image
        try {
            const response = await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/enable-2fa', {
                method: 'GET',
                headers: {
                    Authorization: 'Bearer ' + getToken(),
                },
            });
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
        <Modal className="modal-overlay" show={showModal} onHide={onClose} centered>
            <Paper style={inputStyle.palette.background}>
                <h2>Enable 2FA</h2>
                <Modal.Body>
                    <div className="image-container">
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
                </Modal.Body>
            </Paper>
        </Modal>
    );
};

export default EnableTwoFactorAuthenticationModal;
