import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import App from './App';
import LoginPage from './scenes/login/LoginPage';

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
        <React.StrictMode>
            <BrowserRouter>
                <Routes>
                    <Route index element={<App />} />
                    <Route path="login" element={<LoginPage />} />
                </Routes>
            </BrowserRouter>
        </React.StrictMode>
);
