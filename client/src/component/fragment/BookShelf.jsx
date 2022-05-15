import React from 'react';
import BookCard from './BookCard';

class BookShelf extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      loading: true,
      bookList: this.props.bookList,
      compare: this.props.compare,
    };
  }

  componentDidMount() {
    this.setState({
      loading: false,
      bookList: this.props.bookList,
    })
  }

  render() {
    const{bookList, compare} = this.state;

    let list;
    list = bookList.map((book) =>
      <BookCard key={book.isbn} book={book} compare={compare}></BookCard>
    );
    return(
      <div>
        {list}
      </div>
    );

  }
}

export default BookShelf;
