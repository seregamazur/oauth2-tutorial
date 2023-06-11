import "./Dropdown.css"
import {removeUserSession} from "../../utils/Common";
import {useNavigate} from 'react-router-dom';


const Dropdown = () => {

    const navigate = useNavigate();

    const handleSignOut = () => {
        console.log('removing auth info')
        removeUserSession();
        navigate('/login');
    };

    return (
        <ul className="dropdown-menu">
            <li>Profile</li>
            <li onClick={handleSignOut}>Sign Out</li>
        </ul>
    );
};

export default Dropdown;