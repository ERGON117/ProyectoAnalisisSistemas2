import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TipoMovimientoCXCComponent } from './tipo-movimiento-cxc.component';

describe('TipoMovimientoCXCComponent', () => {
  let component: TipoMovimientoCXCComponent;
  let fixture: ComponentFixture<TipoMovimientoCXCComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoMovimientoCXCComponent]
    });
    fixture = TestBed.createComponent(TipoMovimientoCXCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
