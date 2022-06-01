import axios from 'axios'

const URL = "http://localhost:8765/api/user";

class UserService {
    defaultHeaders() {
        const headers = {
            "Content-Type": "application/json; charset=UTF-8",
            "Authorization": "Bearer " + localStorage.getItem("access-token"),
        }

        return headers;
    }

    login(user) {
        const headers = {
            "Content-Type": "application/json; charset=UTF-8"
        }

        return axios({
            url: URL + "/login",
            method: "POST",
            withCredentials: false,
            data: JSON.stringify(user),
            headers: headers
        });
    }

    logout() {
        localStorage.clear();
        window.location = "/login?logout";
    }

    getLoggedUser() {
        return axios({
            method: "GET",
            url: URL + "/currentUser",
            headers: this.defaultHeaders(),
        }).then(response => {
            localStorage.setItem("user", JSON.stringify(response.data));
            return response.data;
        }).catch(err => {
            this.logout();
            throw new Error(err);
        });
    }

    register(user) {
        const headers = {
            "Content-Type": "application/json; charset=UTF-8"
        }

        return axios({
            url: URL + "/register",
            method: "PUT",
            headers: headers,
            withCredentials: false,
            data: JSON.stringify(user)
        });
    }

    getFavourites() {
        return axios({
            url: URL + "/favourites",
            method: "GET",
            headers: this.defaultHeaders(),
            withCredentials: false,
        });
    }

    getHistory() {
        return axios({
            url: URL + "/history",
            method: "GET",
            headers: this.defaultHeaders(),
            withCredentials: false,
        });
    }


    refreshToken() {
        const headers = {
            "Authorization": "Bearer " + localStorage.getItem("refresh-token"),
        }

        axios({
            url: URL + "/token/refresh",
            headers: headers,
            method: "GET",
            withCredentials: false
        }).then(response => {
            console.log("updating access token..");
            localStorage.setItem("access-token", response.data);
        }).catch(err => {
            if (err.response.headers["expired"]) {
                localStorage.clear();
            }
        });
    }

    addBookToFavourites(books) {
        return axios({
            url: URL + "/detail/favourites/add",
            headers: this.defaultHeaders(),
            method: "POST",
            data: JSON.stringify(books),
        });
    }

    removeFromFavourites(books) {
        return axios({
            url: URL + "/detail/favourites/remove",
            headers: this.defaultHeaders(),
            method: "POST",
            data: JSON.stringify(books),
        });
    }

}

export default new UserService();
