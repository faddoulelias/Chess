import React from 'react'
import "./styles/SignupPage.css";

export default function SignupPage() {
    return (
        <div className="signup-page">
            <div className="signup-form">
                <div className="signup-form-title">Sign up</div>
                <input className="signup-form-input" type="text" placeholder="Username"></input>
                <input className="signup-form-input" type="password" placeholder="Password"></input>
                <input className="signup-form-input" type="password" placeholder="Confirm Password"></input>
                <button className="signup-form-button">Sign up</button>

                <div className="signup-form-login">Already have an account? <a href="/login">Login</a></div>
            </div>
        </div>
    )
}
