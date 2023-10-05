import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import LoginPage from './scenes/login/LoginPage';
import Dashboard from "./scenes/dashboard/dashboard";
import Calendar from "./scenes/calendar/calendar";
import OAuth2Token from "./components/OAuth2Token";
import TwoFactorPage from "./scenes/2fa/TwoFactorPage";
import Checkout from "./scenes/checkout/checkout";

ReactDOM.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<LoginPage/>}/>
                <Route path="login" element={<LoginPage/>}/>
                <Route path="checkout" element={<Checkout/>}/>
                <Route path="2fa" element={<TwoFactorPage/>}/>
                <Route path="register" element={<LoginPage/>}/>
                <Route path="dashboard" element={<Dashboard/>}/>
                <Route path="calendar" element={<Calendar/>}/>
                <Route path="redirect" element={<OAuth2Token/>}/>
            </Routes>
        </BrowserRouter>
    </React.StrictMode>, document.getElementById('root'));
