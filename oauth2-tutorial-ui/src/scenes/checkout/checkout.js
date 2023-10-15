import React, {useEffect, useState} from 'react';
import './checkout.css';
import CustomerLocation from '../../components/CustomerLocation';
import {getCountryCode, getPaymentMethods, getToken} from '../../utils/Common';
import AdyenCheckout from "@adyen/adyen-web";
import {getIconPath} from "./paymentMethodIcon";

const clientKey = 'test_QOXTJVU2NBG5RKGDF47XYUZJ2UMYOZS4'

const Checkout = () => {
        // State to store location data
        const [locationData, setLocationData] = useState(null);
        const [paymentMethodsDivs, setPaymentMethodsDivs] = useState([]);
        const [openDivId, setOpenDivId] = useState(null);
        const [selectedDiv, setSelectedDiv] = useState(null);

        // Function to handle location updates
        const handleLocationUpdate = (data) => {
            setLocationData(data);
        };

        function toggleVisibility(methodId, containerId) {
            setOpenDivId((prevOpenDivId) => {
                if (prevOpenDivId === methodId) {
                    document.getElementById(prevOpenDivId).classList.remove('opened');
                    document.getElementById(prevOpenDivId).classList.add('closed');
                    return null;
                } else if (!prevOpenDivId) {
                    document.getElementById(methodId).classList.remove('closed');
                    document.getElementById(methodId).classList.add('opened');
                    return methodId;
                } else {
                    document.getElementById(prevOpenDivId).classList.remove('opened');
                    document.getElementById(prevOpenDivId).classList.add('closed');
                    document.getElementById(methodId).classList.remove('closed');
                    document.getElementById(methodId).classList.add('opened');
                    return methodId;
                }
            });

            setSelectedDiv((prevSelectedDiv) => {
                if (prevSelectedDiv === containerId) {
                    document.getElementById(prevSelectedDiv).classList.remove('selected');
                    document.getElementById(prevSelectedDiv).classList.add('unselected');
                    return null;
                } else if (!prevSelectedDiv) {
                    document.getElementById(containerId).classList.remove('unselected');
                    document.getElementById(containerId).classList.add('selected');
                    return containerId;
                } else {
                    document.getElementById(prevSelectedDiv).classList.remove('selected');
                    document.getElementById(prevSelectedDiv).classList.add('unselected');
                    document.getElementById(containerId).classList.remove('unselected');
                    document.getElementById(containerId).classList.add('selected');
                    return containerId;
                }
            });
        }

        useEffect(() => {
            initPaymentMethods();
        }, [locationData]);

        async function initPaymentMethods() {
            if (locationData) {
                const countryCodeResponse = await getCountryCode(locationData);
                const countryCode = await countryCodeResponse.json();
                const paymentMethodsResponse = await getPaymentMethods(countryCode.country_code);
                const paymentMethodsObject = await paymentMethodsResponse.json();
                let methods = paymentMethodsObject.paymentMethods;

                if (paymentMethodsObject && Array.isArray(methods)) {
                    const configuration = {
                        paymentMethodsResponse: paymentMethodsObject,
                        clientKey,
                        locale: "en_US",
                        environment: "test",
                        showPayButton: true,
                        paymentMethodsConfiguration: {
                            ideal: {
                                showImage: true,
                            },
                            card: {
                                styles: {
                                    base: {
                                        color: "#000000" // CSS color code for white
                                    }
                                },
                                hasHolderName: true,
                                holderNameRequired: true,
                                name: "Credit or debit card",
                                amount: {
                                    value: 1000,
                                    currency: "EUR",
                                },
                            },
                            paypal: {
                                amount: {
                                    value: 1000,
                                    currency: "USD",
                                },
                                environment: "test", // Change this to "live" when you're ready to accept live PayPal payments
                                countryCode: "US", // Only needed for test. This will be automatically retrieved when you are in production.
                                onCancel: (data, component) => {
                                    component.setStatus('ready');
                                },
                            },
                            blik: {
                                environment: "test"
                            }
                        },
                        onSubmit: (state, component) => {
                            if (state.isValid) {
                                // initiatePayment(state.data)
                                handleSubmission(state, component, "/api/v1/adyen/initiate-payment");
                            }
                        },
                        onAdditionalDetails: (state, component) => {
                            handleSubmission(state, component, "/api/submitAdditionalDetails");
                        },
                    };
                    const mountedDivs = methods.map((method, index) => {
                        const divId = `payment-method-${method.type}`;
                        if (methods.length > 0 && index === 0) {
                            setSelectedDiv(`payment-method-container-${method.type}`);
                            setOpenDivId(`payment-method-${method.type}`);
                        }
                        const icon = getIconPath(method.type); // Get the dynamic icon path
                        return (
                            <div key={divId} id={`payment-method-container-${method.type}`}
                                 className={`payment-method ${index === 0 ? 'selected' : 'unselected'}`}>
                                <label
                                    className="radio">
                                    <div className="payment-method-icon">
                                        {icon && <img src={icon} alt={method.name}/>}
                                    </div>
                                    {method.name}
                                    <input
                                        type="radio"
                                        name="payment-method-radio"
                                        checked={divId === openDivId}
                                        onChange={() => toggleVisibility(divId, `payment-method-container-${method.type}`)}
                                    />
                                    <span className="checkmark"></span>
                                </label>
                                <div id={divId} className={`${index === 0 ? 'opened' : 'closed'}`}></div>
                            </div>
                        );
                    });
                    setPaymentMethodsDivs(mountedDivs);

                    // Initialize the checkout for each payment method
                    for (const method of methods) {
                        // const index = methods.indexOf(method);
                        const divId = `payment-method-${method.type}`;
                        const checkout = await AdyenCheckout(configuration);
                        checkout.create(method.type).mount(document.getElementById(divId));
                    }
                }
            }
        }

        async function handleSubmission(state, component, url) {
            try {
                const res = await callServer(url, state.data);
                handleServerResponse(res, component);
            } catch (error) {
                console.error(error);
                alert("Error occurred. Look at console for details");
            }
        }

        async function callServer(url, data) {
            const res = await fetch(process.env.REACT_APP_BACKEND_URL + url, {
                method: "POST",
                body: data ? JSON.stringify(data) : "",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": 'Bearer ' + getToken()
                },
            });

            return await res.json();
        }

        function handleServerResponse(res, component) {
            if (res.action) {
                component.handleAction(res.action);
            } else {
                switch (res.resultCode) {
                    case "Authorised":
                        window.location.href = "/result/success";
                        break;
                    case "Pending":
                    case "Received":
                        window.location.href = "/result/pending";
                        break;
                    case "Refused":
                        window.location.href = "/result/failed";
                        break;
                    default:
                        window.location.href = "/result/error";
                        break;
                }
            }
        }

        return (
            <>
                <CustomerLocation onLocationUpdate={handleLocationUpdate}/>
                <div>
                    <h3>Select your payment method</h3>
                    <div className="payment-container">
                        {paymentMethodsDivs}
                    </div>
                </div>
            </>
        );
    }
;

export default Checkout;
