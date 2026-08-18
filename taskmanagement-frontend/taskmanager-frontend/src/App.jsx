import {
    BrowserRouter,
    Routes,
    Route,
    Navigate
} from "react-router-dom";


import Login from "./Pages/Login/Login";
import Register from "./Pages/Register/Register";
import Dashboard from "./Pages/Dashboard/DashBoard";

import ProtectedRoute
    from "./Components/ProtectedRoute/ProtectedRoute";


function App() {

    return (

        <BrowserRouter>

            <Routes>

                <Route
                    path="/"
                    element={
                        <Navigate
                            to="/login"
                            replace
                        />
                    }
                />

                <Route
                    path="/login"
                    element={<Login />}
                />


                <Route
                    path="/register"
                    element={<Register />}
                />


                <Route
                    path="/dashboard"
                    element={
                        <ProtectedRoute>
                            <Dashboard />
                        </ProtectedRoute>
                    }
                />

            </Routes>

        </BrowserRouter>
    );
}


export default App;