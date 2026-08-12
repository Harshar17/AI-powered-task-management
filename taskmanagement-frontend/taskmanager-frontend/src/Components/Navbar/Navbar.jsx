import React from "react";
import { useNavigate } from "react-router-dom";
import "./Navbar.css";

const Navbar = ({ userName }) => {

    const navigate = useNavigate();


    const handleLogout = () => {

        localStorage.removeItem("token");

        navigate("/login");
    };


    return (

        <nav className="navbar">

            <div
                className="navbar-logo"
                onClick={() => navigate("/dashboard")}
            >
                ✨ AI Task Manager
            </div>


            <div className="navbar-right">

                {userName && (

                    <span className="navbar-user">
                        Welcome, {userName}
                    </span>

                )}


                <button
                    className="navbar-logout"
                    onClick={handleLogout}
                >
                    Logout
                </button>

            </div>

        </nav>
    );
};


export default Navbar;