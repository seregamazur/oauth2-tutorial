import React, {useState} from 'react';
import { Button, Modal, Form } from 'react-bootstrap';
import {getToken} from "../../utils/Common";
import OtpInput from 'react-otp-input';
import "./TwoFactorModal.css";
import {useNavigate} from 'react-router-dom';
import {createTheme} from "@mui/material/styles";

const TwoFactorAuthenticationModal = ({showModal, onClose}) => {
    const theme = createTheme();
    const navigate = useNavigate();
    const [showQRCode, setShowQRCode] = useState(false);
    const [authenticatorCode, setAuthenticatorCode] = useState('');
    const [qrCodeImage, setQRCodeImage] = useState('');

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
                navigate('/dashboard');
                // Handle successful verification
            } else {
                // Handle verification error
            }
        } catch (error) {
            // Handle network or other errors
        }
    };

    const skip2FA = async () => {
        onClose();
        console.log('Skipping 2FA');
        navigate('/dashboard');
    }

    return (
        <Modal show={showModal} onHide={onClose} contentLabel="Enable 2FA" centered>
            <Modal.Body>
                <div className={`modal-content ${theme ? "" : "custom-modal-content"}`}>
                    <h3>Enable Two-Factor Authentication</h3>
                    {showQRCode ? (
                        <>
                            <img src={qrCodeImage} alt="QR Code" />
                            <Form.Group controlId="authenticatorCode">
                                <Form.Label>Enter Authenticator Code</Form.Label>
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
                    ) : (
                        <>
                            <p>We recommend turning on two-factor authentication:</p>
                            <div className="modal-button-row">
                                <Button variant="primary" className="btn-register" onClick={handleEnable2FA}>
                                    Turn on
                                </Button>
                                <Button variant="secondary" className="btn-cancel" onClick={skip2FA}>
                                    Cancel
                                </Button>
                            </div>
                        </>
                    )}
                </div>
            </Modal.Body>
        </Modal>
    );
};

export default TwoFactorAuthenticationModal;
