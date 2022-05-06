import React from "react";
import '../../common/app.css';
import {FaRegUser} from "react-icons/fa";

export default class Dropdown extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            display: false,
            content: props.content,
            title: props.title,
            id: props.id
        }
    }

    toggleDisplay(e) {
        const{display, id} = this.state;

        if(e.target === document.getElementById(id) || e.target === document.getElementById(id+"Title")) { // can click the title or dropdown container itself
            if(display) this.setState({display: false});
            else this.setState({display: true});
        }
    }

    componentDidMount() {
        window.addEventListener("click", (e) => {
            if(!e.target.className.includes("dropdown")) {
                this.setState({display: false});
            }
        });
    }

    render() {
        const {display, content, title, id} = this.state;

        return(
            <div className="flex dropdown flex-center-align secondary" id={id} onClick={(e) => this.toggleDisplay(e)}>
                <FaRegUser className="mini-icon flex-center-align dropdown-title" id={id + "Title"}/>
                <div className={"dropdown-content-container width-fit-content" + (display ? " display-block" : " display-none")}>
                    <div className="dropdown-option-container">
                        {content}
                    </div>
                </div>
            </div>
        );
    }
}