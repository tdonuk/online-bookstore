import React from 'react';
import {FaHeart, FaRegHeart, FaStar} from 'react-icons/fa';
import {BookCardEntity} from '../../model/entity/BookCardEntity';
import LoadingScreen from "../modal/LoadingScreen";
import UserService from "../../service/UserService";

class BookCard extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            user: JSON.parse(localStorage.getItem("user")),
            book: this.props.book,
            compare: this.props.compare,
        };

        this.handleFavourite = this.handleFavourite.bind(this);
    }

    handleFavourite() {
        const {favourite, user, book} = this.state;

        if (!user) {
            window.location = "/login";
        }

        this.setState({
            loading: true,
        });

        if (favourite) {
            UserService.removeFromFavourites(new Array(this.props.book)).then(response => {
                console.log(book.id + " Favorilerden silindi");
                if (this.props.handleFavourite) {
                    this.props.handleFavourite();
                }
                this.setState({
                    favourite: false,
                    loading: false,
                });
            }).catch(err => {
                console.log(err);
                this.setState({
                    loading: false,
                });
            });
        } else {
            UserService.addBookToFavourites(new Array(this.props.book)).then(response => {
                console.log(book.id + " Favorilere eklendi");
                if (this.props.handleFavourite) {
                    this.props.handleFavourite();
                }
                this.setState({
                    favourite: true,
                    loading: false,
                });
            }).catch(err => {
                console.log(err);
                this.setState({
                    loading: false,
                });
            });
        }
    }

    componentDidMount() {
        const{user, book} = this.state;

        if(user) {
            this.setState({
                favourite: user.favourites.map(b => b.id).includes(book.id),
            });
        }
        else {
            this.setState({
               favourite: false,
            });
        }
    }

    render() {
        const {book, compare, favourite, loading} = this.state;

        return (
            <div className="item-box" aria-disabled={loading}>
                { (book.favouriteCount) &&
                <p className="fav-count">{book.favouriteCount + " favori"}</p>
                }
                <div className="item-header">
                    <p className="favourite-button" onClick={this.handleFavourite}>
                        {!favourite &&
                        <FaRegHeart style={{color: 'yellow', margin: "0", fontSize: "30px"}}/>
                        }
                        {favourite &&
                        <FaHeart style={{color: 'yellow', margin: "0", fontSize: "30px"}}/>
                        }
                    </p>
                </div>

                {loading &&
                <LoadingScreen/>
                }

                <a href={book.url}>
                    <img className="book-img" alt='Görüntü bulunamadı' src={book.imgUrl}/>
                </a>

                <div className="item-footer">
                    <p className="item-title flex-center-align">{book.title}</p>
                    <p className="item-author flex-center-align">{book.authors}</p>
                    <div className="flex-center-align item-price space-between">{book.price + " ₺"}
                        {compare && book.lowestPrice &&
                        <p className="center-align lowest-price-icon">
                            <FaStar style={{color: "gold", fontSize: "20px", margin: "0 10px"}}></FaStar>
                        </p>
                        }
                    </div>
                </div>
            </div>
        );
    }

}

export default BookCard;
