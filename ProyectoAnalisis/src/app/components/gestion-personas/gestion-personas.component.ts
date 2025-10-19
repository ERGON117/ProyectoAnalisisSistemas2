// src/app/components/gestion-personas/gestion-personas.component.ts
import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-gestion-personas',
  templateUrl: './gestion-personas.component.html',
  styleUrls: ['./gestion-personas.component.css']
})
export class GestionPersonasComponent implements OnInit {
  opcion: OpcionItem | null = null;
  formItem: any;
  isEdit: boolean = false;
  items: any[] = [];
  errorMessage: string = '';

  // Catálogos
  generos: any[] = [];
  estadosCiviles: any[] = [];
  tiposDocumento: any[] = [];

  // Documentos de la persona actual
  documentos: any[] = [];

  constructor(private menuService: MenuService, private http: HttpClient) {
    this.resetForm();
  }

  ngOnInit() {
    this.menuService.getMenuStructure().subscribe(menus => {
      this.opcion = this.findOpcion(menus, 'gestion-personas');
    });
    this.loadCatalogos();
    this.loadPersonas();
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

  private loadCatalogos() {
    const url = `${environment.apiUrl}/api/personas/catalogos`;
    this.http.get<any>(url).subscribe({
      next: (data) => {
        this.generos = data.generos || [];
        this.estadosCiviles = data.estadosCiviles || [];
        this.tiposDocumento = data.tiposDocumento || [];
      },
      error: (error) => {
        console.error('Error al cargar catálogos:', error);
      }
    });
  }

  private loadPersonas() {
    const url = `${environment.apiUrl}/api/personas`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar personas: ' + error.message;
        console.error('Error:', error);
      }
    });
  }

  loadPersonaCompleta(id: number) {
  const url = `${environment.apiUrl}/api/personas/${id}`;
  this.http.get<any>(url).subscribe({
    next: (response) => {
      this.formItem = {
        idPersona: response.persona.idPersona,
        nombre: response.persona.nombre || '',
        apellido: response.persona.apellido || '',
        fechaNacimiento: response.persona.fechaNacimiento ?
          new Date(response.persona.fechaNacimiento).toISOString().split('T')[0] : '',
        idGenero: response.persona.genero?.idGenero?.toString() || '',
        idEstadoCivil: response.persona.estadoCivil?.idEstadoCivil?.toString() || '',
        direccion: response.persona.direccion || '',
        telefono: response.persona.telefono || '',
        correoElectronico: response.persona.correoElectronico || ''
      };

      // FIX: Extraer idTipoDocumento del objeto anidado
      this.documentos = (response.documentos || []).map((doc: any) => ({
        idTipoDocumento: doc.tipoDocumento.idTipoDocumento.toString(), // ← AQUÍ
        noDocumento: doc.noDocumento || ''
      }));

      console.log('Documentos mapeados:', this.documentos); // Para verificar
      this.isEdit = true;
    },
    error: (error) => {
      this.errorMessage = 'Error al cargar persona: ' + error.message;
      console.error('Error:', error);
    }
  });
}

  submitForm() {
    const payload = this.buildPayload();
    if (this.isEdit) {
      this.updateItem(payload);
    } else {
      this.addItem(payload);
    }
  }

  private buildPayload() {
    return {
      nombre: this.formItem.nombre,
      apellido: this.formItem.apellido,
      fechaNacimiento: this.formItem.fechaNacimiento,
      idGenero: parseInt(this.formItem.idGenero),
      direccion: this.formItem.direccion,
      telefono: this.formItem.telefono,
      correoElectronico: this.formItem.correoElectronico,
      idEstadoCivil: parseInt(this.formItem.idEstadoCivil),
      documentos: this.documentos.map(doc => ({
        idTipoDocumento: parseInt(doc.idTipoDocumento),
        noDocumento: doc.noDocumento
      }))
    };
  }

  addItem(payload: any) {
    const url = `${environment.apiUrl}/api/personas`;
    this.http.post(url, payload, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Persona creada:', response);
        this.items.push(response);
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al crear persona: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem(payload: any) {
    const url = `${environment.apiUrl}/api/personas/${this.formItem.idPersona}`;
    this.http.put(url, payload, { headers: this.getAuthHeaders() }).subscribe({
      next: (response: any) => {
        console.log('Persona actualizada:', response);
        const index = this.items.findIndex(item => item.idPersona === this.formItem.idPersona);
        if (index !== -1) this.items[index] = response;
        this.errorMessage = '';
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Error al actualizar persona: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  deleteItem(id: number) {
    if (confirm('¿Estás seguro de eliminar esta persona y sus documentos?')) {
      const url = `${environment.apiUrl}/api/personas/${id}`;
      this.http.delete(url).subscribe({
        next: () => {
          console.log('Persona eliminada:', id);
          this.items = this.items.filter(item => item.idPersona !== id);
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error al eliminar persona: ' + (error.error?.message || error.message);
          console.error('Error:', error);
        }
      });
    }
  }

  editItem(item: any) {
    this.loadPersonaCompleta(item.idPersona);
  }

  agregarDocumento() {
    this.documentos.push({
      idTipoDocumento: '',
      noDocumento: ''
    });
  }

  eliminarDocumento(index: number) {
    this.documentos.splice(index, 1);
  }

  resetForm() {
    this.formItem = {
      idPersona: '',
      nombre: '',
      apellido: '',
      fechaNacimiento: '',
      idGenero: '',
      direccion: '',
      telefono: '',
      correoElectronico: '',
      idEstadoCivil: ''
    };
    this.documentos = [];
    this.isEdit = false;
  }

  print() {
    window.print();
  }

  exportData() {
    const csv = 'idPersona,nombre,apellido,correoElectronico,telefono,direccion,genero,estadoCivil,fechaNacimiento\n' +
      this.items.map(item =>
        `${item.idPersona},${item.nombre},${item.apellido},${item.correoElectronico},${item.telefono || ''},${item.direccion || ''},${item.genero?.nombre || ''},${item.estadoCivil?.nombre || ''},${item.fechaNacimiento || ''}`
      ).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'personas.csv';
    a.click();
    window.URL.revokeObjectURL(url);
  }

  // NUEVOS MÉTODOS PARA CONTROLAR VISIBILIDAD DE DOCUMENTOS
  hasPersonaData(): boolean {
    return !!(this.formItem.nombre?.trim() &&
              this.formItem.apellido?.trim() &&
              this.formItem.correoElectronico?.trim());
  }

  hasBasicPersonaData(): boolean {
    return !!(this.formItem.nombre?.trim() &&
              this.formItem.apellido?.trim() &&
              this.formItem.fechaNacimiento &&
              this.formItem.idGenero &&
              this.formItem.idEstadoCivil &&
              this.formItem.correoElectronico?.trim());
  }
}
