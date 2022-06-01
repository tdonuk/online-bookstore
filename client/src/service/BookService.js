import axios from "axios";

const URL = "http://192.168.1.26:8765/api/book";

class BookService {

    defaultHeaders() {
        const headers =  {
            "Authorization": "Bearer " + localStorage.getItem("access-token")
        };

        return headers;
    }

    getLastSearches() {
        return axios({
            url: URL+"/public/search/last",
            method:"GET",
            headers: this.defaultHeaders(),
            withCredentials: false,
        });
    }

    getMostLiked() {
        return axios({
            url: URL+"/public/search/mostLiked",
            method:"GET",
            headers: this.defaultHeaders(),
            withCredentials: false,
        });
    }

    getBookDetails(id) {

        if(id) {
            return axios({
                url: URL + "/public/" + id + "/details",
                method: "GET",
                headers: this.defaultHeaders(),
                withCredentials: false,
            });
        }
    }
}

export default new BookService();