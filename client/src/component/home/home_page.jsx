import React from "react";
import '../../common/app.css';
import './home_page.css';
import PageHeader from "../fragment/PageHeader";
import BookService from "../../service/BookService";
import BookCard from "../fragment/BookCard";
import LoadingScreen from "../modal/LoadingScreen";
import ModalMessage from "../modal/ModalMessage";
import UserService from "../../service/UserService";
import LoginPage from "../auth/login_page";
import BookShelf from "../fragment/BookShelf";

export default class HomePage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            loading: true,
            errorMessage: null,
            user: JSON.parse(localStorage.getItem("user")),
        }

        this.loadData = this.loadData.bind(this);
    }

    componentDidMount() {
        this.setState({loading: true});

        this.loadData();
    }

    loadData() {
        BookService.getBookList().then(response => {
            console.log(response.data);
            this.setState({
                bookList: response.data.content,
                loading: false,
            });
        })
    } 


    render() {
        const{bookList, loading, errorMessage} = this.state;

        const books = () => bookList.map((book) =>
            <BookCard key={book.isbn} book={book} compare={false}></BookCard>
        );

        return (
            <div className="container secondary-background">
                {<PageHeader/>}

                { errorMessage &&
                    <ModalMessage message={errorMessage} subtitle={"Bir hata oluştu"} id="errorModal" type="error" title="Hata"/>
                }

                <div className="page-body">
                { loading &&
                    <div className="big-alert">
                        <LoadingScreen/>
                    </div>
                }
                <section>
                { bookList && 
                <div className="book-shelf-container">
                    <div>
                        <h1 className="shelf-title left-align flex-center-align">Son arananlar</h1>
                    </div>
                    <div className="horizontal-shelf">
                        {books()}
                    </div>
                </div>
                }
                </section>
                </div>
                <div className="page-footer">

                </div>
            </div>
        );
    }
}
