import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Product } from '@soa2-front/shared';

@Component({
  selector: 'lib-product-view',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-view.component.html',
  styleUrl: './product-view.component.less',
})
export class ProductViewComponent {
  public product = input.required<Product>();
}
