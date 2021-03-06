import React from "react";
import {FaTimes} from "react-icons/fa";

export default class Error extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            message: props.message,
            icon: <FaTimes className="mini-icon icon-error flex-center-align"/>
        }
    }

    render() {
        const{icon, message} = this.state;

        return(
            <div className={"flex alert error"}>
                {icon}
                <p className="flex-center-align">{message}</p>
            </div>
        );
    }
}