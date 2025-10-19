import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient , HttpHeaders} from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-sucursales',
  templateUrl: './sucursales.component.html',
  styleUrls: ['./sucursales.component.css']
})
export class SucursalesComponent implements OnInit {
  opcion: OpcionItem | null = null;
  formItem: any;
  isEdit: boolean = false;
  items: any[] = [];
  errorMessage: string = '';
  empresas: any[] = [];

  constructor(private menuService: MenuService, private http: HttpClient) {
    this.resetForm();
  }

  ngOnInit() {
    this.menuService.getMenuStructure().subscribe(menus => {
      this.opcion = this.findOpcion(menus, 'sucursales');
    });
    this.loadSucursales();
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

  private loadSucursales() {
    const url = `${environment.apiUrl}/api/sucursales`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar sucursales: ' + error.message;
        console.error('Error:', error);
      }
    });
  }

  private loadEmpresas() {
    const url = `${environment.apiUrl}/api/sucursales/empresas`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.empresas = data.map(e => ({ idEmpresa: e.idEmpresa, nombre: e.nombre }));
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
    const url = `${environment.apiUrl}/api/sucursales`;
    const { idSucursal, ...addData } = this.formItem; // Exclude idSucursal from POST
    this.http.post(url, addData, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Sucursal creada:', response);
        this.items.push(response);
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al crear sucursal: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem() {
    const url = `${environment.apiUrl}/api/sucursales/${this.formItem.idSucursal}`;
    const { usuarioModificacion, ...updateData } = this.formItem;
    this.http.put(url, updateData, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Sucursal actualizada:', response);
        const index = this.items.findIndex(item => item.idSucursal === this.formItem.idSucursal);
        if (index !== -1) this.items[index] = response;
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al actualizar sucursal: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  deleteItem(id: string) {
    if (confirm('¿Estás seguro de eliminar esta sucursal?')) {
      const url = `${environment.apiUrl}/api/sucursales/${id}`;
      this.http.delete(url).subscribe({
        next: () => {
          console.log('Sucursal eliminada:', id);
          this.items = this.items.filter(item => item.idSucursal !== id);
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error al eliminar sucursal: ' + (error.error?.message || error.message);
          console.error('Error:', error);
        }
      });
    }
  }

  editItem(item: any) {
    this.formItem = {
      ...item,
      empresa: { idEmpresa: item.empresa?.idEmpresa || null }
    };
    this.isEdit = true;
  }

  resetForm() {
    this.formItem = {
      idSucursal: '',
      nombre: '',
      direccion: '',
      empresa: { idEmpresa: null }
    };
    this.isEdit = false;
  }

  print() {
    window.print();
  }

  exportData() {
    const csv = 'idSucursal,nombre,direccion,empresa,usuarioCreacion,usuarioModificacion,fechaCreacion,fechaModificacion\n' +
      this.items.map(item => `${item.idSucursal},${item.nombre},${item.direccion},${item.empresa?.nombre || 'Sin empresa'},${item.usuarioCreacion},${item.usuarioModificacion},${item.fechaCreacion},${item.fechaModificacion}`).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'sucursales.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
