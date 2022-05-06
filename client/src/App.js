import './App.css';
import LoginPage from "./component/auth/login_page";
import SignupPage from "./component/auth/signup_page";
import {BrowserRouter, Routes, Route} from "react-router-dom";
import HomePage from "./component/home/home_page";

function App() {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/login" element={<LoginPage/>}/>
            <Route path="/signup" element={<SignupPage/>}/>
            <Route path="/home" element={<HomePage/>}/>
            <Route path="/" element={<HomePage/>}/>
        </Routes>
    </BrowserRouter>
  );
}

export default App;
