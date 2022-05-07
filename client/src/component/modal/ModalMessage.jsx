import React from "react";
import {FaRegWindowClose, FaExclamationCircle, FaExclamationTriangle, FaTimes} from "react-icons/fa";

export default class ModalMessage extends React.Component {
    constructor(props) {
        super(props);

        let icon;

        if(props.type == "info") icon =  <FaExclamationCircle className="mini-icon icon-info flex-center-align"/>
        if(props.type == "warn") icon =  <FaExclamationTriangle className="mini-icon icon-warn flex-center-align"/>
        if(props.type == "error") icon =  <FaTimes className="mini-icon icon-error flex-center-align"/>

        this.state = {
            message: this.props.message,
            type: this.props.type,
            id: this.props.id,
            title: this.props.title,
            subtitle: this.props.subtitle,
            icon: icon
        }
    }

    dismiss(e) {
        if(e.target === document.getElementById(this.state.id) || e.target === document.getElementById("modalDismissButton") || e.target === document.getElementById(this.state.id + "Dismiss")) {
            document.getElementById(this.state.id).remove();
        }
    }

    render() {
        const{message, type, title, id, subtitle, icon} = this.state;

        return (
            <div className="modal modal-background" id={id} onClick={(e) => this.dismiss(e)}>
                <div className="center-container">
                    <div className="modal-header">
                        <h2 className="modal-title">{title}</h2>
                        <FaRegWindowClose className="mini-icon modal-dismiss-button" id={id+"Dismiss"} onChange={(e) => this.dismiss(e)}/>
                    </div>
                    <hr/>
                    <div className="modal-body flex flex-direction-column">
                        {icon &&
                        <div className={"flex alert modal-alert " + type}>
                            {icon}
                            <p className="flex-center-align">{subtitle}</p>
                        </div>
                        }
                        <p className="modal-message flex-center-align">{message}</p>
                    </div>
                    <div className="modal-footer right-align">
                        <button className="standart-button" id="modalDismissButton" onClick={(e) => this.dismiss(e)}>Tamam</button>
                    </div>
                </div>
            </div>
        );
    }
}