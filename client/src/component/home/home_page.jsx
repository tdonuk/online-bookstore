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
        const{user} = this.state;

        if(user) {
            this.setState({loading: true});

            this.loadData();
        } else {
            this.setState({
                loading: false,
            });
        }
    }

    loadData() {
        BookService.getBookList().then(response => {
            this.setState({
                bookList: response.data,
                loading: false,
            });
        })
    } 


    render() {
        const{bookList, loading, errorMessage, user} = this.state;

        if(!user) {
            return(
                <div>
                    {loading &&
                    <div className="big-alert">
                        <LoadingScreen/>
                    </div>
                    }
                    <LoginPage redirect="/"/>
                    { errorMessage &&
                    <ModalMessage message={errorMessage} subtitle={"Bir hata oluştu"} id="errorModal" type="error" title="Hata"/>
                    }
                </div>
            );
        }

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
                <h1 className="small-title left-align flex-center-align">Son eklenenler</h1>
                { bookList && 
                <BookShelf bookList={bookList}/>
                }
                </section>
                </div>
                <div className="page-footer">

                </div>
            </div>
        );
    }
}
