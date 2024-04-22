import { Component } from '@angular/core';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faFileExport, faPen, faPlus } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-root',
  template: '<router-outlet></router-outlet>'
})
export class AppComponent {

  constructor(library: FaIconLibrary) {
    library.addIcons(faPlus, faPen, faFileExport);
  }
}
