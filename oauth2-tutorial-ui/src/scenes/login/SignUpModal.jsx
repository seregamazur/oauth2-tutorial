import React, {useState} from 'react';
import {Button, Form, Modal} from 'react-bootstrap';
import './SignUpModal.css';
import {registerUser, setToken} from "../../utils/Common";
import {useNavigate} from "react-router-dom";
import {Paper, useTheme} from "@mui/material";
import {themeSettings} from "../global/theme";

const SignUpModal = ({showModal, onClose}) => {
    const navigate = useNavigate();
    const theme = useTheme();
    const inputStyle = themeSettings(theme.palette.mode);

    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleCreateUser = () => {
        // Get the form element
        const form = document.getElementById('signup-form');

        if (form.checkValidity()) {
            // Perform user creation logic here
            console.log('Creating user:', firstName, lastName, email, password);
            setFirstName('');
            setLastName('');
            setEmail('');
            setPassword('');
            sendRegisterUser();
        } else {
            form.reportValidity();
        }
    };

    const sendRegisterUser = async () => {
        try {
            const response = await registerUser(email, firstName, lastName, password);
            if (response.ok) {
                const responseData = await response.json();
                const jwtValue = responseData.value;
                setToken(jwtValue);
                onClose();
                navigate('/2fa');
            } else {
                // Handle login error here
                console.error('Login failed');
            }
        } catch (error) {
            // Handle network or server error here
            console.error('Error:', error);
        }
    };

    return (
        <>
            <Modal className="modal-overlay" show={showModal} onHide={onClose} centered>
                <Paper style={inputStyle.palette.background}>
                    <h3>Sign Up</h3>
                    <Form id="signup-form">
                        <Form.Group controlId="formFirstName">
                            <Form.Label>First Name</Form.Label>
                            <Form.Control
                                className="signup-input"
                                style={inputStyle.palette.input}
                                type="text"
                                placeholder="Enter first name"
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                                required
                            />
                        </Form.Group>
                        <Form.Group controlId="formLastName">
                            <Form.Label>Last Name</Form.Label>
                            <Form.Control
                                className="signup-input"
                                style={inputStyle.palette.input}
                                type="text"
                                placeholder="Enter last name"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Form.Group controlId="formEmail">
                            <Form.Label>Your email</Form.Label>
                            <Form.Control
                                className="signup-input"
                                style={inputStyle.palette.input}
                                type="email"
                                placeholder="Enter email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Form.Group controlId="formPassword">
                            <Form.Label>Password</Form.Label>
                            <Form.Control
                                className="signup-input"
                                style={inputStyle.palette.input}
                                type="password"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <div className="modal-button-row">
                            <Button variant="primary" className="btn-register" onClick={handleCreateUser}>
                                Sign Up
                            </Button>
                            <Button variant="secondary" className="btn-cancel" onClick={onClose}>
                                Cancel
                            </Button>
                        </div>
                    </Form>
                </Paper>
            </Modal>
        </>
    );
};

export default SignUpModal;
