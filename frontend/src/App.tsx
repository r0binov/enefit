import './App.css'
import {BrowserRouter, Route, Routes} from "react-router";
import LoginPage from "./components/LoginPage.tsx";
import LandingPage from "./components/LandingPage.tsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/" element={<LandingPage/>}/>
            </Routes>
        </BrowserRouter>
    )
}

export default App
