import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TuiAlertService, TuiButton, TuiDialogService } from '@taiga-ui/core';
import { Product, ProductsService } from '@soa2-front/shared';
import { BehaviorSubject } from 'rxjs';
import { ProductViewComponent } from '@soa2-front/product-view';
import { PolymorpheusContent } from '@taiga-ui/polymorpheus';
import { TuiInputModule } from '@taiga-ui/legacy';

@Component({
  selector: 'lib-product-min',
  standalone: true,
  imports: [
    CommonModule, 
    TuiButton, 
    ProductViewComponent,
    TuiInputModule,
  ],
  templateUrl: './product-min.component.html',
  styleUrl: './product-min.component.less',
})
export class ProductMinComponent {
  private readonly productsService = inject(ProductsService);
  private readonly alertService = inject(TuiAlertService);
  private readonly dialogs = inject(TuiDialogService);

  protected readonly minimalProduct$ = new BehaviorSubject<Product | null>(null);

  protected getMinimal(template: PolymorpheusContent): void {
    this.productsService.getMinimalProduct().subscribe({
      next: (product: Product) => {
        this.minimalProduct$.next(product)
        
        this.dialogs.open(template, {
          label: 'Просмотр продукта'
        }).subscribe()
      },
      error: () => {
        this.alertService.open("Не удалось получить минимальный продукт", {
          appearance: "error",
          label: "Ошибка!"
        }).subscribe();
      },
    })
  }
}
