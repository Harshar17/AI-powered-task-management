import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../services/api";
import "./Register.css";

function Register() {

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();


    const handleRegister = async (e) => {

        e.preventDefault();

        try {

            setLoading(true);

            await api.post("/auth/register", {
                name,
                email,
                password
            });

            alert("Registration successful! Please login.");

            navigate("/login");

        } catch (error) {

            console.error(error);

            alert(
                error.response?.data?.message ||
                "Registration failed"
            );

        } finally {

            setLoading(false);
        }
    };


    return (

        <div className="register-page">

            <div className="register-card">

                <div className="register-header">

                    <h1>Create Account</h1>

                    <p>
                        Start managing your tasks smarter with AI
                    </p>

                </div>


                <form onSubmit={handleRegister}>

                    <label>
                        Name
                    </label>

                    <input
                        type="text"
                        placeholder="Enter your name"
                        value={name}
                        onChange={(e) =>
                            setName(e.target.value)
                        }
                        required
                    />


                    <label>
                        Email
                    </label>

                    <input
                        type="email"
                        placeholder="Enter your email"
                        value={email}
                        onChange={(e) =>
                            setEmail(e.target.value)
                        }
                        required
                    />


                    <label>
                        Password
                    </label>

                    <input
                        type="password"
                        placeholder="Create a password"
                        value={password}
                        onChange={(e) =>
                            setPassword(e.target.value)
                        }
                        required
                    />


                    <button
                        type="submit"
                        disabled={loading}
                    >

                        {loading
                            ? "Creating Account..."
                            : "Create Account"
                        }

                    </button>

                </form>


                <p className="login-link">

                    Already have an account?

                    <span
                        onClick={() =>
                            navigate("/login")
                        }
                    >
                        Login
                    </span>

                </p>

            </div>

        </div>
    );
}

export default Register;