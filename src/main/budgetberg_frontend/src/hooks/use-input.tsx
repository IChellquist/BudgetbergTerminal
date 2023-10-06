import { ChangeEvent, useState } from "react";

const useInput = (validateValue: any) => {
  const [enteredValue, setEnteredValue] = useState("");
  const [isTouched, setIsTouched] = useState(false);

  const valueIsValid = validateValue(enteredValue);
  const hasError = !valueIsValid && isTouched;

  const valueChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;
    setEnteredValue(newValue);
  };

  const inputBlurHandler = (event: ChangeEvent<HTMLInputElement>) => {
    setIsTouched(true);
  };

  const reset = () => {
    setEnteredValue("");
    setIsTouched(false);
  }
  return {
    value: enteredValue,
    hasError: hasError,
    isValid : valueIsValid,
    valueChangeHandler: valueChangeHandler,
    inputBlurHandler: inputBlurHandler,
    reset
  };
};

export default useInput;
