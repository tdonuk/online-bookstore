import React, {Component} from 'react';
import PageHeader from "../fragment/PageHeader";
import UserService from "../../service/UserService";
import LoadingScreen from "../modal/LoadingScreen";
import ModalMessage from "../modal/ModalMessage";
import BookCard from "../fragment/BookCard";

class ProfilePage extends Component {
    constructor(props) {
        super(props);

        this.state = {
            user: JSON.parse(localStorage.getItem("user")),
        }

        this.getLoggedUser = this.getLoggedUser.bind(this);
    }

    getLoggedUser() {
        UserService.getLoggedUser().then((a) => {
            this.setState({
                loading: false,
                user: a,
            });
        }).catch(err => {
            this.setState({
                loading: false,
                errorMessage: "Bir hata oluştu, hata kodu: " + err.response.status,
            });
        });
    }

    componentDidMount() {
        this.setState({
            loading: true,
        });

        this.getLoggedUser();
    }

    render() {
        const {loading, errorMessage, user} = this.state;

        const mapper = (books) => books.map((book) =>
            <BookCard key={book.id} book={book} compare={false}
                      favourite={user.favourites.map(book => book.id).includes(book.id)}
                      handleFavourite={this.getLoggedUser}/>
        );

        return (
            <div className="container primary-background">
                <PageHeader/>

                {loading &&
                <LoadingScreen/>
                }

                {errorMessage &&
                <ModalMessage message={errorMessage}
                              title="Hata" subtitle="Bir hata oluştu" type={"error"} id={"errorModal"}/>
                }

                <div>
                    <h2 className="page-title">Profiliniz</h2>
                    <div className="user-info-container">
                        <h3>İsim</h3>
                        <span>{user.name.firstname + " " + user.name.lastname}</span>
                    </div>
                    <div className="user-info-container">
                        <h3>Kullanıcı Adı</h3>
                        <span>{user.username}</span>
                    </div>
                    <div className="user-info-container">
                        <h3>E-Posta</h3>
                        <span>{user.email}</span>
                    </div>
                    <div className="user-info-container">
                        <h3>Hesap Tarihi</h3>
                        <span>{new Date(user.accountCreationDate).toLocaleDateString()}</span>
                        <span>{" (" + (Math.abs(new Date() - new Date(user.accountCreationDate)) / (1000 * 60 * 60 * 24)).toFixed(2) + " gün)"}</span>
                    </div>
                    {user.birthDate &&
                    <div className="user-info-container">
                        <h3>Doğum Tarihi</h3>
                        <span>{new Date(user.birthDate).toLocaleDateString()}</span>
                    </div>
                    }
                    {user.primaryPhone &&
                    <div className="user-info-container">
                        <h3>Telefon</h3>
                        <span>{user.primaryPhone.number}</span>
                    </div>
                    }

                    <div>
                        <h1 className="shelf-title left-align flex-center-align">{"Favorileriniz " + "(" + user.favourites.length + " toplam)"}</h1>
                    </div>
                    <div className="horizontal-shelf">
                        {mapper(user.favourites)}
                    </div>

                    <div>
                        <h1 className="shelf-title left-align flex-center-align">{"Son aramalarınız " + "(" + user.searches.length + " toplam)"}</h1>
                    </div>
                    <div className="horizontal-shelf">
                        {mapper(user.searches)}
                    </div>

                </div>

            </div>
        );
    }

}

export default ProfilePage;
