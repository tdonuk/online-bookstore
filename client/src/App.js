import './App.css';
import LoginPage from "./component/auth/login_page";
import SignupPage from "./component/auth/signup_page";
import {BrowserRouter, Routes, Route} from "react-router-dom";
import HomePage from "./component/home/home_page";
import UserService from './service/UserService';

// refresh token also expires once every 10 days and in this case user must login
const tenMinutes = 1*1000*60*9; // refresh the access-token once per 9 minutes

setInterval(() => {
  const refreshToken = localStorage.getItem("refresh-token");
  if(refreshToken) {
    UserService.refreshToken();
  }
}, tenMinutes);


function App() {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/login" element={<LoginPage redirect="/home"/>}/>
            <Route path="/signup" element={<SignupPage/>}/>
            <Route path="/home" element={<HomePage/>}/>
            <Route path="/" element={<HomePage/>}/>
        </Routes>
    </BrowserRouter>
  );
}

export default App;
