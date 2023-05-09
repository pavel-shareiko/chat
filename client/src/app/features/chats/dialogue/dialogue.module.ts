import { NgModule } from '@angular/core';
import { DialogueComponent } from './dialogue.component';
import { CommonModule } from '@angular/common';
import { MessageGroupingPipe } from 'src/app/shared/pipes/message-grouping.pipe';
import { MessageHtmlPipe } from 'src/app/shared/pipes/message-html.pipe';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
  imports: [CommonModule, MessageGroupingPipe, MessageHtmlPipe, FormsModule, FontAwesomeModule],
  declarations: [DialogueComponent],
  exports: [DialogueComponent],
})
export class DialogueModule {}
