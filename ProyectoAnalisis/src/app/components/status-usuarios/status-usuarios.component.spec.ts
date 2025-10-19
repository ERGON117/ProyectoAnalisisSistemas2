import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatusUsuariosComponent } from './status-usuarios.component';

describe('StatusUsuariosComponent', () => {
  let component: StatusUsuariosComponent;
  let fixture: ComponentFixture<StatusUsuariosComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StatusUsuariosComponent]
    });
    fixture = TestBed.createComponent(StatusUsuariosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
