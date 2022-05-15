import React from 'react';
import { Book } from '../../model/entity/Book';
import BookService from '../../service/BookService';
import Error from '../fragment/Error';
import LoadingScreen from '../modal/LoadingScreen';
import '../../common/app.css';
import './book_details.css';
import PageHeader from '../fragment/PageHeader';

class BookDetails extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      id: new URLSearchParams(window.location.search).get("id"),
      book: new Book(),
      loading: true,
    };
  }

  render() {
    const{book, loading, errorMessage} = this.state;

    if(book.id) {
        const authorList = book.authors.map((author, index) =>
            author.name.firstname + " " + author.name.lastname + (index === book.authors.length-1 ? "" : ", ")
        );

        const tagList = book.tags.map((tag, index) =>
            <p key={index} className="tag-item">
                {tag}
            </p>
        );

        return(
            <div className="container primary-background flex-direction-column scrollable">
                <PageHeader/>

                { loading &&
                    <LoadingScreen/>
                }

                { errorMessage &&
                    <Error message={errorMessage}/>
                }
                <div className='center-image-container'>
                    <img className='center-image link' src={book.imageUrl} onClick={() => window.location = book.url} alt="Görsel bulunamadı"/>
                    <h2>{book.title}</h2>
                </div>
                <div className="center-align">
                    <div className="tag-list flex">
                        {tagList}
                    </div>
                </div>
                <table className='book-details-table flex-center-align'>
                    <tbody>
                        <tr>
                            <th>
                                İsim
                            </th>
                            <th>
                                Fiyat
                            </th>
                        </tr>
                        <tr>
                            <td>
                                {book.title}
                            </td>
                            <td>
                                {book.price+" ₺"}
                            </td>
                        </tr>

                        <tr>
                            <th>
                                Sayfa
                            </th>
                            <th>
                                ISBN
                            </th>
                        </tr>
                        <tr>
                            <td>
                                {book.pageCount}
                            </td>
                            <td>
                                {book.isbn}
                            </td>
                        </tr>

                        <tr>
                            <th>
                                Yazarlar
                            </th>
                            <th>
                                Yayım Tarihi
                            </th>
                        </tr>
                        <tr>
                            <td>
                                {authorList}
                            </td>
                            <td>
                                {new Date(book.publishDate).toLocaleDateString()}
                            </td>
                        </tr>

                        <tr>
                            <th>
                                Puan
                            </th>
                            <th>
                                Oy sayısı
                            </th>
                        </tr>
                        <tr>
                            <td>
                                {book.rating + " / 5"}
                            </td>
                            <td>
                                {book.rateCount}
                            </td>
                        </tr>

                        <tr>
                            <th colSpan={2}>
                                Hakkında
                            </th>
                        </tr>
                        <tr>
                            <td colSpan={2}>
                                {book.description}
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        );
    } else {
        return(
            <div>
                { errorMessage &&
                    <Error message={errorMessage}/>
                }
            </div>
        );
    }
  }

  componentDidMount() {
    const{id} = this.state;

    BookService.getBookDetails(id).then(response => {
        this.setState({
            book: response.data,
            loading: false,
        });
    }).catch(err => {
        this.setState({
            loading: false,
            errorMessage: err.response.data,
        });
    })
  }
}

export default BookDetails;
