// src/app/components/tipo-saldo-cuenta/tipo-saldo-cuenta.component.ts
import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-tipo-saldo-cuenta',
  templateUrl: './tipo-saldo-cuenta.component.html',
  styleUrls: ['./tipo-saldo-cuenta.component.css']
})
export class TipoSaldoCuentaComponent implements OnInit {
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
      this.opcion = this.findOpcion(menus, 'tipo-saldo-cuenta');
    });
    this.loadTiposSaldoCuenta();
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

  private loadTiposSaldoCuenta() {
    const url = `${environment.apiUrl}/api/tipo-saldo-cuenta`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar tipos de saldo cuenta: ' + error.message;
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
    const url = `${environment.apiUrl}/api/tipo-saldo-cuenta`;
    this.http.post(url, this.formItem, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('TipoSaldoCuenta creado:', response);
        this.items.push(response);
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al crear tipo de saldo cuenta: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem() {
    const url = `${environment.apiUrl}/api/tipo-saldo-cuenta/${this.formItem.idTipoSaldoCuenta}`;
    this.http.put(url, this.formItem, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('TipoSaldoCuenta actualizado:', response);
        const index = this.items.findIndex(item => item.idTipoSaldoCuenta === this.formItem.idTipoSaldoCuenta);
        if (index !== -1) this.items[index] = response;
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al actualizar tipo de saldo cuenta: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  deleteItem(id: number) {
    if (confirm('¿Estás seguro de eliminar este tipo de saldo cuenta?')) {
      const url = `${environment.apiUrl}/api/tipo-saldo-cuenta/${id}`;
      this.http.delete(url).subscribe({
        next: () => {
          console.log('TipoSaldoCuenta eliminado:', id);
          this.items = this.items.filter(item => item.idTipoSaldoCuenta !== id);
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error al eliminar tipo de saldo cuenta: ' + (error.error?.message || error.message);
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
      idTipoSaldoCuenta: '',
      nombre: ''
    };
    this.isEdit = false;
  }

  print() {
    window.print();
  }

  exportData() {
    const csv = 'idTipoSaldoCuenta,nombre,fechaCreacion,usuarioCreacion,fechaModificacion,usuarioModificacion\n' +
      this.items.map(item =>
        `${item.idTipoSaldoCuenta},${item.nombre},"${item.fechaCreacion || ''}","${item.usuarioCreacion || ''}","${item.fechaModificacion || ''}","${item.usuarioModificacion || ''}"`
      ).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'tipo-saldo-cuenta.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
