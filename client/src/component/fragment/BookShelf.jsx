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

    const mapper = () => bookList.map((book, index) => {
      <BookCard key={index} book={book} compare={compare}/>
    });

    return(
      <div>
        { bookList && mapper()}
      </div>
    );

  }
}

export default BookShelf;
