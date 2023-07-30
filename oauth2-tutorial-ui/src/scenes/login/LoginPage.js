import React, { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faGoogle, faGithub, faFacebookSquare } from '@fortawesome/free-brands-svg-icons';
import { faLock } from '@fortawesome/free-solid-svg-icons';
import './LoginPage.css';
import { useNavigate } from 'react-router-dom';
import { setUserSession } from '../../utils/Common';
import SignUpModal from './SignUpModal';

function LoginPage() {
    const navigate = useNavigate();
    const [showModal, setShowModal] = useState(false);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [rememberMe, setRememberMe] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    const openModal = () => {
        setShowModal(true);
    };

    const closeModal = () => {
        setShowModal(false);
    };

    const handleSocialLoginRedirect = async (siteName) => {
        try {
            window.location.href = process.env.REACT_APP_BACKEND_URL + '/oauth2/authorization/' + siteName;
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const handleGoogleLogin = async () => {
        return handleSocialLoginRedirect('google');
    };

    const handleFacebookLogin = () => {
        return handleSocialLoginRedirect('facebook');
    };

    const handleGithubLogin = () => {
        return handleSocialLoginRedirect('github');
    };

    const handleOktaLogin = () => {
        return handleSocialLoginRedirect('okta');
    };

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/identify?email=' + email, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                setShowPassword(true);
                setErrorMessage('');
            } else if (response.status === 404) {
                setErrorMessage('No user found with this email.');
            } else {
                // Handle login error here
                console.error('Login failed');
            }
        } catch (error) {
            // Handle network or server error here
            console.error('Error:', error);
        }
    };

    const handlePasswordSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('email', email);
        formData.append('password', password);
        formData.append('rememberMe', rememberMe);

        try {
            const response = await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/authenticate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    'email': email,
                    'password': password,
                    'rememberMe': rememberMe
                })
            });

            if (response.ok) {
                // Handle successful login here
                console.log('Login successful');
                setUserSession(response.json().value);
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
        <div className="container">
            <h2>Login</h2>
            <br />
            <form onSubmit={showPassword ? handlePasswordSubmit : handleLogin}>
                <label htmlFor="email">Email</label>
                <input
                    type="text"
                    id="email"
                    name="email"
                    placeholder="Enter email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                {errorMessage && <p className="error-message">{errorMessage}</p>}

                {showPassword && (
                    <>
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            placeholder="Enter password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />

                        <div>
                            <input
                                type="checkbox"
                                id="rememberMe"
                                name="rememberMe"
                                checked={rememberMe}
                                onChange={(e) => setRememberMe(e.target.checked)}
                            />
                            <label htmlFor="rememberMe">Remember me</label>
                        </div>
                    </>
                )}

                <input type="submit" value={showPassword ? 'Login' : 'Next'} />
                <input type="button" id="register-btn" value="Create account" onClick={openModal} />
                <div className="line">
                    <span>OR</span>
                </div>
            </form>

            <div className="social-login">
                <button className="btn google" onClick={handleGoogleLogin}>
                    <FontAwesomeIcon icon={faGoogle}/> Sign in with Google
                </button>
                <button className="btn facebook" onClick={handleFacebookLogin}>
                    <FontAwesomeIcon icon={faFacebookSquare}/> Sign in with Facebook
                </button>
                <button className="btn github" onClick={handleGithubLogin}>
                    <FontAwesomeIcon icon={faGithub}/> Sign in with GitHub
                </button>
                <button className="btn okta" onClick={handleOktaLogin}>
                    <FontAwesomeIcon icon={faLock}/> Sign in with Okta
                </button>
            </div>

            <SignUpModal showModal={showModal} onClose={closeModal} />
        </div>
    );
}

export default LoginPage;
