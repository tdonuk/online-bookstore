import React from "react";
import '../../common/app.css';
import './home_page.css';
import {User} from "../../model/entity/User";
import PageHeader from "../fragment/PageHeader";

export default class HomePage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            user: new User(),
            accessToken: localStorage.getItem("access-token"),
            refreshToken: localStorage.getItem("refresh-token")
        }
    }

    componentDidMount() {
        this.setState({user: JSON.parse(localStorage.getItem("user"))});
    }

    render() {
        const {user} = this.state;

        return (
            <div className="container">
                {<PageHeader/>}
                <div className="page-body">
                    <label>İsim: </label>{user.name.firstname + " " + user.name.lastname} <br/> <br/>
                    <label>Kullanıcı adı: </label> {user.username} <br/> <br/>
                    <label>Email: </label> {user.email} <br/> <br/>
                    <label>Hesap açılış: </label> {new Date(user.accountCreationDate).toLocaleString()} <br/> <br/>
                </div>
                <div className="page-footer">
                    footer text
                </div>
            </div>
        );
    }
}