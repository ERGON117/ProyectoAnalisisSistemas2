import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.css']
})
export class RolesComponent implements OnInit {
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
      this.opcion = this.findOpcion(menus, 'roles');
    });
    this.loadRoles();
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

  private loadRoles() {
    const url = `${environment.apiUrl}/api/roles`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar roles: ' + error.message;
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
    const url = `${environment.apiUrl}/api/roles`;
    const { idRole, ...addData } = this.formItem; // Exclude idRole from POST
    this.http.post(url, addData, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Rol creado:', response);
        this.items.push(response);
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al crear rol: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem() {
    const url = `${environment.apiUrl}/api/roles/${this.formItem.idRole}`;
    const { usuarioModificacion, ...updateData } = this.formItem;
    this.http.put(url, updateData, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Rol actualizado:', response);
        const index = this.items.findIndex(item => item.idRole === this.formItem.idRole);
        if (index !== -1) this.items[index] = response;
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al actualizar rol: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  deleteItem(id: string) {
    if (confirm('¿Estás seguro de eliminar este rol?')) {
      const url = `${environment.apiUrl}/api/roles/${id}`;
      this.http.delete(url).subscribe({
        next: () => {
          console.log('Rol eliminado:', id);
          this.items = this.items.filter(item => item.idRole !== id);
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error al eliminar rol: ' + (error.error?.message || error.message);
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
      idRole: '',
      nombre: ''
    };
    this.isEdit = false;
  }

  print() {
    window.print();
  }

  exportData() {
    const csv = 'idRole,nombre,usuarioCreacion,usuarioModificacion,fechaCreacion,fechaModificacion\n' +
      this.items.map(item => `${item.idRole},${item.nombre},${item.usuarioCreacion},${item.usuarioModificacion},${item.fechaCreacion},${item.fechaModificacion}`).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'roles.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
