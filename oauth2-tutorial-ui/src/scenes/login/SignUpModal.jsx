import React, {useState} from 'react';
import {Button, Form, Modal} from 'react-bootstrap';
import './SignUpModal.css';
import {setUserSession} from "../../utils/Common";
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

        // Check form validity
        if (form.checkValidity()) {
            // Perform user creation logic here
            console.log('Creating user:', firstName, lastName, email, password);
            // Reset form fields
            setFirstName('');
            setLastName('');
            setEmail('');
            setPassword('');
            // Close the modal
            sendRegisterUser();
        } else {
            // If the form is invalid, trigger the HTML5 form validation messages
            form.reportValidity();
        }
    };

    const sendRegisterUser = async () => {

        try {
            const response = await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/register', {
                method: 'POST',
                body: JSON.stringify({
                    'email': email,
                    'firstName': firstName,
                    'lastName': lastName,
                    'password': password
                }),
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const responseData = await response.json();
                const jwtValue = responseData.value;
                setUserSession(jwtValue);
                // Handle successful login here
                console.log('Login successful');
                onClose();
                navigate('/dashboard');
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
                    <h2>Sign Up</h2>
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
