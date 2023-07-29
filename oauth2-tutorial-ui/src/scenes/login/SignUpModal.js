import React, {useState} from 'react';
import {Modal, Form, Button} from 'react-bootstrap';
import './SignUpModal.css'; // Import the CSS file

const SignUpModal = ({showModal, onClose}) => {
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
            sendRegisterUser()
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
                onClose();
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
        <Modal show={showModal} onHide={onClose} centered>
            <Modal.Header>
                <Modal.Title>Sign Up</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form id="signup-form">
                    <Form.Group controlId="formFirstName">
                        <Form.Label>First Name</Form.Label>
                        <Form.Control
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
                            type="text"
                            placeholder="Enter email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </Form.Group>

                    <Form.Group controlId="formPassword">
                        <Form.Label>Password</Form.Label>
                        <Form.Control
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
            </Modal.Body>
        </Modal>
    );
};

export default SignUpModal;
