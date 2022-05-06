import React from "react";
import {FaExclamationCircle} from "react-icons/fa";

export default class Info extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            message: props.message,
            icon: <FaExclamationCircle className="mini-icon icon-info flex-center-align"/>
        }
    }

    render() {
        const{icon, message} = this.state;

        return(
            <div className={"flex alert info flex-center-align"}>
                {icon}
                <p className="flex-center-align">{message}</p>
            </div>
        );
    }
}