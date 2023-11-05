import React, { useState, useEffect } from 'react';

function CustomerLocation({ onLocationUpdate }) {
  const [latitude, setLatitude] = useState(null);
  const [longitude, setLongitude] = useState(null);
  const [error, setError] = useState(null);
  const [locationRequested, setLocationRequested] = useState(false);

  useEffect(() => {
    if (!locationRequested && navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          setLatitude(latitude);
          setLongitude(longitude);
          // Call the callback function with the location data
          onLocationUpdate({ latitude, longitude });
        },
        (error) => {
          setError(error.message);
        },
      );
      setLocationRequested(true); // Mark that location has been requested
    }
  }, [onLocationUpdate, locationRequested]);

  return <div>{error && <p>Error: {error}</p>}</div>;
}

export default CustomerLocation;
