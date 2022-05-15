import React from "react";
import Dropdown from "./Dropdown";
import {FaRegQuestionCircle, FaRegUserCircle, FaCogs, FaRegArrowAltCircleLeft} from "react-icons/fa";

export default class PageHeader extends React.Component {
    constructor(props) {
        super(props);

        const user = JSON.parse(localStorage.getItem("user"));

        if(user) { // authenticated
            const userMenuContent = [
                {
                    text: "Profil",
                    action: () => window.location = "/profile",
                    key: "profile",
                    icon: <FaRegUserCircle className="mini-icon flex-center-align"/>
                },
                {
                    text: "Ayarlar",
                    action: () => window.location = "/settings",
                    key: "settings",
                    icon: <FaCogs className="mini-icon flex-center-align"/>
                },
                {
                    text: "Çıkış",
                    action: () => {
                        localStorage.clear();
                        window.location = "/login?logout"
                    },
                    key: "logout",
                    icon: <FaRegArrowAltCircleLeft className="mini-icon flex-center-align"/>
                },
                {
                    text: "Hakkında",
                    action: () => window.location = "/about",
                    key: "about",
                    icon: <FaRegQuestionCircle className="mini-icon flex-center-align"/>
                }
            ];
    
            const contents = userMenuContent.map((obj) =>
                <div key={obj.key} className="dropdown-option flex space-between" onClick={obj.action}>
                    <p href={obj.href} className="dropdown-option-text">{obj.text}</p>
                    {obj.icon}
                </div>
            );
    
            this.state = {
                userMenuContent:  contents,
                user: JSON.parse(localStorage.getItem("user")),
            };
        }

        else this.state = {};

    }

    render() {
        const{userMenuContent, user} = this.state;

        return(
            <div className="page-header flex space-between">
                <h2 onClick={() => window.location="/"} className="page-title secondary">Bookstore</h2>
                <div className="flex flex-center-align space-between">
                    <form className="flex" id="searchBar" action="/search">
                        <input maxLength={20} minLength={2} placeholder="Kitap ismi.." className="flex-center-align search" type="text" name="key" id="search"/>
                        <button type="submit" className="standart-button width-fit-content flex-center-align no-margin">Ara</button>
                    </form>
                </div>
                { user &&
                    <Dropdown title={"Ben"} id="userMenu" content={userMenuContent}/>
                }
                { (!user) &&
                    <div className="strong-nav flex flex-end-align">
                        <a className="secondary nav-item flex-end-align" href="/login">Giriş yap</a>
                        <a className="secondary nav-item flex-end-align" href="/signup">Üye ol</a>
                    </div>
                }
            </div>
        );
    }

}