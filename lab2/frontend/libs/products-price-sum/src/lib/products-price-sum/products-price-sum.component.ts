import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BehaviorSubject, switchMap } from 'rxjs';
import { ProductsService } from '@soa2-front/shared';
import { TuiButton } from '@taiga-ui/core';

@Component({
  selector: 'lib-products-price-sum',
  standalone: true,
  imports: [CommonModule, TuiButton],
  templateUrl: './products-price-sum.component.html',
  styleUrl: './products-price-sum.component.less',
})
export class ProductsPriceSumComponent {
  private readonly productsService = inject(ProductsService);

  private readonly search$ = new BehaviorSubject<number>(0);

  protected readonly sum$ = this.search$.pipe(
    switchMap(() => this.productsService.getPricesSum())
  );

  protected reload(): void {
    this.search$.next(this.search$.value + 1);
  }
}
