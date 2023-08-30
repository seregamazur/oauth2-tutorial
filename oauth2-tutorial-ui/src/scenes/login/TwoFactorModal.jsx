import React, {useState} from 'react';
import {Button, Form, Modal} from 'react-bootstrap';
import {getToken} from "../../utils/Common";
import OtpInput from 'react-otp-input';
import "./TwoFactorModal.css";
import {createTheme} from "@mui/material/styles";
import {themeSettings} from "../global/theme";
import {Paper} from "@mui/material";
import {useNavigate} from "react-router-dom";

const TwoFactorAuthenticationModal = ({showModal, onClose}) => {

    const navigate = useNavigate();
    // const  [theme, colorMode ] = useMode();
    // const inputStyle = themeSettings(theme.palette.mode);
    const theme = createTheme();
    const inputStyle = themeSettings(theme.palette.mode);

    const [authenticatorCode, setAuthenticatorCode] = useState('');

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
                navigate('/dashboard');
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
                <h2>Verify 2FA</h2>
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
                            </div>
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
                </Modal.Body>
            </Paper>
        </Modal>
    );
};

export default TwoFactorAuthenticationModal;
