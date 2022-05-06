import React from "react";
import {FaWindowClose} from "react-icons/fa";

export default class error extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            message: props.message,
            icon: <FaWindowClose className="mini-icon icon-error flex-center-align"/>
        }
    }

    render() {
        const{icon, message} = this.state;

        return(
            <div className={"flex alert modal-alert error"}>
                {icon}
                <p>{message}</p>
            </div>
        );
    }
}