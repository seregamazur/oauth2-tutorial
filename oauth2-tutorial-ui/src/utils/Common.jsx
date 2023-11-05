// return the user data from the session storage
import jwtDecode from 'jwt-decode';

export const getUser = () => {
  const userStr = sessionStorage.getItem('user');
  if (userStr) return JSON.parse(userStr);
  else return null;
};

// return the token from the session storage
export const getToken = () => {
  return sessionStorage.getItem('token') || null;
};

// remove the token and user from the session storage
export const removeUserSession = () => {
  sessionStorage.removeItem('token');
  localStorage.removeItem('token');
};

// set the token and user from the session storage
export const setToken = (token) => {
  sessionStorage.setItem('token', token);
  localStorage.setItem('token', token);
};

//2fa
export async function enableTwoFactor() {
  return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/enable-2fa', {
    method: 'GET',
    headers: {
      Authorization: 'Bearer ' + getToken(),
    },
  });
}

export async function verifyTwoFactor(authenticatorCode) {
  return await fetch(
    process.env.REACT_APP_BACKEND_URL + '/api/v1/verify-2fa?totpCode=' + authenticatorCode,
    {
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + getToken(),
        'Content-Type': 'application/json',
      },
    },
  );
}

export const twoFactorEnabled = () => {
  const decodedToken = jwtDecode(getToken());
  return decodedToken.two_factor_enabled;
};

//login
export async function loginUser(email, password, rememberMe) {
  return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/authenticate', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: new URLSearchParams({
      email: email,
      password: password,
      rememberMe: rememberMe,
    }),
  });
}

export async function registerUser(email, firstName, lastName, password) {
  return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/register', {
    method: 'POST',
    body: JSON.stringify({
      email: email,
      firstName: firstName,
      lastName: lastName,
      password: password,
    }),
    headers: {
      'Content-Type': 'application/json',
    },
  });
}

export async function getAccountInfo() {
  return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/account', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + getToken(),
    },
  });
}

export async function identifyEmail(email) {
  return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/identify?email=' + email, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });
}

export async function handleSocialLoginRedirect(siteName) {
  try {
    window.location.href = process.env.REACT_APP_BACKEND_URL + '/oauth2/authorization/' + siteName;
  } catch (error) {
    console.error('Error:', error);
  }
}

//payments

export async function getCountryCode(data) {
  return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/location', {
    method: 'POST',
    body: data ? JSON.stringify(data) : '',
    headers: {
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + getToken(),
    },
  });
}

export async function getPaymentMethods(data) {
  return await fetch(
    process.env.REACT_APP_BACKEND_URL + '/api/v1/payment-methods?country_code=' + data,
    {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + getToken(),
      },
    },
  );
}

export async function getPaymentMethodIcon(name) {
  return await fetch(
    process.env.REACT_APP_ADYEN_URL + `/checkoutshopper/images/logos/${name}.svg`,
    {
      method: 'GET',
    },
  );
}

export async function initiatePayment() {
  return await fetch(process.env.REACT_APP_BACKEND_URL + '/api/v1/adyen/initiate-payment', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + getToken(),
    },
  });
}
