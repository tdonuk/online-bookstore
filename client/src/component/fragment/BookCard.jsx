import React from 'react';
import {FaStarHalfAlt} from 'react-icons/fa';
import {BookCardEntity} from '../../model/entity/BookCardEntity';

class BookCard extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      book: new BookCardEntity(),
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
        { compare && book.lowestPrice &&
          <p className="center-align item-sub-info">En uygun</p>
        }
        </div>
        
        <a href={book.url}>
          <img className="book-img" alt='Görüntü bulunamadı' src={book.imgUrl}/>
        </a>

        <div className="item-footer">
          <p className="item-title flex-center-align">{book.title}</p>
          <p className="item-author flex-center-align">{book.authors}</p>
          <p className="flex-center-align item-price">{book.price} ₺</p>
          { (!compare) &&
            <p className="center-align item-sub-info" onClick={() => window.location = book.url}>{book.source}</p>
          }
        </div>
      </div>
      );
  }
}

export default BookCard;
