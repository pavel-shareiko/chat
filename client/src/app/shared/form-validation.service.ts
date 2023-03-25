import {Injectable} from "@angular/core";
import {AbstractControl} from "@angular/forms";

@Injectable({
    providedIn: "root"
})
export class FormValidationService {
    isControlTouched(control: AbstractControl<any>, formSubmitted: boolean = false) {
        return control.dirty || control.touched || formSubmitted;
    }

    isControlTouchedAndInvalid(control: AbstractControl<any>, formSubmitted: boolean = false) {
        return this.isControlTouched(control, formSubmitted) && control.invalid;
    }
}