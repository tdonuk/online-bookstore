import axios from "axios";

const URL = "http://localhost:8765/api/book";

class BookService {
    getBookList() {
        const headers =  {
            "Authorization": "Bearer " + localStorage.getItem("access-token")
        };
        
        return axios({
            url: URL+"/last",
            method:"GET",
            headers: headers,
            withCredentials: false,
        });
    }
}

export default new BookService();