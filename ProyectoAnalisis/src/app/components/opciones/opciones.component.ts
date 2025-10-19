import { Component, OnInit } from '@angular/core';
import { MenuService as MenuStructureService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-opciones',
  templateUrl: './opciones.component.html',
  styleUrls: ['./opciones.component.css']
})
export class OpcionesComponent implements OnInit {
  opcion: OpcionItem | null = null;
  formItem: any;
  isEdit: boolean = false;
  items: any[] = [];
  errorMessage: string = '';
  menus: any[] = [];

  constructor(private menuService: MenuStructureService, private http: HttpClient) {
    this.resetForm();
  }

  ngOnInit() {
    this.menuService.getMenuStructure().subscribe(menus => {
      this.opcion = this.findOpcion(menus, 'opciones');
    });
    this.loadOpciones();
    this.loadMenus();
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

  private loadOpciones() {
    const url = `${environment.apiUrl}/api/opciones`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar opciones: ' + error.message;
        console.error('Error:', error);
      }
    });
  }

  private loadMenus() {
    const url = `${environment.apiUrl}/api/opciones/menus`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.menus = data.map(m => ({ idMenu: m.idMenu, nombre: m.nombre }));
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar menús: ' + error.message;
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
    const url = `${environment.apiUrl}/api/opciones`;
    const { idOpcion, ...addData } = this.formItem; // Exclude idOpcion from POST
    this.http.post(url, addData, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Opción creada:', response);
        this.items.push(response);
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al crear opción: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem() {
    const url = `${environment.apiUrl}/api/opciones/${this.formItem.idOpcion}`;
    const { usuarioModificacion, ...updateData } = this.formItem;
    this.http.put(url, updateData, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Opción actualizada:', response);
        const index = this.items.findIndex(item => item.idOpcion === this.formItem.idOpcion);
        if (index !== -1) this.items[index] = response;
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al actualizar opción: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  deleteItem(id: string) {
    if (confirm('¿Estás seguro de eliminar esta opción?')) {
      const url = `${environment.apiUrl}/api/opciones/${id}`;
      this.http.delete(url).subscribe({
        next: () => {
          console.log('Opción eliminada:', id);
          this.items = this.items.filter(item => item.idOpcion !== id);
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error al eliminar opción: ' + (error.error?.message || error.message);
          console.error('Error:', error);
        }
      });
    }
  }

  editItem(item: any) {
    this.formItem = {
      ...item,
      menu: { idMenu: item.menu?.idMenu || null }
    };
    this.isEdit = true;
  }

  resetForm() {
    this.formItem = {
      idOpcion: '',
      nombre: '',
      menu: { idMenu: null },
      ordenMenu: 0,
      pagina: ''
    };
    this.isEdit = false;
  }

  print() {
    window.print();
  }

  exportData() {
    const csv = 'idOpcion,nombre,menu,ordenMenu,pagina,usuarioCreacion,usuarioModificacion,fechaCreacion,fechaModificacion\n' +
      this.items.map(item => `${item.idOpcion},${item.nombre},${item.menu?.nombre || 'Sin menú'},${item.ordenMenu},${item.pagina},${item.usuarioCreacion},${item.usuarioModificacion},${item.fechaCreacion},${item.fechaModificacion}`).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'opciones.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
