import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TipoSaldoCuentaComponent } from './tipo-saldo-cuenta.component';

describe('TipoSaldoCuentaComponent', () => {
  let component: TipoSaldoCuentaComponent;
  let fixture: ComponentFixture<TipoSaldoCuentaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoSaldoCuentaComponent]
    });
    fixture = TestBed.createComponent(TipoSaldoCuentaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
