import React, {useState} from 'react';
import './LoginPage.css';
import {useNavigate} from 'react-router-dom';
import {handleSocialLoginRedirect, identifyEmail, loginUser, setToken} from '../../utils/Common';
import SignUpModal from './SignUpModal';
import {ColorModeContext, themeSettings, useMode} from "../global/theme";
import Topbar from "../global/Topbar";
import {CssBaseline, ThemeProvider} from "@mui/material";
import {getIconPath} from "../checkout/iconUtils";

function LoginPage() {

    const [theme, colorMode] = useMode();
    const inputStyle = themeSettings(theme.palette.mode);

    const navigate = useNavigate();
    const [showModal, setShowModal] = useState(false);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [rememberMe, setRememberMe] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [providers, setProviders] = useState(['GOOGLE', 'GITHUB', 'FACEBOOK', 'OKTA']);
    const [showProvidersMessage, setShowProvidersMessage] = useState(false);
    const [showChosenProviders, setShowChosenProviders] = useState(false);

    const openModal = () => {
        setShowModal(true);
    };

    const closeModal = () => {
        setShowModal(false);
    };

    const revertToDefaultState = () => {
        setEmail(''); // Clear the email input field
        setShowPassword(false); // Hide password input field
        setRememberMe(false); // Uncheck remember me checkbox
        setErrorMessage(''); // Clear error message
        setShowChosenProviders(false); // Hide OAuth2 login buttons
        setShowProvidersMessage(false); // Hide the OAuth2 message
        setProviders(['GOOGLE', 'GITHUB', 'FACEBOOK', 'OKTA']);
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
            const response = await identifyEmail(email);

            if (response.ok) {
                const responseData = await response.json();
                const authProviders = responseData.authProviders;

                // Check if Internal provider is missing
                if (authProviders.length === 0) {
                    setShowPassword(true);
                    setErrorMessage('');
                } else {
                    setErrorMessage('');
                    setShowProvidersMessage(true);
                    setProviders(authProviders || []);
                    setShowChosenProviders(authProviders && authProviders.length > 0);
                }
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
            const response = await loginUser(email, password, rememberMe);
            if (response.ok) {
                const responseData = await response.json();
                const jwtValue = responseData.value;
                setToken(jwtValue);
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
            <ColorModeContext.Provider value={colorMode}>
                <ThemeProvider theme={theme}>
                    <CssBaseline/>
                    <Topbar switchStyleButtonOnly={true}/>
                    <div id='login-container' className="login-container">
                        <h3>Welcome back</h3>
                        <form onSubmit={showPassword ? handlePasswordSubmit : handleLogin}>
                            <input
                                autoFocus={true}
                                type="email"
                                id="email"
                                name="email"
                                className="login-input"
                                style={inputStyle.palette.input}
                                placeholder="Enter email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                                disabled={showChosenProviders}
                            />
                            {showChosenProviders && (
                                <div className="provider-message">
                                    It looks like you've previously logged in using OAuth2.
                                </div>
                            )}
                            {errorMessage && <p className="error-message">{errorMessage}</p>}

                            {showPassword && (
                                <>
                                    <label htmlFor="password">Password</label>
                                    <input
                                        className="login-input"
                                        style={inputStyle.palette.input}
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

                            {!showChosenProviders && (
                                <>
                                    <input type="submit" value={showPassword ? 'Login' : 'Continue'}/>
                                    <input type="button" id="register-btn" value="Create account" onClick={openModal}/>
                                    <div className="login-methods-separator">OR</div>
                                </>
                            )}

                        </form>
                        <div className="social-login-method">
                            {providers.includes("GOOGLE") &&
                                (<button className="btn google" onClick={handleGoogleLogin}>
                                    <span className="provider-icon">{<img src={getIconPath('google')} alt={'google'}/>}</span>
                                    <span className="provider-name--google">Continue with Google</span>
                                </button>)}

                            {providers.includes("FACEBOOK") &&
                                (<button className="btn facebook" onClick={handleFacebookLogin}>
                                    <span className="provider-icon">{<img src={getIconPath('facebook')} alt={'facebook'}/>}</span>
                                    <span className="provider-name">Continue with Facebook</span>
                                </button>)}

                            {providers.includes("GITHUB") &&
                                (<button className="btn github" onClick={handleGithubLogin}>
                                    <span className="provider-icon"><img src={getIconPath('github')} alt={'github'}/></span>
                                    <span className="provider-name">Continue with GitHub</span>
                                </button>)}

                            {providers.includes("OKTA") &&
                                (<button className="btn okta" onClick={handleOktaLogin}>
                                    <span className="provider-icon">{<img src={getIconPath('okta')} alt={'okta'}/>}</span>
                                    <span className="provider-name">Continue with Okta</span>
                                </button>)}
                            {showChosenProviders && (
                                <input type="button" id="back-btn" value="Back" onClick={revertToDefaultState}/>
                            )}
                        </div>
                    </div>
                    <SignUpModal showModal={showModal} onClose={closeModal}/>
                </ThemeProvider>
            </ColorModeContext.Provider>
        </>
    );
}

export default LoginPage;
