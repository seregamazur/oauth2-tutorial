import './checkout.css'
import {useEffect} from "react";
import {initCheckout} from "./adyenImplementation";

function Checkout() {
    useEffect(() => {
        initCheckout(); // Call the initialization function
    }, []);
    return (
        <div id="payment-page">
            <div className="container">
                <div className="payment-container">
                    <div id="payment" className="payment"/>
                </div>
            </div>
        </div>
    );
}

export default Checkout;
