import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "src/app/auth/auth.service";
import {RegistrationUser} from "src/app/models/user.model"

@Component({
  selector: "app-registration",
  templateUrl: "./registration.component.html",
  styleUrls: ["../login/login.component.scss"],
})
export class RegistrationComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      firstname: ["", Validators.required],
      lastname: ["", Validators.required],
      login: ["", Validators.required],
      password: ["", Validators.required],
    });
  }

  register() {
    const val = this.form.value;
    if ((val.login, val.password, val.firstname, val.lastname)) {
      const user: RegistrationUser = {firstName: val.firstname, lastName: val.lastname, username: val.login, password: val.password}
      console.log(user)
      this.authService
        .register(user)
        .subscribe(() => {
          this.router.navigateByUrl("/login");
        });
        
    }
    
  }
}
