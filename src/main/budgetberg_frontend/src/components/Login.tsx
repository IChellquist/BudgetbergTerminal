import React, { FormEvent, useEffect, useState } from "react";
import "../css/bootstrap/bootstrap.min.css";
import useInput from "../hooks/use-input";
import { useDispatch } from "react-redux";
import { loginActions } from "../reduxstore/login-slice";


const Login: React.FC = () => {

  
  const {
    value: enteredEmail,
    hasError: emailInputHasError,
    isValid: enteredEmailIsValid,
    valueChangeHandler: emailChangeHandler,
    inputBlurHandler: emailBlurHandler,
    reset: resetEmailInput,
  } = useInput((value: String) =>
    String(value)
      .toLowerCase()
      .match(
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      )
  );

  const {
    value: enteredPassword,
    hasError: enteredPasswordHasError,
    isValid: enteredPasswordIsValid,
    valueChangeHandler: passwordChangeHandler,
    inputBlurHandler: passwordBlurHandler,
    reset: resetPasswordInput,
  } = useInput((value: String) => value.trim() !== "");

  const [invalidLogin, setInvalidLogin] = useState(false);
  const emailInputClasses = emailInputHasError
    ? "form-control is-invalid"
    : "form-control";

  const passwordInputClasses = enteredPasswordHasError
    ? "form-control is-invalid"
    : "form-control";

  let formValid = enteredEmailIsValid && enteredPasswordIsValid;
  const dispatch = useDispatch();

  const handleLogin = async (e: FormEvent<HTMLFormElement>) => {
    e?.preventDefault();
    if (!enteredEmailIsValid) {
      return;
    }
    resetEmailInput();
    resetPasswordInput();

    const response = await fetch("http://localhost:8080" + "/api/auth/signin", {
      method: "POST",
      body: JSON.stringify({
        username: enteredEmail,
        password: enteredPassword,
      }),
      headers: { "Content-Type": "application/json" },
    });

    if (!response.ok) {
      console.log("Error!");
      setInvalidLogin(true);
      return;
    } else {
      const data: any = await response.json();
      const jwtToken = data.token;
      const jwtTokenExpiration = Date.now() + 86400000;
      localStorage.setItem("jwtToken", jwtToken);
      localStorage.setItem("jwtTokenExpiration", jwtTokenExpiration.toString());
      dispatch(loginActions.logIn({}));
    }
  };

  return (
    <>
      <div className="col-md-12">
        <div className="card card-container">
          <form onSubmit={handleLogin}>
            <div className="form-group">
              <label htmlFor="email">Email</label>
              <input
                type="text"
                className={emailInputClasses}
                name="email"
                onChange={emailChangeHandler}
                onBlur={emailBlurHandler}
                value={enteredEmail}
              ></input>
            </div>
            <div className="form-group">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                className={passwordInputClasses}
                name="password"
                value={enteredPassword}
                onChange={passwordChangeHandler}
                onBlur={passwordBlurHandler}
              ></input>
            </div>
            <div className="form-group">
              <button disabled={!formValid}>Submit</button>
            </div>
            {emailInputHasError && <p>Entered email is invalid</p>}
            {enteredPasswordHasError && <p>Entered password is invalid</p>}
            {invalidLogin && (
              <div className="alert alert-danger">
                Your login could not be validated. Please try again!
              </div>
            )}
          </form>
        </div>
      </div>
    </>
  );
};

export default Login;
