import {Component, Input} from '@angular/core';
import {AbstractControl, FormGroup} from '@angular/forms';

export interface FormError {
    control: AbstractControl,
    errorKey: string,
    errorMessage: string | undefined
}

@Component({
    selector: 'app-form-validation[validations]',
    template: `
      <div *ngIf="shouldDisplayError()" class="text-danger">
        {{ errorMessage }}
      </div>
    `,
})
export class FormValidationComponent {
    @Input() validations!: FormError[];
    @Input() formSubmitted: boolean | undefined;

    errorMessage: string = "";

    shouldDisplayError() {
        for (let validation of this.validations) {
            const wasTouched = validation.control.dirty || validation.control.touched || this.formSubmitted;
            if (!wasTouched) return false;

            if (validation.control.hasError(validation.errorKey)) {
                if (!validation.errorMessage) {
                    this.errorMessage = "Please, enter valid data";
                    return true;
                }
                this.errorMessage = validation.errorMessage;
                return true;
            }
        }

        return false;
    }
}