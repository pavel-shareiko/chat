import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/auth/auth.service';
import { FormValidationService } from '../../shared/form-validation.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  form: FormGroup;
  isSubmitted = false;
  errorMessage = '';

  constructor(
    public formValidationService: FormValidationService,
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group(
      {
        username: ['', Validators.required],
        password: ['', Validators.required],
      },
      {
        updateOn: 'blur',
      }
    );
  }

  login() {
    this.isSubmitted = true;
    const val = this.form.value;
  
    if (!val) {
      throw new Error('Form value is null or undefined');
    }
  
    const { username, password } = val;
  
    if (!username || !password) {
      throw new Error('Username or password is missing');
    }
  
    this.authService.login({ username, password }).subscribe({
      complete: () => {
        this.router.navigateByUrl('/chats');
      },
      error: errorResponse => {
        if (!this.errorMessage) {
          this.errorMessage = errorResponse.error.message;
          this.isSubmitted = false;
          setTimeout(() => {
            this.errorMessage = '';
          }, 5000);
        }
      },
    });
  }
}
