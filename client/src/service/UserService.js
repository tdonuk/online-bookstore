import axios from 'axios'

const URL = "http://localhost:8765/api/user";

class UserService {
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
      if(err.response.headers["expired"]) {
        localStorage.clear();
      }
    });
  }

}

export default new UserService();
