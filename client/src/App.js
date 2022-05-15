import './App.css';
import LoginPage from "./component/auth/login_page";
import SignupPage from "./component/auth/signup_page";
import {BrowserRouter, Routes, Route} from "react-router-dom";
import HomePage from "./component/home/home_page";
import UserService from './service/UserService';
import BookDetails from './component/book/BookDetails';
import SearchPage from './component/home/SearchPage';
import { disableReactDevTools } from '@fvilers/disable-react-devtools';
import { Component } from 'react';

disableReactDevTools();

export default class App extends Component {

  componentDidMount() {
    // refresh token also expires once every 10 days and in this case user must login
    const nineMinutes = 1*1000*60*9; // refresh the access-token once per 9 minutes
    const refreshToken = localStorage.getItem("refresh-token");
    UserService.refreshToken();
    setInterval(() => {
      const refreshToken = localStorage.getItem("refresh-token");
      if(refreshToken) {
        UserService.refreshToken();
      }
    }, nineMinutes);
  }

  render() {
    return (
      <BrowserRouter>
          <Routes>
              <Route path="/login" element={<LoginPage redirect="/home"/>}/>
              <Route path="/signup" element={<SignupPage/>}/>
              <Route path="/home" element={<HomePage/>}/>
              <Route path="/" element={<HomePage/>}/>
              <Route path="/search" element={<SearchPage/>}/>
              <Route path="/book/details" element={<BookDetails/>}/>
          </Routes>
      </BrowserRouter>
    );
  }
}