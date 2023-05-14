import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NotFoundComponent } from './not-found.component';

@NgModule({
  imports: [CommonModule, RouterModule],
  declarations: [NotFoundComponent],
})
export class NotFoundModule {}
