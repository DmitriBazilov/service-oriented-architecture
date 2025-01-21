import { Component, inject } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, RouterOutlet } from '@angular/router';
import { FunService } from '@soa2-front/shared';
import { TuiRoot } from '@taiga-ui/core';
import { TuiSwitch, TuiTabs } from '@taiga-ui/kit';
import { TuiInputModule } from '@taiga-ui/legacy';

@Component({
  standalone: true,
  imports: [
    TuiRoot, 
    TuiInputModule, 
    ReactiveFormsModule, 
    RouterOutlet, 
    RouterModule,
    TuiTabs,
    TuiSwitch,
  ],
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.less',
})
export class AppComponent {
  private readonly funService = inject(FunService)
  protected readonly funControl = new FormControl<boolean>(false);

  public ngOnInit() {
    this.funControl.valueChanges.subscribe((value) => {
      this.funService.funToggle$.next(value || false);
    })
  }

  title = 'soa2-front';
}
