import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EbayIncreaseComponent } from './ebay-increase.component';

describe('EbayIncreaseComponent', () => {
  let component: EbayIncreaseComponent;
  let fixture: ComponentFixture<EbayIncreaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EbayIncreaseComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(EbayIncreaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
