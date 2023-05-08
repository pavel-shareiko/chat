import { environment } from 'src/app/core/environment/environment';
import { Component } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  public readonly applicationName = environment.applicationName;
}
