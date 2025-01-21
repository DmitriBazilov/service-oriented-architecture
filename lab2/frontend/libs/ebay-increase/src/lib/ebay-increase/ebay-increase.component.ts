import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TuiAlertService, TuiButton } from '@taiga-ui/core';
import { TuiInputNumberModule, TuiTextfieldControllerModule } from '@taiga-ui/legacy';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { EbayService } from '@soa2-front/shared';

@Component({
  selector: 'lib-ebay-increase',
  standalone: true,
  imports: [
    CommonModule,
    TuiButton,
    TuiInputNumberModule,
    ReactiveFormsModule,
    TuiTextfieldControllerModule,
  ],
  templateUrl: './ebay-increase.component.html',
  styleUrl: './ebay-increase.component.less',
})
export class EbayIncreaseComponent {
  private readonly ebayService = inject(EbayService);
  private readonly alertService = inject(TuiAlertService);

  protected readonly percentControl = new FormControl<number>(0);

  protected increase() {
    this.ebayService.increase(this.percentControl.value || 0).subscribe({
      next: () => {
        this.alertService.open("Вы успешно увеличили стоимость", {
          appearance: "success",
          label: "Успешно!"
        }).subscribe();
      }, 
      error: () => {
        this.alertService.open("Не удалось увеличить стоимость", {
          appearance: "error",
          label: "Ошибка!"
        }).subscribe();
      }
    })
  }
}
