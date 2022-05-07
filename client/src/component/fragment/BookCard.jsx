import React from 'react';
import {FaStarHalfAlt} from 'react-icons/fa';
import {Book} from '../../model/entity/Book';
import BookService from '../../service/BookService';

class BookCard extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      book: new Book(),
    };

  }

  componentDidMount() {
    this.setState({
      book: this.props.book,
      action: () => window.location = "/book/details?id="+this.props.book.id,
    });
  }

  render() {
    const{book, action} = this.state;

    return(
      <div className="item-box">
        <div className="item-header">
          <div className="flex space-between">
              <div className="item-title flex-center-align flex">
                    <FaStarHalfAlt className="flex-center-align" style={{margin: '0 2px', color: "goldenrod", fontSize:'20px'}}/>
                    <p className="flex-center-align">{book.rating} ({book.rateCount})</p>
              </div>
              <p className="flex-center-align">{book.price} ₺</p>
          </div>
        </div>
        <div className="item-body">
            <div className="image-container flex">
              <img className="book-img flex-center-align" alt='image not found' src={book.imageUrl}/>
            </div>
        </div>
        <div className="item-footer">
          <p className="item-title flex-center-align">{book.title}</p>
        </div>
      </div>
    );
  }
}

export default BookCard;
