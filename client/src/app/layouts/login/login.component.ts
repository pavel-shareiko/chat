import {Component} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "src/app/auth/auth.service";
import {FormValidationService} from "../../shared/form-validation.service";

@Component({
    selector: "app-login",
    templateUrl: "./login.component.html",
    styleUrls: ["./login.component.scss"],
})
export class LoginComponent {
    form: FormGroup;
    isSubmitted = false;

    constructor(
        public formValidationService: FormValidationService,
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) {
        this.form = this.fb.group({
            username: ["", Validators.required],
            password: ["", Validators.required],
        }, {
            updateOn: 'blur'
        });
    }

    login() {
        this.isSubmitted = true;
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
