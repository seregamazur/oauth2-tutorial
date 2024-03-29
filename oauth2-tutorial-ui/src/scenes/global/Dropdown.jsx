import './Dropdown.css';
import { removeUserSession } from '../../utils/Common';
import { useNavigate } from 'react-router-dom';
import React from 'react';

const Dropdown = () => {
  const navigate = useNavigate();

  const handleSignOut = () => {
    console.log('removing auth info');
    removeUserSession();
    navigate('/login');
  };

  const handleSubscription = () => {
    console.log('opening subscription');
    navigate('/checkout');
  };

  return (
    <>
      <ul className="dropdown-menu">
        <li onClick={handleSubscription}>Subscription</li>
        <li>Profile</li>
        <li onClick={handleSignOut}>Sign Out</li>
      </ul>
    </>
  );
};

export default Dropdown;
