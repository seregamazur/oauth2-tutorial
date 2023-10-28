import React, {useEffect, useRef, useState} from 'react';
import './checkout.css';
import CustomerLocation from '../../components/CustomerLocation';
import * as Common from '../../utils/Common';
import {getCountryCode, getToken} from '../../utils/Common';
import AdyenCheckout from "@adyen/adyen-web";
import {getIconPath} from "./paymentMethodIcon";

const clientKey = 'test_QOXTJVU2NBG5RKGDF47XYUZJ2UMYOZS4'

const Checkout = () => {
        // State to store location data
        const [locationData, setLocationData] = useState(null);
        const [traditionalPaymentMethodDivs, setTraditionalPaymentMethodDivs] = useState([]);
        const [instantPaymentMethodDiv, setInstantPaymentMethodDiv] = useState(null);
        const [openDivId, setOpenDivId] = useState(null);
        const [selectedDiv, setSelectedDiv] = useState(null);
        const [radioButtonState, setRadioButtonState] = useState('payment-method__header__radio');
        const [firstRadioSelected, setFirstRadioSelected] = useState(null);
        const instantPaymentMethod = 'googlepay';

        const handleLocationUpdate = (data) => {
            setLocationData(data);
        };

        function isInstantPaymentMethodAvailable(methods) {
            return methods.some((method) => method.type === instantPaymentMethod);
        }

        function toggleVisibility(methodId, methodType) {
            setOpenDivId((prevOpenDivId) => {
                document.getElementById(prevOpenDivId).classList.remove('payment-method__details--opened');
                document.getElementById(prevOpenDivId).classList.add('payment-method__details');
                document.getElementById(methodId).classList.remove('payment-method__details');
                document.getElementById(methodId).classList.add('payment-method__details--opened');
                return methodId;
            });
            setSelectedDiv((prevSelectedDiv) => {
                document.getElementById(prevSelectedDiv).classList.remove('payment-method--selected');
                document.getElementById(prevSelectedDiv).classList.add('payment-method');
                document.getElementById(`payment-method-item-${methodType}`).classList.remove('payment-method');
                document.getElementById(`payment-method-item-${methodType}`).classList.add('payment-method--selected');
                return `payment-method-item-${methodType}`;
            });
            setRadioButtonState((prevState) => {
                document.getElementById(prevState).classList.remove('payment-method__header__radio--selected');
                document.getElementById(`${methodType}-radio`).classList.add('payment-method__header__radio--selected');
                return `${methodType}-radio`;
            });
        }

        useEffect(() => {
            initPaymentMethods();
        }, [locationData]);

        function adyenCheckout(paymentMethodsObject) {
            let config = {
                paymentMethodsResponse: paymentMethodsObject,
                clientKey,
                locale: "en_US",
                environment: "test",
                showPayButton: true,
                paymentMethodsConfiguration: {
                    googlepay: {
                        environment: 'test',
                        buttonSizeMode: 'fill'
                    },
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
                    //add dissapear other methods and this one increasis in size
                    if (state.isValid) {
                        blockNonSelectedMethods(state.data.paymentMethod.type);
                        handleSubmission(state, component, "/api/v1/adyen/initiate-payment");
                    }
                },
                onAdditionalDetails: (state, component) => {
                    handleSubmission(state, component, "/api/submitAdditionalDetails");
                },
            };
            return AdyenCheckout(config);
        }

        async function blockNonSelectedMethods(methodType) {
            if (instantPaymentMethod !== methodType) {
                document.getElementById(`payment-method-${instantPaymentMethod}`).classList.remove('instant-payment-method__details--opened');
                document.getElementById(`payment-method-${instantPaymentMethod}`).classList.add('instant-payment-method__details--blocked');
            }
            const listItems = document.querySelectorAll('li');
            listItems.forEach((li) => {
                if (li.id !== `payment-method-item-${methodType}`)
                    li.classList.add('payment-method--blocked');
            });
        }

        async function getPaymentMethods() {
            const countryCodeResponse = await getCountryCode(locationData);
            const countryCode = await countryCodeResponse.json();
            const paymentMethodsResponse = await Common.getPaymentMethods(countryCode.country_code);
            return await paymentMethodsResponse.json();
        }

        function setFirstPaymentOpened(index, method) {
            if (index === 0) {
                setSelectedDiv(`payment-method-item-${method.type}`);
                setOpenDivId(`payment-method-${method.type}`);
                setRadioButtonState(`${method.type}-radio`);
                setFirstRadioSelected(`${method.type}-radio`);
            }
        }

        function initializedPaymentMethod(method, index) {
            const divId = `payment-method-${method.type}`;

            const icon = getIconPath(method.type);

            return (
                <li key={index} id={`payment-method-item-${method.type}`}
                    onClick={() => {
                        toggleVisibility(divId, method.type);
                    }}
                    className={`payment-method${index === 0 ? '--selected' : ''}`}>
                    <div className={'payment-method__header'}>
                        <button className="payment-method__header__title" role={"radio"} type={"button"}>
                                            <span id={`${method.type}-radio`} aria-hidden={true}
                                                  className={`${index === 0 ? 'payment-method__header__radio payment-method__header__radio--selected' : 'payment-method__header__radio'}`}/>
                            <span className="payment-method__icon">{icon && <img src={icon} alt={method.name}/>}</span>
                            <span className={"payment-method__header__name"}>{method.name}</span>
                        </button>
                    </div>
                    <div id={divId} className={`${index === 0
                        ? 'payment-method__details--opened' : 'payment-method__details'}`}></div>
                </li>
            );
        }

        async function initPaymentMethods() {
            if (locationData) {
                let paymentMethodsObject = await getPaymentMethods();
                let methods = paymentMethodsObject.paymentMethods;
                if (paymentMethodsObject && Array.isArray(methods) && methods.length > 0) {
                    const checkout = await adyenCheckout(paymentMethodsObject);

                    const traditionalPaymentMethodDivs = methods.filter(m => m.type !== instantPaymentMethod)
                        .map((method, index) => {
                            setFirstPaymentOpened(index, method);
                            return initializedPaymentMethod(method, index);
                        });
                    setTraditionalPaymentMethodDivs(traditionalPaymentMethodDivs);

                    if (isInstantPaymentMethodAvailable(methods)) {
                        setInstantPaymentMethodDiv(<div id='payment-method-googlepay' className={'instant-payment-method__details--opened'}/>);
                    }

                    // Initialize the checkout for each payment method
                    for (const method of methods) {
                        const divId = `payment-method-${method.type}`;
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
                <div className={"checkout-payment-form"}>
                    <h3 className={"checkout-payment-form__title"}>Select your payment method</h3>
                    <div className="payment-container">
                        <ul className={"instant-payment-list"}>{instantPaymentMethodDiv}</ul>
                        <div className={"payment-lists-separator"}>or pay with</div>
                        <ul className={"traditional-payment-list"} role={"radiogroup"}
                            required={true}>{traditionalPaymentMethodDivs}</ul>
                    </div>
                </div>
            </>
        );
    }
;

export default Checkout;
