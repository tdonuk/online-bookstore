import React from 'react';
import SearchService from '../../service/SearchService';
import BookShelf from '../fragment/BookShelf';
import PageHeader from '../fragment/PageHeader';
import LoadingScreen from '../modal/LoadingScreen';
import Error from '../fragment/Error';
import { Book } from '../../model/entity/Book';
import BookCard from "../fragment/BookCard";

class SearchPage extends React.Component {
  constructor() {
    super();

    const user = JSON.parse(localStorage.getItem("user"));

    this.state = {
      key: new URLSearchParams(window.location.search).get("key"),
      loading: false,
      user: user,
      searched: false,
    };

  }

  componentDidMount() {
    const{key, searched} = this.state;

    if(!searched) {
        this.setState({
            loading: true,
        });
        SearchService.search(key).then(response => {
            console.log(response.data);
            this.setState({
                data: response.data,
                loading: false,
                searched: true,
            });
        }).catch(err => {
            this.setState({
                errorMessage: err.response.data,
                loading: false,
                searched: true,
            });
        });
    }
  }

  render() {
    const{user, loading, data, key, errorMessage} = this.state;

      const mapper = (books) => books.map((book) =>
          <BookCard key={book.id} book={book} compare={true}
                    favourite={user.favourites.map(book => book.id).includes(book.id)}
                    handleFavourite={this.getLoggedUser}/>
      );

    const map = () => {
        const shelf = data.resultSet.map(result =>
            <div className='shelf-wrapper' key={result.source}>
                <div>
                    <h4 className='shelf-title'>{result.source}</h4>
                    <div className="search-info">
                        <span>{"En düşük: " + parseFloat(result.lowestPrice).toFixed(2) + " ₺"}</span>
                        <span>{"Ortalama: " + parseFloat(result.averagePrice).toFixed(2) + " ₺"}</span>
                        <span>{"En yüksek: " + parseFloat(result.highestPrice).toFixed(2) + " ₺"}</span>
                    </div>
                </div>
                <div>
                    {mapper(result.result)}
                </div>
            </div>
        );
        return shelf;
    }

    return(
        <div className="container primary-background">
            { loading && 
                <LoadingScreen/>
            }

            <PageHeader/>

            <h1 className='center-align'>{"'" + key + "' için arama sonuçları"}</h1>
            { data &&
                map()
            }

            { errorMessage && 
                <Error message={errorMessage}/>
            }
        </div>
    );
  }

}

export default SearchPage;
