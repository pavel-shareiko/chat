import {Component} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "src/app/auth/auth.service";
import {CustomValidators} from "../../shared/validators";
import {FormValidationService} from "../../shared/form-validation.service";

@Component({
    selector: "app-registration",
    templateUrl: "./registration.component.html",
    styleUrls: ["../login/login.component.scss"],
})
export class RegistrationComponent {
    form: FormGroup;
    isSubmitted = false;
    requestSubmitted = false;

    constructor(
        public formValidationService: FormValidationService,
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) {
        this.form = this.fb.group({
            firstname: ["", [Validators.required, Validators.minLength(3)]],
            lastname: ["", [Validators.required, Validators.minLength(3)]],
            username: ["", [Validators.required, Validators.minLength(3)]],
            password: ["", [Validators.required, CustomValidators.passwordValidator]],
        }, {
            updateOn: 'blur'
        });
    }

    register() {
        this.isSubmitted = true;
        if (this.form.invalid) {
            return;
        }
        if (this.requestSubmitted) {
            return;
        }

        this.requestSubmitted = true;
        this.authService.register({
            firstName: this.form.value.firstname,
            lastName: this.form.value.lastname,
            username: this.form.value.login,
            password: this.form.value.password,
        }).subscribe(() => {
            this.router.navigateByUrl("/login");
        });
    }
}
