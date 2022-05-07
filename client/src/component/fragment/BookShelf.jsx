import React from 'react';
import BookCard from './BookCard';

class BookShelf extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      loading: true,
      bookList: this.props.bookList,
    };
  }

  componentDidMount() {
    this.setState({
      loading: false,
      bookList: this.props.bookList,
    })
  }

  render() {
    const{bookList} = this.state;

    let list;
    list = bookList.map((book) =>
      <BookCard key={book.id} book={book}></BookCard>
    );
    return(
      <div className='card-container flex'>
          {list}
      </div>
    );

  }
}

export default BookShelf;
