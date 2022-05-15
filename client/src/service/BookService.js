import axios from "axios";

const URL = "http://localhost:8765/api/book";

class BookService {

    defaultHeaders() {
        const headers =  {
            "Authorization": "Bearer " + localStorage.getItem("access-token")
        };

        return headers;
    }

    getBookList() {
        return axios({
            url: URL+"/public/last",
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