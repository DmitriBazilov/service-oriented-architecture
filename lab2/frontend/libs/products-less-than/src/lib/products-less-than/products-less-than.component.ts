import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TuiAlertService, TuiButton, TuiDialogService } from '@taiga-ui/core';
import { TuiInputModule, TuiInputNumberComponent, TuiInputNumberModule } from '@taiga-ui/legacy';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { ProductsTableComponent } from '@soa2-front/products-table';
import { BehaviorSubject } from 'rxjs';
import { Product, ProductsService } from '@soa2-front/shared';
import { PolymorpheusContent } from '@taiga-ui/polymorpheus';

@Component({
  selector: 'lib-products-less-than',
  standalone: true,
  imports: [
    CommonModule,
    TuiButton, 
    TuiInputModule,
    ReactiveFormsModule,
    ProductsTableComponent,
    TuiInputNumberModule,
  ],
  templateUrl: './products-less-than.component.html',
  styleUrl: './products-less-than.component.less',
})
export class ProductsLessThanComponent {
  private readonly productsService = inject(ProductsService);
  private readonly alertService = inject(TuiAlertService);
  private readonly dialogService = inject(TuiDialogService);

  protected readonly manufactureCostControl = new FormControl<number>(1);
  protected readonly page = new FormControl<number>(1);
  protected readonly size = new FormControl<number>(10);

  protected readonly products$ = new BehaviorSubject<Product[] | null>(null);

  protected getProducts(template: PolymorpheusContent) {
    this.productsService.getLessThanProduct(
      this.manufactureCostControl.value || 0,
      this.page.value || 0,
      this.size.value || 0,
    ).subscribe({
      next: (products: Product[]) => {
        this.products$.next(products);

        this.dialogService.open(template, {
          label: 'Просмотр продукта'
        }).subscribe()
      },
      error: () => {
        this.alertService.open("Не удалось получить продукты", {
          appearance: "error",
          label: "Ошибка!"
        }).subscribe();
      },
    })
  }
}
