import axios from "axios";

const URL = "http://localhost:8765/api/search";

class SearchService {

    search(key) {
        console.log("search request sending...");
        if(!key || key === "" || key === " ") throw new Error("Lütfen geçerli bir anahtar kelime giriniz.");

        const headers =  {
            "Authorization": "Bearer " + localStorage.getItem("access-token")
        };

        return axios({
            url: URL + "?key="+key,
            method: "GET",
            withCredentials: false,
            headers: headers,
        });
    }

}

export default new SearchService();