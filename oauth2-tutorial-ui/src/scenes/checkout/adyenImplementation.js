import {useEffect} from "react";
import {getPaymentMethods} from "../../utils/Common";
import AdyenCheckout from "@adyen/adyen-web";

const clientKey = '';
const type = 'dropin';

function Checkout() {

    useEffect(() => {
        initCheckout();
    }, []);

    async function initCheckout() {
        try {
            const paymentMethodsResponse = await getPaymentMethods();
            const configuration = {
                paymentMethodsResponse: paymentMethodsResponse,
                clientKey,
                locale: "en_US",
                environment: "test",
                showPayButton: true,
                paymentMethodsConfiguration: {
                    ideal: {
                        showImage: true,
                    },
                    card: {
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
                    }
                },
                onSubmit: (state, component) => {
                    if (state.isValid) {
                        handleSubmission(state, component, "/api/v1/adyen/initiate-payment");
                    }
                },
                onAdditionalDetails: (state, component) => {
                    handleSubmission(state, component, "/api/v1/adyen/submitAdditionalDetails");
                },
            };
            // `spring.jackson.default-property-inclusion=non_null` needs to set in
            // src/main/resources/application.properties to avoid NPE here
            const checkout = await AdyenCheckout(configuration);
            checkout.create(type).mount(document.getElementById("payment"));
        } catch (error) {
            console.error(error);
            alert("Error occurred. Look at console for details");
        }
    }

// Event handlers called when the shopper selects the pay button,
// or when additional information is required to complete the payment
    async function handleSubmission(state, component, url) {
        try {
            const res = await callServer(url, state.data);
            handleServerResponse(res, component);
        } catch (error) {
            console.error(error);
            alert("Error occurred. Look at console for details");
        }
    }

// Calls your server endpoints
    async function callServer(url, data) {
        const res = await fetch(url, {
            method: "POST",
            body: data ? JSON.stringify(data) : "",
            headers: {
                "Content-Type": "application/json",
            },
        });

        return await res.json();
    }

// Handles responses sent from your server to the client
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
        <div className="container">
            <div className="payment-container">
                <div id="payment" className="payment"></div>
            </div>
        </div>);
}

export default Checkout;
