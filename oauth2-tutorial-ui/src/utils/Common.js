// return the user data from the session storage
import jwtDecode from "jwt-decode";

export const getUser = () => {
    const userStr = sessionStorage.getItem('user');
    if (userStr) return JSON.parse(userStr);
    else return null;
}

// return the token from the session storage
export const getToken = () => {
    return sessionStorage.getItem('token') || null;
}

// remove the token and user from the session storage
export const removeUserSession = () => {
    sessionStorage.removeItem('token');
    localStorage.removeItem('token');
}

// set the token and user from the session storage
export const setToken = (token) => {
    sessionStorage.setItem('token', token);
    localStorage.setItem('token', token);
}

export async function enableTwoFactor() {
    return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/enable-2fa', {
        method: 'GET',
        headers: {
            Authorization: 'Bearer ' + getToken(),
        },
    });
}

export const twoFactorEnabled = () => {
    const decodedToken = jwtDecode(getToken());
    return decodedToken.two_factor_enabled; // Assuming 'enable_2fa' is a claim in the JWT
}

export async function getAccountInfo() {
    return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/account', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getToken()
        }
    });
}

export async function getPaymentMethods() {
    return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/adyen/payment-methods', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getToken()
        }
    });
}

export async function identifyEmail(email) {
    return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/identify?email=' + email, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
}