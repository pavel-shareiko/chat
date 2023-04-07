import { HttpClient } from "@angular/common/http";
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
    input = "";
    output = "";

    constructor(
        public formValidationService: FormValidationService,
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private http: HttpClient
    ) {
        this.form = this.fb.group({
            username: ["", Validators.required],
            password: ["", Validators.required],
        }, {
            updateOn: 'blur'
        });
    }

    
    get() {
        this.http.get(this.input).subscribe(res => this.output = res.toString());
    }

    post() {
        this.http.post(this.input, {}).subscribe(res => this.output = res.toString());
    }

    login() {
        this.isSubmitted = true;
        const val = this.form.value;
        if (this.form.valid) {
            this.authService.login({username: val.username, password: val.password})
                .subscribe(
                    () => {
                        this.router.navigateByUrl('/chats');
                    }
                );
        }
    }
}
