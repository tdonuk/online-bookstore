import axios from 'axios'
import {BehaviorSubject} from 'rxjs';

const URL = "http://localhost:8765/api/user";
const currentUser = new BehaviorSubject(JSON.parse(localStorage.getItem('currentUser')));

class UserService {
  get currentUserValue() {
    return currentUser.value;
  }

  get currentUser() {
    return currentUser.asObservable;
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
    return axios.post(URL + "/logout", {}).then(response => {
      localStorage.removeItem('currentUser');
      currentUser.next(null);
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

}

export default new UserService();
