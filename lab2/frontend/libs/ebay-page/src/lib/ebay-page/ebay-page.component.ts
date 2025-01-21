import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EbayDecreaseComponent } from '@soa2-front/ebay-decrease';
import { EbayIncreaseComponent } from '@soa2-front/ebay-increase';
import { FunService } from '@soa2-front/shared';

@Component({
  selector: 'lib-ebay-page',
  standalone: true,
  imports: [
    CommonModule,
    EbayDecreaseComponent,
    EbayIncreaseComponent,
  ],
  templateUrl: './ebay-page.component.html',
  styleUrl: './ebay-page.component.less',
})
export class EbayPageComponent {
  private readonly funService = inject(FunService);

  protected readonly funToggle$ = this.funService.funToggle$;
}
