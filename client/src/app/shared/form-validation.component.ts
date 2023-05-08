import { Component, Input } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { FormValidationService } from './form-validation.service';

export interface FormError {
  control: AbstractControl;
  errorKey: string;
  errorMessage?: string;
  preventDefault?: boolean;
}

@Component({
  selector: 'app-form-validation[validations]',
  template: `
    <div *ngIf="shouldDisplayError()" class="text-danger small">
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
      if (!wasTouched) {
        return false;
      }

      if (validation.control.hasError(validation.errorKey)) {
        if (!validation.errorMessage) {
          const error = validation.control.getError(validation.errorKey);
          if (!validation.preventDefault && error && error['message']) {
            this.errorMessage = error['message'];
            return true;
          }
          this.errorMessage = 'Please provide a valid value';
          return true;
        }

        this.errorMessage = validation.errorMessage;
        return true;
      }
    }

    return false;
  }
}
