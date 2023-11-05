import React, { useEffect, useState } from 'react';
import { enableTwoFactor, twoFactorEnabled, verifyTwoFactor } from '../../utils/Common';
import './two-factor.css';
import { Button, Form } from 'react-bootstrap';
import OtpInput from 'react-otp-input';
import { CssBaseline, ThemeProvider } from '@mui/material';
import { ColorModeContext, useMode } from '../global/theme';
import { useNavigate } from 'react-router-dom';

const TwoFactorPage = () => {
  const [theme, colorMode] = useMode();
  const navigate = useNavigate();

  const [authenticatorCode, setAuthenticatorCode] = useState('');
  const [qrCodeImage, setQRCodeImage] = useState('');
  const [verify2Fa, setVerify2Fa] = useState('');

  const onCloseVerify = () => {
    navigate('/login');
  };

  const onCloseEnable = () => {
    navigate('/dashboard');
  };

  useEffect(() => {
    setVerify2Fa(twoFactorEnabled());
    if (verify2Fa) {
      handleVerify2FA();
    } else {
      handleEnable2FA();
    }
  }, []);

  const handleEnable2FA = async () => {
    try {
      const response = await enableTwoFactor();
      if (response.ok) {
        const qrCodeImageBlob = await response.blob();
        const qrCodeImageUrl = URL.createObjectURL(qrCodeImageBlob);
        setQRCodeImage(qrCodeImageUrl);
      } else {
      }
    } catch (error) {}
  };

  const handleVerify2FA = async () => {
    try {
      const response = await verifyTwoFactor(authenticatorCode);
      if (response.ok) {
        onCloseEnable();
      }
    } catch (error) {}
  };

  return (
    <>
      <ColorModeContext.Provider value={colorMode}>
        <ThemeProvider theme={theme}>
          <CssBaseline />
          {verify2Fa && (
            <div className="two-factor-page">
              <h3>Verify Your 2FA</h3>
              <>
                <div className="info-text">
                  <p>
                    Previously, you've taken an important step to enhance the security of your
                    account by setting up Two-Factor Authentication (2FA). With 2FA, your account is
                    better protected.
                  </p>
                  <p>
                    Authenticator apps and browser extensions like 1Password, Authy, Microsoft
                    Authenticator, and others generate one-time passwords, which serve as a second
                    layer of security to confirm your identity when prompted during the sign-in
                    process.
                  </p>
                </div>
                <br />
                <h4>Enter Authenticator Code</h4>
                <Form.Group controlId="authenticatorCode">
                  <OtpInput
                    shouldAutoFocus={true}
                    value={authenticatorCode}
                    onChange={setAuthenticatorCode}
                    isInputNum={true}
                    numInputs={6}
                    inputStyle="otp"
                    renderInput={(props) => <input {...props} />}
                  />
                </Form.Group>

                <div className="modal-button-row">
                  <Button className="btn--verify" onClick={handleVerify2FA}>
                    Verify
                  </Button>
                  <Button className="btn--cancel" onClick={onCloseVerify}>
                    Cancel
                  </Button>
                </div>
              </>
            </div>
          )}
          {!verify2Fa && (
            <div className="two-factor-page">
              <h3>Enable 2FA</h3>
              <>
                <div className="info-text">
                  <h4>Setup authenticator app</h4>
                  <p>
                    Authenticator apps and browser extensions like 1Password, Authy, Microsoft
                    Authenticator, etc. generate one-time passwords that are used as a second factor
                    to verify your identity when prompted during sign-in.
                  </p>
                  <h4>Scan the QR code</h4>
                  <p>
                    Use an authenticator app or browser extension to scan. Learn more about enabling
                    2FA.
                  </p>
                </div>
                <img src={qrCodeImage} alt="QR Code" />
                <br />
                <h4>Enter Authenticator Code</h4>
                <Form.Group controlId="authenticatorCode">
                  <OtpInput
                    shouldAutoFocus={true}
                    value={authenticatorCode}
                    onChange={setAuthenticatorCode}
                    isInputNum={true}
                    numInputs={6}
                    inputStyle="otp"
                    renderInput={(props) => <input {...props} />}
                  />
                </Form.Group>

                <div className="modal-button-row">
                  <Button className="btn--verify" onClick={handleVerify2FA}>
                    Enable
                  </Button>
                  <Button className="btn--cancel" onClick={onCloseEnable}>
                    Cancel
                  </Button>
                </div>
              </>
            </div>
          )}
        </ThemeProvider>
      </ColorModeContext.Provider>
    </>
  );
};
export default TwoFactorPage;
