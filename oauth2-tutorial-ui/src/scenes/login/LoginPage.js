import React from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faGoogle, faGithub, faFacebookSquare} from '@fortawesome/free-brands-svg-icons';
import {faLock} from '@fortawesome/free-solid-svg-icons';
import './LoginPage.css';


function LoginPage() {
    const openRegister = () => {

    };
    const handleSocialLoginRedirect = async (siteName) => {
        try {
            window.location.href = process.env.REACT_APP_BACKEND_URL + '/oauth2/authorization/' + siteName;
        } catch (error) {
            console.error('Error:', error);
        }
    }

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

    return (
        <div className="container">
            <h2>Login</h2>
            <br/>
            <form action="oauth2-tutorial-ui/src/login/LoginPage#" method="post">
                <label htmlFor="username">Username</label>
                <input type="text" id="username" name="username" placeholder="Enter username" required/>

                <label htmlFor="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter password" required/>

                <input type="submit" value="Login"/>
                <input type="button" id="register-btn" value="Create account" onClick={openRegister}/>
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

        </div>
    );
}

export default LoginPage;
