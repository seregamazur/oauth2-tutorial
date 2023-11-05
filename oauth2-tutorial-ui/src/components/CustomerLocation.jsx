import React, { useState, useEffect } from 'react';

function CustomerLocation({ onLocationUpdate }) {
  const [error, setError] = useState(null);
  const [locationRequested, setLocationRequested] = useState(false);

  useEffect(() => {
    if (!locationRequested && navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          onLocationUpdate({ latitude, longitude });
        },
        (error) => {
          setError(error.message);
        },
      );
      setLocationRequested(true);
    }
  }, [onLocationUpdate, locationRequested]);

  return <div>{error && <p>Error: {error}</p>}</div>;
}

export default CustomerLocation;
