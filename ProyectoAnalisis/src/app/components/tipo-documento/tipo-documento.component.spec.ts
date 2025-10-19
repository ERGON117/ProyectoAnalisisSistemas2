import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TipoDocumentoComponent } from './tipo-documento.component';

describe('TipoDocumentoComponent', () => {
  let component: TipoDocumentoComponent;
  let fixture: ComponentFixture<TipoDocumentoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoDocumentoComponent]
    });
    fixture = TestBed.createComponent(TipoDocumentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
