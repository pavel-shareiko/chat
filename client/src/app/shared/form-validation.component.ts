import { Component, Input } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { FormValidationService } from './form-validation.service';

export interface FormError {
  control: AbstractControl;
  errorKey: string;
  errorMessage: string | undefined;
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
  errorMessage: string = '';

  constructor(private formValidationService: FormValidationService) {}

  shouldDisplayError() {
    for (let validation of this.validations) {
      const wasTouched = this.formValidationService.isControlTouched(
        validation.control,
        this.formSubmitted
      );
      if (!wasTouched) return false;

      if (validation.control.hasError(validation.errorKey)) {
        if (!validation.errorMessage) {
          this.errorMessage = 'Please, enter valid data';
          return true;
        }
        this.errorMessage = validation.errorMessage;
        return true;
      }
    }

    return false;
  }
}
