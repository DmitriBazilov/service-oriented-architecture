import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TuiAlertService, TuiButton, TuiDialogService } from '@taiga-ui/core';
import { TuiInputNumberModule } from '@taiga-ui/legacy';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { BehaviorSubject } from 'rxjs';
import { Product, ProductsService } from '@soa2-front/shared';
import { ProductViewComponent } from '@soa2-front/product-view';
import { ProductEditComponent } from '@soa2-front/product-edit';
import { PolymorpheusContent } from '@taiga-ui/polymorpheus';

@Component({
  selector: 'lib-product-by-id',
  standalone: true,
  imports: [
    CommonModule, 
    TuiButton,
    TuiInputNumberModule,
    ReactiveFormsModule,
    ProductViewComponent,
    ProductEditComponent,
  ],
  templateUrl: './product-by-id.component.html',
  styleUrl: './product-by-id.component.less',
})
export class ProductByIdComponent {
  private readonly productsService = inject(ProductsService);
  private readonly alertService = inject(TuiAlertService);
  private readonly dialogs = inject(TuiDialogService);

  protected readonly product$ = new BehaviorSubject<Product | null>(null);

  protected readonly idControl = new FormControl<number>(1);

  protected getById() {
    console.log(this.idControl.value)
    this.productsService.getById(this.idControl.value || 0).subscribe({
      next: (product: Product) => {
        this.product$.next(product);
      },
      error: () => {
        console.log('ok')
        this.product$.next(null);
    
        this.alertService.open("Не удалось получить продукт по ID", {
          appearance: "error",
          label: "Ошибка!"
        }).subscribe();
      },
    })
  }

  protected deleteById() {
    this.productsService.deleteProduct(this.idControl.value || 0).subscribe({
      next: () => {
        this.alertService.open("Вы успешно удалили продукт", {
          appearance: "success",
          label: "Успешно!"
        }).subscribe();
      }, 
      error: () => {
        this.alertService.open("Не удалось удалить продукт", {
          appearance: "error",
          label: "Ошибка!"
        }).subscribe();
      }
    })
  }

  protected edit(template: PolymorpheusContent) {
    this.dialogs.open(template, {
      label: 'Редактирование продукта'
    }).subscribe()
  }
}
