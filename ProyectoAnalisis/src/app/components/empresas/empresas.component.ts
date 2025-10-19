// src/app/components/empresas/empresas.component.ts
import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-empresas',
  templateUrl: './empresas.component.html',
  styleUrls: ['./empresas.component.css']
})
export class EmpresasComponent implements OnInit {
  opcion: OpcionItem | null = null;
  formItem: any;
  isEdit: boolean = false;
  items: any[] = [];
  errorMessage: string = '';

  constructor(private menuService: MenuService, private http: HttpClient) {
    this.resetForm();
  }

  ngOnInit() {
    this.menuService.getMenuStructure().subscribe(menus => {
      this.opcion = this.findOpcion(menus, 'empresas');
    });
    this.loadEmpresas();
  }

  private findOpcion(menus: MenuResponse[], pagina: string): OpcionItem | null {
    for (const menu of menus) {
      for (const menuItem of menu.menuItems) {
        for (const opcion of menuItem.opciones) {
          if (opcion.pagina === pagina) return opcion;
        }
      }
    }
    return null;
  }

   private getAuthHeaders(): HttpHeaders {
        const token = localStorage.getItem('token'); // Assuming token is stored in localStorage
        return new HttpHeaders({
          'Authorization': `Bearer ${token || ''}`,
          'Content-Type': 'application/json'
        });
      }

  private loadEmpresas() {
    const url = `${environment.apiUrl}/api/empresas`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar empresas: ' + error.message;
        console.error('Error:', error);
      }
    });
  }

  submitForm() {
    if (this.isEdit) {
      this.updateItem();
    } else {
      this.addItem();
    }
  }

  addItem() {
    const url = `${environment.apiUrl}/api/empresas`;
    this.http.post(url, this.formItem, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Empresa creada:', response);
        this.items.push(response);
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al crear empresa: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem() {
    const url = `${environment.apiUrl}/api/empresas/${this.formItem.idEmpresa}`;
    this.http.put(url, this.formItem,   { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Empresa actualizada:', response);
        const index = this.items.findIndex(item => item.idEmpresa === this.formItem.idEmpresa);
        if (index !== -1) this.items[index] = response;
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al actualizar empresa: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  deleteItem(id: number) {
    if (confirm('¿Estás seguro de eliminar esta empresa?')) {
      const url = `${environment.apiUrl}/api/empresas/${id}`;
      this.http.delete(url).subscribe({
        next: () => {
          console.log('Empresa eliminada:', id);
          this.items = this.items.filter(item => item.idEmpresa !== id);
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error al eliminar empresa: ' + (error.error?.message || error.message);
          console.error('Error:', error);
        }
      });
    }
  }

  editItem(item: any) {
    this.formItem = { ...item };
    this.isEdit = true;
  }

  resetForm() {
    this.formItem = {
      idEmpresa: '',
      nombre: '',
      direccion: '',
      nit: '',
      passwordCantidadMayusculas: 0,
      passwordCantidadMinusculas: 0,
      passwordCantidadNumeros: 0,
      passwordCantidadCaracteresEspeciales: 0,
      passwordLargo: 0,
      passwordCantidadCaducidadDias: 0,
      passwordIntentosAntesDeBloquear: 0,
      passwordCantidadPreguntasValidar: 0
    };
    this.isEdit = false;
  }

  print() {
    window.print();
  }

  exportData() {
    const csv = 'idEmpresa,nombre,direccion,nit,passwordCantidadMayusculas,passwordCantidadMinusculas,passwordCantidadNumeros,passwordCantidadCaracteresEspeciales,passwordLargo,passwordCantidadCaducidadDias,passwordIntentosAntesDeBloquear,passwordCantidadPreguntasValidar\n' +
      this.items.map(item => `${item.idEmpresa},${item.nombre},${item.direccion},${item.nit},${item.passwordCantidadMayusculas},${item.passwordCantidadMinusculas},${item.passwordCantidadNumeros},${item.passwordCantidadCaracteresEspeciales},${item.passwordLargo},${item.passwordCantidadCaducidadDias},${item.passwordIntentosAntesDeBloquear},${item.passwordCantidadPreguntasValidar}`).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'empresas.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
