import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpcionesComponent } from './opciones.component';

describe('OpcionesComponent', () => {
  let component: OpcionesComponent;
  let fixture: ComponentFixture<OpcionesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OpcionesComponent]
    });
    fixture = TestBed.createComponent(OpcionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
