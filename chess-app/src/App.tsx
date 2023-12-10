import React from 'react';
import './App.css';
import GamePage from './pages/GamePage';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import API from './api/API';

type AuthenticationMode = 'login' | 'signup';

function AuthenticationPage(mode: AuthenticationMode) {
  switch (mode) {
    case 'login':
      return <LoginPage></LoginPage>;
    case 'signup':
      return <SignupPage></SignupPage>;
  }
}

function App() {
  const [loggedIn, setLoggedIn] = React.useState(false);
  const path = window.location.pathname;
  const authenticationMode = path === '/signup' ? 'signup' : 'login';

  React.useEffect(() => {
    let token = localStorage.getItem('token');
    if (token) {
      API.authenticateToken(token).then((success) => {
        if (success) {
          setLoggedIn(true);
          window.history.pushState(null, '', '/');
        } else {
          setLoggedIn(false);
        }
      });
    }
  }, []);


  return (
    <>
      {loggedIn ? (<GamePage id={67} ></GamePage>) : (AuthenticationPage(authenticationMode))}
    </>
  );
}

export default App;
