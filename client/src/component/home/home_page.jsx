import React from "react";
import '../../common/app.css';
import './home_page.css';
import {User} from "../../model/entity/User";
import {Book} from "../../model/entity/Book";
import PageHeader from "../fragment/PageHeader";
import BookService from "../../service/BookService";
import Info from "../fragment/Info";
import {FaStarHalfAlt} from "react-icons/fa";

export default class HomePage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            accessToken: localStorage.getItem("access-token"),
            refreshToken: localStorage.getItem("refresh-token"),
            loading: true,
        }
    }

    componentDidMount() {
        this.setState({loading: true});

        BookService.getBookList().then(response => {
            console.log(response.data);
            this.setState({
                bookList: response.data,
                loading: false,
            });
        }).catch(err => {
            console.log(err);
        });

    }

    render() {
        const{bookList, loading} = this.state;
        let list;
        if(bookList) {
            console.log("booklist vra: "+JSON.stringify(bookList));
            list = bookList.map((book) => 
            <div className="item-box" onClick={function(){window.location = book.url}}>
                <div className="item-header primary">
                    <div className="flex space-between">
                        <div className="item-title flex-center-align flex">
                            <FaStarHalfAlt className="flex-center-align" style={{margin: '0 2px', color: "goldenrod", fontSize:'20px'}}/>
                            <p className="flex-center-align">{book.rating} ({book.rateCount})</p>
                        </div>
                        <p className="flex-center-align">{book.price}₺</p>
                    </div>
                </div>
                <img className="book-img flex-center-align" src={book.imageUrl}/>
                <div className="item-box-body">
                    <p className="item-title flex-center-align">{book.title}</p>
                </div>
            </div>
            );
        }

        return (
            <div className="container secondary-background">
                {<PageHeader/>}
                <div className="page-body">
                <h1 className="big-title left-align primary">Son eklenenler</h1>
                { loading &&
                    <div className="big-alert">
                        <Info message="Loading..."/>
                    </div>
                }
                { bookList &&
                    <div>
                        {list}
                    </div>
                }
                </div>
                <div className="page-footer">

                </div>
            </div>
        );
    }
}