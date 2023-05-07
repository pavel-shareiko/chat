import { AbstractControl, ValidationErrors } from '@angular/forms';

export class CustomValidators {
  static passwordValidator(control: AbstractControl): ValidationErrors | null {
    const passwordRegex = /^\S{8,}$/;
    const isValid = passwordRegex.test(control.value);

    return isValid
      ? null
      : {
          passwordValidator: {
            message: 'Password must be at least 8 characters long and contain no spaces',
          },
        };
  }

  static usernameValidator(control: AbstractControl): ValidationErrors | null {
    const usernameRegex = /^[a-zA-Z\d]{5,20}$/;
    const isValid = usernameRegex.test(control.value);

    return isValid
      ? null
      : {
          usernameValidator: {
            message: 'Username must be between 5 and 20 alphanumeric characters',
          },
        };
  }
}
