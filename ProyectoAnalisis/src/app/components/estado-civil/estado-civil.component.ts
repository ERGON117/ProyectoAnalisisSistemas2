// src/app/components/estado-civil/estado-civil.component.ts
import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-estado-civil',
  templateUrl: './estado-civil.component.html',
  styleUrls: ['./estado-civil.component.css']
})
export class EstadoCivilComponent implements OnInit {
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
      this.opcion = this.findOpcion(menus, 'estado-civil');
    });
    this.loadEstadosCiviles();
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

  private loadEstadosCiviles() {
    const url = `${environment.apiUrl}/api/estado-civil`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar estados civiles: ' + error.message;
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
    const url = `${environment.apiUrl}/api/estado-civil`;
    this.http.post(url, this.formItem, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('EstadoCivil creado:', response);
        this.items.push(response);
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al crear estado civil: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem() {
    const url = `${environment.apiUrl}/api/estado-civil/${this.formItem.idEstadoCivil}`;
    this.http.put(url, this.formItem, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('EstadoCivil actualizado:', response);
        const index = this.items.findIndex(item => item.idEstadoCivil === this.formItem.idEstadoCivil);
        if (index !== -1) this.items[index] = response;
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al actualizar estado civil: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  deleteItem(id: number) {
    if (confirm('¿Estás seguro de eliminar este estado civil?')) {
      const url = `${environment.apiUrl}/api/estado-civil/${id}`;
      this.http.delete(url).subscribe({
        next: () => {
          console.log('EstadoCivil eliminado:', id);
          this.items = this.items.filter(item => item.idEstadoCivil !== id);
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error al eliminar estado civil: ' + (error.error?.message || error.message);
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
      idEstadoCivil: '',
      nombre: ''
    };
    this.isEdit = false;
  }

  print() {
    window.print();
  }

  exportData() {
    const csv = 'idEstadoCivil,nombre,fechaCreacion,usuarioCreacion,fechaModificacion,usuarioModificacion\n' +
      this.items.map(item =>
        `${item.idEstadoCivil},${item.nombre},"${item.fechaCreacion || ''}","${item.usuarioCreacion || ''}","${item.fechaModificacion || ''}","${item.usuarioModificacion || ''}"`
      ).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'estado-civil.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
