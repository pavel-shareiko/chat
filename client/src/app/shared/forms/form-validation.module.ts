import { NgModule } from '@angular/core';
import { FormValidationComponent } from './form-validation.component';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [CommonModule],
  declarations: [FormValidationComponent],
  exports: [FormValidationComponent],
})
export class FormValidationModule {}
