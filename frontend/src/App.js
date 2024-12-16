import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from "./component/Home";
import About from "./component/About";
import NotFound from "./component/NotFound";

function App() {
    return (
        <Router basename="textile/react">
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/about" element={<About />} />
                <Route path="*" element={<NotFound />} />
            </Routes>
        </Router>
    );
}

export default App;
