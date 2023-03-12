import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "src/app/auth/auth.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
})
export class LoginComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      login: ["", Validators.required],
      password: ["", Validators.required],
    });
  }

  login (){
    const val = this.form.value;
    if (this.form.valid) {
      this.authService.login({username: val.login, password: val.password})
          .subscribe(
              () => {
                  this.router.navigateByUrl('/');
              }
          );
    }
  }
}
