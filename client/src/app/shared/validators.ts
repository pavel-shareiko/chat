import {AbstractControl, ValidationErrors} from "@angular/forms";

export class CustomValidators {
    static passwordValidator(control: AbstractControl): ValidationErrors | null {
        const passwordRegex = /^\S{8,}$/;
        const valid = passwordRegex.test(control.value);
        return valid ? null : {passwordValidator: {message: "Password doesn't match the requirements"}};
    }
}