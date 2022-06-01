import React from "react";
import '../../common/app.css';
import './home_page.css';
import PageHeader from "../fragment/PageHeader";
import BookService from "../../service/BookService";
import LoadingScreen from "../modal/LoadingScreen";
import ModalMessage from "../modal/ModalMessage";
import BookCard from "../fragment/BookCard";
import UserService from "../../service/UserService";

export default class HomePage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            loading: true,
            errorMessage: null,
            user: JSON.parse(localStorage.getItem("user")),
        }

        this.handleFavourite = this.handleFavourite.bind(this);
    }

    componentDidMount() {
        const {user} = this.state;

        this.setState({loading: true});

        if (user) {
            UserService.getLoggedUser()
            {
                this.setState({
                    user: JSON.parse(localStorage.getItem("user")),
                });
            }
        }

        this.loadData();
    }

    loadData() {
        BookService.getLastSearches().then(response => {
            this.setState({
                lastSearched: response.data.content,
                loading: false,
            });
        }).catch(err => {
            this.setState({
                loading: false,
                errorMessage: "Bir hata oluştu. Lütfen daha sonra tekrar deneyiniz",
            })
        });

        BookService.getMostLiked().then(response => {
            this.setState({
                mostLiked: response.data.content,
                loading: false,
            });
        }).catch(err => {
            this.setState({
                loading: false,
                errorMessage: "Bir hata oluştu. Lütfen daha sonra tekrar deneyiniz",
            })
        });
    }

    handleFavourite() {
        UserService.getLoggedUser().then(response => {
            this.loadData();
            this.setState({
                user: response,
            });
        });
    }


    render() {
        const {lastSearched, mostLiked, loading, errorMessage, user} = this.state;

        if (lastSearched) lastSearched.sort(() => Math.random() - 0.5);
        if (mostLiked) mostLiked.sort(() => (Math.random() > 0.5) ? 1 : -1);

        const books = (bookList) => {
            return (
                bookList.map((book) => {
                        if (!user) {
                            return (
                                <BookCard key={book.id} book={book} compare={false}/>
                            );
                        } else {
                            return (
                                <BookCard key={book.id} book={book} compare={false}
                                          favourite={false} favCount={book.favoruiteCount} handleFavourite={this.handleFavourite}></BookCard>
                            );
                        }
                    }
                )
            );
        }

        return (
            <div className="container primary-background">
                {<PageHeader/>}

                {errorMessage &&
                <ModalMessage message={errorMessage} subtitle={"Bir hata oluştu"} id="errorModal" type="error"
                              title="Hata"/>
                }

                <div className="page-body">
                    {loading &&
                    <div className="big-alert">
                        <LoadingScreen/>
                    </div>
                    }
                    {lastSearched &&
                    <div className="book-shelf-container">
                        <div>
                            <h1 className="shelf-title left-align flex-center-align">Son arananlar</h1>
                        </div>
                        <div className="horizontal-shelf">
                            {books(lastSearched)}
                        </div>
                    </div>
                    }
                    {mostLiked &&
                    <div className="book-shelf-container">
                        <div>
                            <h1 className="shelf-title left-align flex-center-align">En sevilenler</h1>
                        </div>
                        <div className="horizontal-shelf">
                            {books(mostLiked)}
                        </div>
                    </div>
                    }
                </div>
                <div className="page-footer">

                </div>
            </div>
        );
    }
}
