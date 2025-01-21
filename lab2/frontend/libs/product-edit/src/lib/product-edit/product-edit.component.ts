import { Component, inject, input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Product, UnitOfMeasureEngMap, UnitOfMeasureRusMap } from '@soa2-front/shared';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { noEmptyStringValidator } from './no-empty-string.validator';
import { moreTenStringValidator } from './more-ten-string.validator';
import { TuiInputModule, TuiInputNumberModule, TuiSelectModule } from '@taiga-ui/legacy';
import { TuiDataList } from '@taiga-ui/core/components/data-list';
import { TuiDataListWrapper } from '@taiga-ui/kit/components/data-list-wrapper';
import { TuiAlertService, TuiButton, TuiNumberFormat } from '@taiga-ui/core';
import { ProductsService } from '@soa2-front/shared';

@Component({
  selector: 'lib-product-edit',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    TuiInputModule,
    TuiSelectModule,
    TuiDataList,
    TuiDataListWrapper,
    TuiNumberFormat,
    TuiInputNumberModule,
    TuiButton,
  ],
  templateUrl: './product-edit.component.html',
  styleUrl: './product-edit.component.less',
})
export class ProductEditComponent implements OnInit {
  private readonly productsService = inject(ProductsService);
  private readonly alertService = inject(TuiAlertService);

  public product = input.required<Product>();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  public observer = input.required<any>();

  protected readonly unitsOfMeasure = [
    'Килограммы',
    'Метры',
    'Миллиграммы',
    'Миллилитры'
  ];

  protected readonly productForm = new FormGroup({
    name: new FormControl<string>('', [noEmptyStringValidator()]),
    coordinateX: new FormControl<number>(0),
    coordinateY: new FormControl<number>(0),
    price: new FormControl<number>(1),
    partNumber: new FormControl<string | null>(null, [moreTenStringValidator()]),
    manufactureCost: new FormControl<number>(0),
    unitOfMeasure: new FormControl<string>('Килограммы'),
    personPassportId: new FormControl<string | null>(null),
  });

  public ngOnInit(): void {
    const product = this.product();

    const unitOfMeasureMapped = UnitOfMeasureEngMap.get(product.unitOfMeasure) || 'Метры';

    this.productForm.setValue({
      name: product.name,
      coordinateX: product.coordinates.x,
      coordinateY: product.coordinates.y,
      price: product.price,
      partNumber: product.partNumber,
      manufactureCost: product.manufacturerCost,
      unitOfMeasure: unitOfMeasureMapped,
      personPassportId: product.owner.passportID,
    });
  }

  protected updateProduct(): void {
    const productId = this.product().id;

    if (!productId) {
      return;
    }
  
    const name = this.productForm.controls.name.value;
    const coordinateX = this.productForm.controls.coordinateX.value;
    const coordinateY = this.productForm.controls.coordinateY.value;
    const price = this.productForm.controls.price.value;
    const partNumber = this.productForm.controls.partNumber.value;
    const manufacturerCost = this.productForm.controls.manufactureCost.value;
    const unitOfMeasure = this.productForm.controls.unitOfMeasure.value;
    const personPassportId = this.productForm.controls.personPassportId.value;

    if (
      !name || coordinateX === null || coordinateY === null || price === null ||
      manufacturerCost === null || !unitOfMeasure
    ) {
      return;
    }

    const mappedUnitOfMeasure = UnitOfMeasureRusMap.get(unitOfMeasure);

    if (!mappedUnitOfMeasure) {
      return;
    }

    this.productsService.updateProduct(productId, {
      name,
      coordinates: {
        x: coordinateX,
        y: coordinateY,
      },
      price,
      partNumber,
      manufacturerCost,
      unitOfMeasure: mappedUnitOfMeasure,
      ownerPassportId: personPassportId,
    }).subscribe({
      next: () => {
        this.observer().complete();

        this.alertService.open("Вы успешно отредактировали продукт", {
          appearance: "success",
          label: "Успешно!"
        }).subscribe();
      },
      error: () => {
        this.alertService.open("Не удалось отредактировать продукт", {
          appearance: "error",
          label: "Ошибка!"
        }).subscribe();
      }
    });
  }
}
