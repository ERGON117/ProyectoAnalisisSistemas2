// src/app/components/status-cuenta/status-cuenta.component.ts
import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-status-cuenta',
  templateUrl: './status-cuenta.component.html',
  styleUrls: ['./status-cuenta.component.css']
})
export class StatusCuentaComponent implements OnInit {
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
      this.opcion = this.findOpcion(menus, 'status-cuenta');
    });
    this.loadStatusCuentas();
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
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token || ''}`,
      'Content-Type': 'application/json'
    });
  }

  private loadStatusCuentas() {
    const url = `${environment.apiUrl}/api/status-cuenta`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar status de cuenta: ' + error.message;
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
    const url = `${environment.apiUrl}/api/status-cuenta`;
    this.http.post(url, this.formItem, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('StatusCuenta creado:', response);
        this.items.push(response);
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al crear status de cuenta: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem() {
    const url = `${environment.apiUrl}/api/status-cuenta/${this.formItem.idStatusCuenta}`;
    this.http.put(url, this.formItem, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('StatusCuenta actualizado:', response);
        const index = this.items.findIndex(item => item.idStatusCuenta === this.formItem.idStatusCuenta);
        if (index !== -1) this.items[index] = response;
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al actualizar status de cuenta: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  deleteItem(id: number) {
    if (confirm('¿Estás seguro de eliminar este status de cuenta?')) {
      const url = `${environment.apiUrl}/api/status-cuenta/${id}`;
      this.http.delete(url).subscribe({
        next: () => {
          console.log('StatusCuenta eliminado:', id);
          this.items = this.items.filter(item => item.idStatusCuenta !== id);
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error al eliminar status de cuenta: ' + (error.error?.message || error.message);
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
      idStatusCuenta: '',
      nombre: ''
    };
    this.isEdit = false;
  }

  print() {
    window.print();
  }

  exportData() {
    const csv = 'idStatusCuenta,nombre,fechaCreacion,usuarioCreacion,fechaModificacion,usuarioModificacion\n' +
      this.items.map(item =>
        `${item.idStatusCuenta},${item.nombre},"${item.fechaCreacion || ''}","${item.usuarioCreacion || ''}","${item.fechaModificacion || ''}","${item.usuarioModificacion || ''}"`
      ).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'status-cuenta.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
