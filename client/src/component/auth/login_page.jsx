import React from 'react';
import UserService from "../../service/UserService";
import {User} from '../../model/entity/User';
import '../../common/app.css';
import './form_page.css';
import Info from "../fragment/Info";
import Error from "../fragment/Error";
import LoadingScreen from '../modal/LoadingScreen';
import PageHeader from "../fragment/PageHeader";

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
            errorMessage: null,
            redirect: this.props.redirect,
        };
    }

    handleInput(e) {
        const {name, value} = e.target;
        const user = this.state.user;

        user[name] = value;

        this.setState({user: user});
    }

    handleLogin(e) {
        e.preventDefault();

        this.setState({submitted: true});

        let{user} = this.state;

        if(!(user["email"] && user["password"])) {
            return;
        }

        this.setState({loading: true});

        UserService.login(user).then(response => {
            localStorage.setItem("access-token", response.headers["access_token"]);
            localStorage.setItem("refresh-token", response.headers["refresh_token"]);

            user = response.data;

            user.password = "[Protected]";

            localStorage.setItem("user", JSON.stringify(user));

            this.setState({errorMessage: null, loading: false});

            window.location = this.state.redirect;

        }).catch(error => {
            console.log(error);
            this.setState({
                errorMessage: "Giriş yapılamadı",
                loading: false
            })
        });
    }

    render() {
        const{user, submitted, loading, errorMessage, hasError} = this.state;
        const params = new URLSearchParams(window.location.search);

        return(
            <div id="#formBackground" className="container primary-background flex flex-direction-column">
                <PageHeader/>
                {loading &&
                <LoadingScreen/>
                }
                <div className="form-container flex-center-align">
                    <form className="form center-form" onSubmit={this.handleLogin}>
                        <h1 className="big-title primary"><strong>Giriş</strong></h1>
                        <div className="form-control-group">
                            <div className="form-control flex-direction-column">
                                <label>Email</label>
                                <input type="text" name="email" className="input text-input" value={user.email} placeholder="E-posta adresiniz" id="emailField" onChange={(e) => this.handleInput(e)}/>
                                {submitted && !user.email &&
                                <p className="alert error">E-Mail adresinizi giriniz</p>
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
                                <Error message={errorMessage}/>
                            </div>
                            }

                            { params.get("logout") != null &&
                            <Info message={"Sistemden çıkış yaptınız"}/>
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
                    </form>
                </div>
            </div>
        )
    }
}