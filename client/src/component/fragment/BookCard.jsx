import React from 'react';
import {FaStarHalfAlt} from 'react-icons/fa';
import {Book} from '../../model/entity/Book';
import BookService from '../../service/BookService';

class BookCard extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      book: new Book(),
      compare: this.props.compare,
    };

  }

  componentDidMount() {
    this.setState({
      book: this.props.book,
      action: () => window.location = "/book/details?id="+this.props.book.id,
    });
  }

  render() {
    const{book, compare} = this.state;

    return(
      <div className="item-box">
        <div className="item-header">
          <div className="item-rating">
            <FaStarHalfAlt className="flex-center-align" style={{margin: '0 2px', color: "goldenrod", fontSize:'20px'}}/>
            <p className="flex-center-align">{book.rating} {book.rateCount !== 0 && book.rateCount}</p>
          </div>
          <p className="flex-center-align">{new Date(book.publishDate).toLocaleDateString()}</p>
        </div>
        
        <img className="book-img" onClick={() => window.location = "/book/details?id="+book.id} alt='Görüntü bulunamadı' src={book.imageUrl}/>

        <div className="item-footer">
          <p className="item-title flex-center-align">{book.title}</p>
          <p className="flex-center-align item-price">{book.price} ₺</p>
          { (!compare) &&
            <p className="center-align item-sub-info" onClick={() => window.location = book.url}>{book.source}</p>
          }
          { compare && book.lowestPrice &&
          <p className="center-align item-sub-info">En uygun</p>
          }
        </div>
      </div>
      );
  }
}

export default BookCard;
