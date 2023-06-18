import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import App from './App';
import LoginPage from './scenes/login/LoginPage';
import Dashboard from "./scenes/dashboard";
import Calendar from "./scenes/calendar";
import OAuth2Token from "./components/OAuth2Token";

ReactDOM.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route index element={<App/>}/>
                <Route path="login" element={<LoginPage/>}/>
                <Route path="dashboard" element={<Dashboard/>}/>
                <Route path="calendar" element={<Calendar/>}/>
                <Route path="redirect" element={<OAuth2Token/>}/>
            </Routes>
        </BrowserRouter>
    </React.StrictMode>, document.getElementById('root'));
