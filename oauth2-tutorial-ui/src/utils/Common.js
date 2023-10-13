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

//2fa
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

//login
export async function getAccountInfo() {
    return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/account', {
        method: 'GET',
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

//payments

export async function getCountryCode(data) {
    return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/location', {
        method: 'POST',
        body: data ? JSON.stringify(data) : "",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getToken()
        }
    });
}

export async function getPaymentMethods(data) {
    return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/payment-methods?country_code=' + data, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getToken()
        }
    });
}

export async function getPaymentMethodIcon(name) {
    return await fetch(process.env.REACT_APP_ADYEN_URL + `/checkoutshopper/images/logos/${name}.svg`, {
        method: 'GET'
    });
}


export async function initiatePayment() {
    return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/adyen/initiate-payment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getToken()
        }
    });
}