import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient , HttpHeaders} from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-modulos',
  templateUrl: './modulos.component.html',
  styleUrls: ['./modulos.component.css']
})
export class ModulosComponent implements OnInit {
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
      this.opcion = this.findOpcion(menus, 'modulos');
    });
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
  private loadModulos() {
    const url = `${environment.apiUrl}/api/modulos`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar módulos: ' + error.message;
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
    const url = `${environment.apiUrl}/api/modulos`;
    const { idModulo, ...addData } = this.formItem; // Exclude idModulo from POST
    this.http.post(url, addData, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Módulo creado:', response);
        this.items.push(response);
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al crear módulo: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem() {
    const url = `${environment.apiUrl}/api/modulos/${this.formItem.idModulo}`;
    const { usuarioModificacion, ...updateData } = this.formItem;
    this.http.put(url, updateData,  { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Módulo actualizado:', response);
        const index = this.items.findIndex(item => item.idModulo === this.formItem.idModulo);
        if (index !== -1) this.items[index] = response;
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al actualizar módulo: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  deleteItem(id: string) {
    if (confirm('¿Estás seguro de eliminar este módulo?')) {
      const url = `${environment.apiUrl}/api/modulos/${id}`;
      this.http.delete(url).subscribe({
        next: () => {
          console.log('Módulo eliminado:', id);
          this.items = this.items.filter(item => item.idModulo !== id);
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error al eliminar módulo: ' + (error.error?.message || error.message);
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
      idModulo: '',
      nombre: '',
      ordenMenu: 0
    };
    this.isEdit = false;
  }

  print() {
    window.print();
  }

  exportData() {
    const csv = 'idModulo,nombre,ordenMenu,usuarioCreacion,usuarioModificacion,fechaCreacion,fechaModificacion\n' +
      this.items.map(item => `${item.idModulo},${item.nombre},${item.ordenMenu},${item.usuarioCreacion},${item.usuarioModificacion},${item.fechaCreacion},${item.fechaModificacion}`).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'modulos.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
