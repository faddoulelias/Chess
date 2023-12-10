import React from 'react'
import "./styles/LoginPage.css";
import API from '../api/API';

interface LoginPageProps {

}

export async function authenticate(username: string, password: string) {
    let success = await API.authenticate(username, password);
    if (success) {
        localStorage.setItem("token", btoa(username + ":" + password));
        window.location.href = "/";
    } else {
        alert("Invalid username or password!");
    }
}

export default function LoginPage(props: LoginPageProps) {
    const [username, setUsername] = React.useState("");
    const [password, setPassword] = React.useState("");

    return (
        <div className="login-page">
            <div className="login-form">
                <div className="login-form-title">Login</div>
                <input className="login-form-input" type="text" placeholder="Username"
                    onChange={(e) => {
                        setUsername(e.target.value);
                    }}
                ></input>
                <input className="login-form-input" type="password" placeholder="Password"
                    onChange={(e) => {
                        setPassword(e.target.value);
                    }}
                ></input>
                <button className="login-form-button"
                    onClick={() => {
                        authenticate(username, password);
                        console.log("Login button clicked!");
                    }}
                >Login</button>

                <div className="login-form-signup">Don't have an account? <a href="/signup">Sign up</a></div>
            </div>
        </div>
    )
}
