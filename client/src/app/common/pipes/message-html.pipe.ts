import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Pipe({
  name: 'messageHtml',
})
export class MessageHtmlPipe implements PipeTransform {
  constructor(private sanitizer: DomSanitizer) {}
  transform(message: string): SafeHtml {
    const urlRegex = /((https?:\/\/)|(www\.))[^\s]+/g;
    const emailRegex = /([^\s@]+@[^\s@]+\.[^\s@]+)/g;

    // Replace < and > with &lt; and &gt;
    message = message.replace(/</g, '&lt;').replace(/>/g, '&gt;');

    // Replace URLs with clickable links
    message = message.replace(urlRegex, url => {
      const href = url.startsWith('http') ? url : `//${url}`;
      return `<a href="${href}" target="_blank">${url}</a>`;
    });

    // Replace email addresses with mailto links
    message = message.replace(emailRegex, email => {
      return `<a href="mailto:${email}">${email}</a>`;
    });

    // Replace newlines with <br>
    message = message.replace(/\n/g, '<br />');

    // Sanitize the resulting HTML to prevent XSS attacks
    return this.sanitizer.bypassSecurityTrustHtml(message);
  }
}
