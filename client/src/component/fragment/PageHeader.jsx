import React from "react";
import Dropdown from "./Dropdown";
import {FaRegQuestionCircle, FaRegUserCircle, FaCogs, FaRegArrowAltCircleLeft} from "react-icons/fa";

export default class PageHeader extends React.Component {
    constructor(props) {
        super(props);

        const userMenuContent = [
            {
                text: "Profil",
                href: "/profile",
                key: "profile",
                icon: <FaRegUserCircle className="mini-icon flex-center-align"/>
            },
            {
                text: "Ayarlar",
                href: "/settings",
                key: "settings",
                icon: <FaCogs className="mini-icon flex-center-align"/>
            },
            {
                text: "Çıkış",
                href: "/login?logout",
                key: "logout",
                icon: <FaRegArrowAltCircleLeft className="mini-icon flex-center-align"/>
            },
            {
                text: "Hakkında",
                href: "/about",
                key: "about",
                icon: <FaRegQuestionCircle className="mini-icon flex-center-align"/>
            }
        ];

        const contents = userMenuContent.map((obj) =>
            <div key={obj.key} className="dropdown-option flex space-between" onClick={() => window.location=obj.href}>
                <p href={obj.href} className="dropdown-option-text">{obj.text}</p>
                {obj.icon}
            </div>
        );

        this.state = {
            userMenuContent:  contents
        };
    }

    render() {
        const user = JSON.parse(localStorage.getItem("user"));
        const{userMenuContent} = this.state

        return(
            <div className="page-header flex space-between">
                <h2 onClick={() => window.location="/"} className="big-title secondary">Bookstore</h2>
                <div className="flex flex-center-align space-between" id="searchBar">
                    <input placeholder="Kitap ismi.." className="flex-center-align" type="text" id="search"/>
                    <button className="standart-button width-fit-content flex-center-align no-margin">Ara</button>
                    {<Dropdown title={"Ben"} id="userMenu" content={userMenuContent}/>}
                </div>
            </div>
        );
    }

}