import {
    BrowserRouter,
    Routes,
    Route,
    Navigate
} from "react-router-dom";


import Login from "./pages/Login/Login";
import Register from "./pages/Register/Register";
import Dashboard from "./pages/Dashboard/Dashboard";

import ProtectedRoute
    from "./components/ProtectedRoute/ProtectedRoute";


function App() {

    return (

        <BrowserRouter>

            <Routes>

                {/* ========================= */}
                {/* DEFAULT */}
                {/* ========================= */}

                <Route
                    path="/"
                    element={
                        <Navigate
                            to="/login"
                            replace
                        />
                    }
                />


                {/* ========================= */}
                {/* LOGIN */}
                {/* ========================= */}

                <Route
                    path="/login"
                    element={<Login />}
                />


                {/* ========================= */}
                {/* REGISTER */}
                {/* ========================= */}

                <Route
                    path="/register"
                    element={<Register />}
                />


                {/* ========================= */}
                {/* PROTECTED DASHBOARD */}
                {/* ========================= */}

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