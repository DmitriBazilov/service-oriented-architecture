import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TuiAlertService, TuiButton } from '@taiga-ui/core';
import { TuiInputNumberModule, TuiTextfieldControllerModule } from '@taiga-ui/legacy';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { EbayService } from '@soa2-front/shared';

@Component({
  selector: 'lib-ebay-decrease',
  standalone: true,
  imports: [
    CommonModule,
    TuiButton,
    TuiInputNumberModule,
    TuiTextfieldControllerModule,
    ReactiveFormsModule,
  ],
  templateUrl: './ebay-decrease.component.html',
  styleUrl: './ebay-decrease.component.less',
})
export class EbayDecreaseComponent {
  private readonly ebayService = inject(EbayService);
  private readonly alertService = inject(TuiAlertService);

  protected readonly percentControl = new FormControl<number>(0);

  protected decrease() {
    this.ebayService.decrease(this.percentControl.value || 0).subscribe({
      next: () => {
        this.alertService.open("Вы успешно уменьшили стоимость", {
          appearance: "success",
          label: "Успешно!"
        }).subscribe();
      }, 
      error: () => {
        this.alertService.open("Не удалось уменьшить стоимость", {
          appearance: "error",
          label: "Ошибка!"
        }).subscribe();
      }
    })
  }
}
