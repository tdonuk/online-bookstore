import React from "react";
import UserService from "../../service/UserService";
import {User} from "../../model/entity/User";
import "./form_page.css";
import "../../common/app.css";
import Error from "../fragment/Error";
import PageHeader from "../fragment/PageHeader";

export default class SignupPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            user: new User(),
            submitted: false,
            errorMessage: null,
            loading: false
        };
    }

    componentDidMount() {
        document.body.classList.add("primary-background");
    }

    handleInput(e) {
        const {name, value} = e.target
        const user = this.state.user;

        user[name] = value;

        this.setState({user: user});
    }

    handleSubmit() {
        this.setState({submitted: true});

        let{user, errorMessage} = this.state;

        if(!(user.firstname && user.lastname && user.email && user.password && user.username)) {
           return;
        }

        if(user.password != user.repeatPassword) {
            return;
        }

        user.name = {
            firstname: user.firstname,
            lastname: user.lastname
        }

        user.primaryPhone = {
            title: user.phoneTitle,
            number: user.phoneNumber
        }

        this.setState({loading: true});

        console.info(user);

        UserService.register(user).then(response => {
            console.log(response.data);
            this.setState({loading: false});
            window.location = "/login?signup";
        }).catch(error => {
            this.setState({loading: false, errorMessage: error.response.data});
        });
    }


    render() {
        const{user, loading, submitted, errorMessage} = this.state;

        return (
            <div className="container primary-background flex flex-direction-column">
                <PageHeader/>
                <div className="form-container flex-center-align">
                    <div className="form center-form">
                        <h1 className="big-title primary">Üyelik</h1>

                        <table>
                            <tbody>
                                <tr>
                                    <td>
                                        <h2>Kimlik</h2>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div className="form control flex-direction-column">
                                            <label htmlFor="firstnameField">İsim <p className="sign">*</p></label>
                                            <input minLength={1} maxLength={20} type="text" name="firstname" value={user.firstname} onChange={(e) => this.handleInput(e)} id="firstnameField" placeholder="Adınız"></input>

                                        </div>
                                    </td>
                                    <td>
                                        <div className="form control flex-direction-column">
                                            <label htmlFor="lastnameField">Soyisim <p className="sign">*</p></label>
                                            <input minLength={1} maxLength={20} type="text" name="lastname" value={user.lastname} onChange={(e) => this.handleInput(e)} id="lastnameField" placeholder="Soyadınız"></input>

                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        { (submitted && !user.firstname) &&
                                        <p className="alert error">İsim gereklidir</p>
                                        }
                                    </td>
                                    <td>
                                        { (submitted && !user.lastname) &&
                                        <p className="alert error">Soyisim gereklidir</p>
                                        }
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        <div className="form control flex-direction-column">
                                            <label htmlFor="emailField">Email <p className="sign">*</p></label>
                                            <input type="email" value={user.email} name="email" onChange={(e) => this.handleInput(e)} id="emailField" placeholder="E-Mail adresiniz"></input>
                                        </div>
                                    </td>
                                    <td>
                                        <div className="form control flex-direction-column">
                                            <label htmlFor="usernameField">Kullanıcı adı <p className="sign">*</p></label>
                                            <input minLength={5} maxLength={12} type="text" value={user.username} name="username" onChange={(e) => this.handleInput(e)} id="usernameField" placeholder="Kullanıcı adınız"></input>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        { submitted && !user.email &&
                                        <p className="alert error">E-posta gereklidir</p>
                                        }
                                    </td>
                                    <td>
                                        { submitted && !user.username &&
                                        <p className="alert error">Kullanıcı adı gereklidir</p>
                                        }
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        <h2>Parola</h2>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div className="form control flex-direction-column">
                                            <label htmlFor="passwordField">Şifre <p className="sign">*</p></label>
                                            <input minLength={4} maxLength={16} type="password" value={user.password} name="password" onChange={(e) => this.handleInput(e)} id="passwordField" placeholder="Parolanız"></input>
                                        </div>
                                    </td>
                                    <td>
                                        <div className="form control flex-direction-column">
                                            <label htmlFor="passwordRepeat">Şifre (tekrar) <p className="sign">*</p></label>
                                            <input minLength={4} maxLength={16} type="password" value={user.repeatPassword} name="repeatPassword" onChange={(e) => this.handleInput(e)} id="passwordRepeat" placeholder="Parolanız"></input>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        { submitted && !user.password &&
                                        <p className="alert error">Şifre gereklidir</p>
                                        }
                                    </td>
                                    <td>
                                        { (user.repeatPassword != null && user.repeatPassword != "" && user.password != user.repeatPassword) &&
                                        <p className="alert error">Şifreniz ile uyuşmamaktadır</p>
                                        }
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        <h2>Telefon</h2>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div className="form control flex-direction-column">
                                            <label htmlFor="phoneTitleField">İsim</label>
                                            <input type="text" value={user.phoneTitle} name="phoneTitle" onChange={(e) => this.handleInput(e)} id="phoneTitleField" placeholder="örn: cep"></input>
                                        </div>
                                    </td>
                                    <td>
                                        <div className="form control flex-direction-column">
                                            <label htmlFor="phoneNumberField">Numara</label>
                                            <input type="text" value={user.phoneNumber} name="phoneNumber" onChange={(e) => this.handleInput(e)} id="phoneNumberField" placeholder="5xx xxx xx xx"></input>
                                        </div>
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        <h2>Yaş</h2>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div className="form control flex-direction-column">
                                            <label htmlFor="birthDateField">Doğum tarihi</label>
                                            <input type="date" value={user.birthDate} name="birthDate" onChange={(e) => this.handleInput(e)} id="birthDateField" placeholder="Seçiniz.."></input>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>

                        <h5 className="note"> <p className="sign">*</p> ile işaretlenmiş alanların doldurulması zorunludur</h5>
                        <hr/>

                        { submitted && errorMessage &&
                        <Error message={errorMessage}/>
                        }

                        <div className="form control flex-direction-column">
                            <button disabled={loading} onClick={(e) => this.handleSubmit(e)} className="action-element">Gönder</button>
                        </div>
                        <div className="form control flex-direction-column">
                            <a className="link standard-link" href="/login">Zaten bir hesabınız var mı? giriş yapın</a>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}