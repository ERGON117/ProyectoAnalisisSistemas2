import { Component, OnInit } from '@angular/core';
import { MenuService as MenuStructureService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-menus',
  templateUrl: './menus.component.html',
  styleUrls: ['./menus.component.css']
})
export class MenusComponent implements OnInit {
  opcion: OpcionItem | null = null;
  formItem: any;
  isEdit: boolean = false;
  items: any[] = [];
  errorMessage: string = '';
  modulos: any[] = [];

  constructor(private menuService: MenuStructureService, private http: HttpClient) {
    this.resetForm();
  }

  ngOnInit() {
    this.menuService.getMenuStructure().subscribe(menus => {
      this.opcion = this.findOpcion(menus, 'menus');
    });
    this.loadMenus();
    this.loadModulos();
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

  private loadMenus() {
    const url = `${environment.apiUrl}/api/menus`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar menús: ' + error.message;
        console.error('Error:', error);
      }
    });
  }

  private loadModulos() {
    const url = `${environment.apiUrl}/api/modulos`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.modulos = data.map(m => ({ idModulo: m.idModulo, nombre: m.nombre }));
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar módulos: ' + error.message;
        console.error('Error:', error);
      }
    });
  }

  submitForm() {
    if (!this.formItem.modulo.idModulo) {
      this.errorMessage = 'Debe seleccionar un módulo.';
      return;
    }
    if (this.isEdit) {
      this.updateItem();
    } else {
      this.addItem();
    }
  }

  addItem() {
    const url = `${environment.apiUrl}/api/menus`;
    const { idMenu, ...addData } = this.formItem; // Exclude idMenu from POST
    this.http.post(url, addData, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Menú creado:', response);
        this.items.push(response);
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al crear menú: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem() {
    const url = `${environment.apiUrl}/api/menus/${this.formItem.idMenu}`;
    const { usuarioModificacion, ...updateData } = this.formItem;
    this.http.put(url, updateData, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Menú actualizado:', response);
        const index = this.items.findIndex(item => item.idMenu === this.formItem.idMenu);
        if (index !== -1) this.items[index] = response;
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al actualizar menú: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  deleteItem(id: string) {
    if (confirm('¿Estás seguro de eliminar este menú?')) {
      const url = `${environment.apiUrl}/api/menus/${id}`;
      this.http.delete(url).subscribe({
        next: () => {
          console.log('Menú eliminado:', id);
          this.items = this.items.filter(item => item.idMenu !== id);
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error al eliminar menú: ' + (error.error?.message || error.message);
          console.error('Error:', error);
        }
      });
    }
  }

  editItem(item: any) {
    this.formItem = {
      ...item,
      modulo: { idModulo: item.modulo?.idModulo || null }
    };
    this.isEdit = true;
  }

  resetForm() {
    this.formItem = {
      idMenu: '',
      nombre: '',
      ordenMenu: 0,
      modulo: { idModulo: null }
    };
    this.isEdit = false;
  }

  print() {
    window.print();
  }

  exportData() {
    const csv = 'idMenu,nombre,ordenMenu,modulo,usuarioCreacion,usuarioModificacion,fechaCreacion,fechaModificacion\n' +
      this.items.map(item => `${item.idMenu},${item.nombre},${item.ordenMenu},${item.modulo?.nombre || 'Sin módulo'},${item.usuarioCreacion},${item.usuarioModificacion},${item.fechaCreacion},${item.fechaModificacion}`).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'menus.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
