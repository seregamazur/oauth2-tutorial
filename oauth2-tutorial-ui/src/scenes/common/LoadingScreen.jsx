import React from 'react';
import { NewtonsCradle } from '@uiball/loaders';
import './load.css';

const LoadingScreen = () => {
  return (
    <div className="loading-screen">
      <NewtonsCradle size={40} speed={1.4} color="black" />
    </div>
  );
};

export default LoadingScreen;
