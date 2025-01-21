import { Component, inject, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Product } from '@soa2-front/shared';
import { toObservable } from '@angular/core/rxjs-interop';
import { TuiButton, TuiDialogService } from '@taiga-ui/core';
import { ProductViewComponent } from '@soa2-front/product-view';
import { PolymorpheusContent } from '@taiga-ui/polymorpheus';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'lib-products-table',
  standalone: true,
  imports: [CommonModule, TuiButton, ProductViewComponent],
  templateUrl: './products-table.component.html',
  styleUrl: './products-table.component.less',
})
export class ProductsTableComponent {
  private readonly dialogService = inject(TuiDialogService);

  protected readonly selectedProduct$ = new BehaviorSubject<Product | null>(null);

  public products = input.required<Product[]>();

  protected readonly products$ = toObservable(this.products);

  protected setViewProduct(template: PolymorpheusContent, product: Product): void {
    this.selectedProduct$.next(product);
  
    this.dialogService.open(template, {
      label: 'Просмотр продукта'
    }).subscribe()
  }
}
