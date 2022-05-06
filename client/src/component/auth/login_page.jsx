import React from 'react';
import UserService from "../../service/UserService";
import {User} from '../../model/entity/User';
import '../../common/app.css';
import './form_page.css';
import ModalMessage from "../modal/modal_message";
import Info from "../fragment/Info";

export default class LoginPage extends React.Component {
    constructor(props) {
        super(props);

        if(localStorage.getItem("access-token")) {
            // TODO
        }

        this.state = {
            user: new User(),
            submitted: false,
            loading: false,
            errorMessage: null
        };
    }

    componentDidMount() {
        document.body.classList.add("primary-background");
    }

    handleInput(e) {
        const {name, value} = e.target;
        const user = this.state.user;

        user[name] = value;

        this.setState({user: user});
    }

    handleLogin(e) {
        this.setState({submitted: true});

        let{user} = this.state;

        if(!(user["email"] && user["password"])) {
            return;
        }

        this.setState({loading: true});

        UserService.login(user).then(response => {
            localStorage.setItem("access-token", response.headers["access_token"]);
            localStorage.setItem("refresh-token", response.headers["access_token"]);
            localStorage.setItem("user", JSON.stringify(response.data));

            user = response.data;

            console.info("Giriş başarılı: "+user.name.firstname + " " + user.name.lastname + "\nYetki: "+user.role);

            this.setState({errorMessage: null, loading: false});
            window.location = "/home";
        }).catch(error => {
            this.setState({
                errorMessage: error.response.data,
                loading: false
            })
        });
    }

    render() {
        const{user, submitted, loading, errorMessage, hasError} = this.state;
        const params = new URLSearchParams(window.location.search);

        return(
            <div className="container">
                <div className="container form-container">
                    <div className="form center-form">
                        <h1 className="big-title primary"><strong>Giriş</strong></h1>
                        <div className="form-control-group">
                            <div className="form-control flex-direction-column">
                                <label>Email</label>
                                <input type="text" name="email" className="input text-input" value={user.email} placeholder="E-posta adresiniz" id="emailField" onChange={(e) => this.handleInput(e)}/>
                                {submitted && !user.email &&
                                <p className="alert error">E-posta adresinizi giriniz</p>
                                }
                            </div>
                            <div className="form-control flex-direction-column">
                                <label>Şifre</label>
                                <input type="password" name="password" className="input text-input" value={user.password} placeholder="Şifreniz" id="passwordField" onChange={(e) => this.handleInput(e)}/>
                                {submitted && !user.password &&
                                <p className="alert error">Şifrenizi giriniz</p>
                                }
                            </div>
                            { errorMessage &&
                            <div className="form-control flex-direction-column">
                                <p className="alert error">{errorMessage}</p>
                            </div>
                            }

                            { params.get("logout") != null &&
                            <Info message={"Başarıyla çıkış yaptınız"}/>
                            }
                            { params.get("signup") != null &&
                            <Info message={"Başarıyla hesap oluşturdunuz"}/>
                            }

                            <div className="form-control flex-direction-column">
                                <button disabled={loading} onClick={(e) => this.handleLogin(e)} className="action-element">Giriş</button>
                            </div>
                            <div className="form-control flex-direction-column flex-center-align">
                                <a className="link standard-link" href="/signup">Yeni bir hesap oluşturun</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}