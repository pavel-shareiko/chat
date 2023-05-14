import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/services/auth.service';
import { CustomValidators } from '../../shared/validators/validators';
import { FormValidationService } from '../../shared/forms/form-validation.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['../login/login.component.scss'],
})
export class RegistrationComponent {
  form: FormGroup;
  isSubmitted = false;
  requestSubmitted = false;
  errorMessage = '';

  constructor(
    public formValidationService: FormValidationService,
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group(
      {
        firstname: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(35)]],
        lastname: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(35)]],
        username: ['', [Validators.required, CustomValidators.usernameValidator]],
        password: ['', [Validators.required, CustomValidators.passwordValidator]],
      },
      {
        updateOn: 'blur',
      }
    );
  }

  register() {
    // Set isSubmitted flag to true
    this.isSubmitted = true;

    // Only continue if the form is valid and a request hasn't already been submitted
    if (this.form.invalid || this.requestSubmitted) {
      return;
    }

    // Set requestSubmitted flag to true to prevent multiple form submissions
    this.requestSubmitted = true;

    // Extract form values into an object with more descriptive keys
    const { firstname, lastname, username, password } = this.form.value;
    const registrationData = { firstName: firstname, lastName: lastname, username, password };

    // Call the register function in the auth service
    this.authService.register(registrationData).subscribe({
      // If the registration is successful, log the user in
      complete: () => {
        const loginData = { username, password };
        this.authService.login(loginData).subscribe(() => {
          this.router.navigate(['/chats']);
        });
      },
      // If there is an error, display a message to the user and clear the form
      error: errorResponse => {
        this.requestSubmitted = false;

        if (!this.errorMessage) {
          if (
            errorResponse.status === 400 &&
            errorResponse.error.message.includes('Validation failed')
          ) {
            this.errorMessage = this.extractComposedErrorMessage(errorResponse.error.errors);
          } else {
            this.errorMessage = errorResponse.error.message;
          }

          // Clear the error message after 8 seconds
          setTimeout(() => {
            this.errorMessage = '';
          }, 8000);
        }
      },
    });
  }
  extractComposedErrorMessage(errors: any): string {
    const errorMessages = errors.map((error: any) => {
      return `<li>${error.defaultMessage}</li>`;
    });
    return `
      <p>Your registration form contains following errors:</p>
      <ul> 
        ${errorMessages.join('')} 
      </ul>
    `;
  }
}
