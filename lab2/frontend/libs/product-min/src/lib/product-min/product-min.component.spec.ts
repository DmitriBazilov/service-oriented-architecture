import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductMinComponent } from './product-min.component';

describe('ProductMinComponent', () => {
  let component: ProductMinComponent;
  let fixture: ComponentFixture<ProductMinComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductMinComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductMinComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
