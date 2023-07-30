import React, { useState } from 'react';

const UserCreationModal = ({ showModal, setShowModal }) => {
    const [fullName, setFullName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleCreateUser = () => {
        // Perform user creation logic here
        console.log('Creating user:', fullName, email, password);
        // Reset form fields
        setFullName('');
        setEmail('');
        setPassword('');
        // Close the modal
        setShowModal(false);
    };

    const closeModal = () => {
        setShowModal(false);
    };

    return (
        showModal && (
            <div className="modal-overlay">
                <div className="modal">
                    <h2>User Creation</h2>
                    <label>
                        Full Name:
                        <input
                            type="text"
                            value={fullName}
                            onChange={(e) => setFullName(e.target.value)}
                        />
                    </label>
                    <label>
                        Email:
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </label>
                    <label>
                        Password:
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </label>
                    <div className="modal-buttons">
                        <button onClick={handleCreateUser}>Create</button>
                        <button onClick={closeModal}>Quit</button>
                    </div>
                </div>
            </div>
        )
    );
};

export default UserCreationModal;
