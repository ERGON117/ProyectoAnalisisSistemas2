// src/app/components/tipo-movimiento-cxc/tipo-movimiento-cxc.component.ts
import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-tipo-movimiento-cxc',
  templateUrl: './tipo-movimiento-cxc.component.html',
  styleUrls: ['./tipo-movimiento-cxc.component.css']
})
export class TipoMovimientoCXCComponent implements OnInit {
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
      this.opcion = this.findOpcion(menus, 'tipo-movimiento-cxc');
    });
    this.loadTiposMovimientoCXC();
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

  private loadTiposMovimientoCXC() {
    const url = `${environment.apiUrl}/api/tipo-movimiento-cxc`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar tipos de movimiento CXC: ' + error.message;
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
    const url = `${environment.apiUrl}/api/tipo-movimiento-cxc`;
    this.http.post(url, this.formItem, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('TipoMovimientoCXC creado:', response);
        this.items.push(response);
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al crear tipo de movimiento CXC: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem() {
    const url = `${environment.apiUrl}/api/tipo-movimiento-cxc/${this.formItem.idTipoMovimientoCXC}`;
    this.http.put(url, this.formItem, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('TipoMovimientoCXC actualizado:', response);
        const index = this.items.findIndex(item => item.idTipoMovimientoCXC === this.formItem.idTipoMovimientoCXC);
        if (index !== -1) this.items[index] = response;
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al actualizar tipo de movimiento CXC: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  deleteItem(id: number) {
    if (confirm('¿Estás seguro de eliminar este tipo de movimiento CXC?')) {
      const url = `${environment.apiUrl}/api/tipo-movimiento-cxc/${id}`;
      this.http.delete(url).subscribe({
        next: () => {
          console.log('TipoMovimientoCXC eliminado:', id);
          this.items = this.items.filter(item => item.idTipoMovimientoCXC !== id);
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error al eliminar tipo de movimiento CXC: ' + (error.error?.message || error.message);
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
      idTipoMovimientoCXC: '',
      nombre: '',
      operacionCuentaCorriente: 1
    };
    this.isEdit = false;
  }

  print() {
    window.print();
  }

  exportData() {
    const csv = 'idTipoMovimientoCXC,nombre,operacionCuentaCorriente,fechaCreacion,usuarioCreacion,fechaModificacion,usuarioModificacion\n' +
      this.items.map(item =>
        `${item.idTipoMovimientoCXC},${item.nombre},${item.operacionCuentaCorriente},"${item.fechaCreacion || ''}","${item.usuarioCreacion || ''}","${item.fechaModificacion || ''}","${item.usuarioModificacion || ''}"`
      ).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'tipo-movimiento-cxc.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }

  getOperacionText(operacion: number): string {
    return operacion === 1 ? 'Sumar' : 'Restar';
  }
}
