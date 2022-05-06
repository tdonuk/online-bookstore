import React from "react";
import {FaExclamationTriangle} from "react-icons/fa";

export default class Warn extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            message: props.message,
            icon: <FaExclamationTriangle className="mini-icon icon-warn flex-center-align"/>
        }
    }

    render() {
        const{icon, message} = this.state;

        return(
            <div className={"flex alert warn"}>
                {icon}
                <p className="flex-center-align">{message}</p>
            </div>
        );
    }
}