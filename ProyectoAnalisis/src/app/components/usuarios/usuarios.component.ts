// src/app/components/usuarios/usuarios.component.ts
import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.css']
})
export class UsuariosComponent implements OnInit {
  opcion: OpcionItem | null = null;
  formItem: any;
  isEdit: boolean = false;
  items: any[] = [];
  errorMessage: string = '';
  sucursales: any[] = [];
  roles: any[] = [];
  statusUsuarios: any[] = [];
  generos: any[] = [];

  constructor(private menuService: MenuService, private http: HttpClient) {
    this.resetForm();
  }

  ngOnInit() {
    this.menuService.getMenuStructure().subscribe(menus => {
      this.opcion = this.findOpcion(menus, 'usuarios');
    });
    this.loadUsuarios();
    this.loadSucursales();
    this.loadRoles();
    this.loadStatusUsuarios();
    this.loadGeneros();
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

  private loadUsuarios() {
    const url = `${environment.apiUrl}/api/usuarios`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar usuarios: ' + error.message;
        console.error('Error:', error);
      }
    });
  }

  private loadSucursales() {
    const url = `${environment.apiUrl}/api/usuarios/sucursales`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.sucursales = data.map(s => ({ idSucursal: s.idSucursal, nombre: s.nombre }));
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar sucursales: ' + error.message;
        console.error('Error:', error);
      }
    });
  }

  private loadRoles() {
    const url = `${environment.apiUrl}/api/usuarios/roles`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.roles = data.map(r => ({ idRole: r.idRole, nombre: r.nombre }));
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar roles: ' + error.message;
        console.error('Error:', error);
      }
    });
  }

  private loadStatusUsuarios() {
    const url = `${environment.apiUrl}/api/usuarios/status-usuarios`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.statusUsuarios = data.map(s => ({ idStatusUsuario: s.idStatusUsuario, nombre: s.nombre }));
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar estados de usuario: ' + error.message;
        console.error('Error:', error);
      }
    });
  }

  private loadGeneros() {
    const url = `${environment.apiUrl}/api/usuarios/generos`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.generos = data.map(g => ({ idGenero: g.idGenero, nombre: g.nombre }));
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar géneros: ' + error.message;
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
    const url = `${environment.apiUrl}/api/usuarios`;
    this.http.post(url, this.formItem,  { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Usuario creado:', response);
        this.items.push(response);
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al crear usuario: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem() {
    const url = `${environment.apiUrl}/api/usuarios/${this.formItem.idUsuario}`;
    const { password, ...updateData } = this.formItem;
    this.http.put(url, updateData,  { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Usuario actualizado:', response);
        const index = this.items.findIndex(item => item.idUsuario === this.formItem.idUsuario);
        if (index !== -1) this.items[index] = response;
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al actualizar usuario: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  deleteItem(id: string) {
    if (confirm('¿Estás seguro de eliminar este usuario?')) {
      const url = `${environment.apiUrl}/api/usuarios/${id}`;
      this.http.delete(url).subscribe({
        next: () => {
          console.log('Usuario eliminado:', id);
          this.items = this.items.filter(item => item.idUsuario !== id);
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error al eliminar usuario: ' + (error.error?.message || error.message);
          console.error('Error:', error);
        }
      });
    }
  }

  editItem(item: any) {
    this.formItem = {
      ...item,
      sucursal: { idSucursal: item.sucursal?.idSucursal || null },
      role: { idRole: item.role?.idRole || null },
      statusUsuario: { idStatusUsuario: item.statusUsuario?.idStatusUsuario || null },
      genero: { idGenero: item.genero?.idGenero || null }
    };
    this.isEdit = true;
  }

  resetForm() {
    this.formItem = {
      idUsuario: '',
      nombre: '',
      apellido: '',
      correoElectronico: '',
      password: '',
      sucursal: { idSucursal: null },
      role: { idRole: null },
      statusUsuario: { idStatusUsuario: null },
      genero: { idGenero: null },
      fechaNacimiento: '',
      telefonoMovil: '',
      pregunta: '',
      respuesta: ''
    };
    this.isEdit = false;
  }

  print() {
    window.print();
  }

  exportData() {
    const csv = 'idUsuario,nombre,apellido,correoElectronico,sucursal,role,statusUsuario,genero,fechaNacimiento,telefonoMovil\n' +
      this.items.map(item => `${item.idUsuario},${item.nombre},${item.apellido},${item.correoElectronico},${item.sucursal?.nombre || ''},${item.role?.nombre || ''},${item.statusUsuario?.nombre || ''},${item.genero?.nombre || ''},${item.fechaNacimiento},${item.telefonoMovil}`).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'usuarios.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
