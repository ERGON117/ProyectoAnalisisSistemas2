// src/app/components/gestion-cuentas/gestion-cuentas.component.ts
import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../core/services/menu.service';
import { MenuResponse, OpcionItem } from '../../core/models/menu-response.model';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';

interface CreateCuentaDTO {
  idPersona: number;
  idStatusCuenta: number;
  idTipoSaldoCuenta: number;
  saldoAnterior?: number;
  debitos?: number;
  creditos?: number;
}

interface SaldoCuenta {
  idSaldoCuenta: number;
  persona: {
    idPersona: number;
    nombre?: string;
    apellido?: string;
    correoElectronico?: string;
  };
  statusCuenta: {
    idStatusCuenta: number;
    nombre: string;
  };
  tipoSaldoCuenta: {
    idTipoSaldoCuenta: number;
    nombre: string;
  };
  saldoAnterior: number;
  debitos: number;
  creditos: number;
}

interface CuentaFormData extends CreateCuentaDTO {
  idSaldoCuenta?: number;
}

@Component({
  selector: 'app-gestion-cuentas',
  templateUrl: './gestion-cuentas.component.html',
  styleUrls: ['./gestion-cuentas.component.css'],
})
export class GestionCuentasComponent implements OnInit {
  opcion: OpcionItem | null = null;
  formItem: Partial<CuentaFormData> = {};
  isEdit: boolean = false;
  items: SaldoCuenta[] = [];
  personas: any[] = [];
  errorMessage: string = '';
  loading: boolean = false;

  statusCuentas: any[] = [];
  tiposSaldoCuentas: any[] = [];
  documentos: any[] = [];
  saldoActual: number = 0;
  selectedPersonaId: number | null = null;

  constructor(
    private menuService: MenuService,
    private http: HttpClient
  ) {
    this.resetForm();
  }

  ngOnInit() {
    this.menuService.getMenuStructure().subscribe({
      next: (menus) => {
        this.opcion = this.findOpcion(menus, 'gestion-cuentas');
      },
      error: (error) => {
        console.error('Error cargando menú:', error);
      }
    });
    this.loadInitialData();
  }

  private findOpcion(menus: MenuResponse[], pagina: string): OpcionItem | null {
    for (const menu of menus) {
      if (menu.menuItems) {
        for (const menuItem of menu.menuItems) {
          if (menuItem.opciones) {
            for (const opcion of menuItem.opciones) {
              if (opcion.pagina === pagina) return opcion;
            }
          }
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

  private loadInitialData() {
    this.loading = true;
    const promises = [
      this.loadCatalogos(),
      this.loadPersonas(),
      this.loadAllCuentas()
    ];

    Promise.all(promises).finally(() => {
      this.loading = false;
    }).catch(error => {
      console.error('Error en carga inicial:', error);
      this.loading = false;
    });
  }

  private loadCatalogos(): Promise<void> {
    const url = `${environment.apiUrl}/api/cuentas/catalogos`;
    return this.http.get<{ statusCuentas: any[], tiposSaldoCuentas: any[] }>(url)
      .toPromise()
      .then((data) => {
        this.statusCuentas = data?.statusCuentas || [];
        this.tiposSaldoCuentas = data?.tiposSaldoCuentas || [];
      })
      .catch((error: HttpErrorResponse) => {
        console.error('Error al cargar catálogos:', error);
        this.errorMessage = 'Error al cargar catálogos';
        this.statusCuentas = [];
        this.tiposSaldoCuentas = [];
        throw error;
      });
  }

  private loadPersonas(): Promise<void> {
    const url = `${environment.apiUrl}/api/personas`;
    return this.http.get<any[]>(url)
      .toPromise()
      .then((data) => {
        this.personas = data || [];
      })
      .catch((error: HttpErrorResponse) => {
        console.error('Error al cargar personas:', error);
        this.personas = [];
        throw error;
      });
  }

  private loadAllCuentas(): Promise<void> {
    const url = `${environment.apiUrl}/api/cuentas`;
    return this.http.get<SaldoCuenta[]>(url)
      .toPromise()
      .then((data) => {
        this.items = data || [];
      })
      .catch((error: HttpErrorResponse) => {
        console.error('Error al cargar cuentas:', error);
        this.errorMessage = 'Error al cargar cuentas';
        this.items = [];
        throw error;
      });
  }

  loadCuentasByPersona(idPersona: number): void {
    if (idPersona > 0) {
      const url = `${environment.apiUrl}/api/cuentas/persona/${idPersona}`;
      this.http.get<SaldoCuenta[]>(url).subscribe({
        next: (data) => {
          this.items = data || [];
        },
        error: (error: HttpErrorResponse) => {
          this.errorMessage = 'Error al cargar cuentas de la persona';
          console.error('Error:', error);
          this.items = [];
        }
      });
    } else {
      this.loadAllCuentas();
    }
  }

  loadCuentaById(id: number): void {
    const url = `${environment.apiUrl}/api/cuentas/${id}`;
    this.http.get<SaldoCuenta>(url).subscribe({
      next: (response) => {
        if (response) {
          this.formItem = {
            idSaldoCuenta: response.idSaldoCuenta,
            idPersona: response.persona.idPersona,
            idStatusCuenta: response.statusCuenta.idStatusCuenta,
            idTipoSaldoCuenta: response.tipoSaldoCuenta.idTipoSaldoCuenta,
            saldoAnterior: response.saldoAnterior || 0,
            debitos: response.debitos || 0,
            creditos: response.creditos || 0
          };
          this.isEdit = true;
          this.selectedPersonaId = response.persona.idPersona;
          this.loadDocumentosPersona(response.persona.idPersona);
          this.calcularSaldoActual();
        }
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = 'Error al cargar cuenta';
        console.error('Error:', error);
      }
    });
  }

  loadDocumentosPersona(idPersona: number): void {
    const url = `${environment.apiUrl}/api/cuentas/persona/${idPersona}/documentos`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        this.documentos = data || [];
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error al cargar documentos:', error);
        this.documentos = [];
      }
    });
  }

  private calcularSaldoActual(): void {
    const saldoAnterior = parseFloat(String(this.formItem.saldoAnterior || 0)) || 0;
    const debitos = parseFloat(String(this.formItem.debitos || 0)) || 0;
    const creditos = parseFloat(String(this.formItem.creditos || 0)) || 0;
    this.saldoActual = saldoAnterior + debitos - creditos;
  }

  submitForm(): void {
    const payload = this.buildPayload();
    if (this.isEdit && this.formItem.idSaldoCuenta) {
      this.updateItem(payload);
    } else {
      this.addItem(payload);
    }
  }

  private buildPayload(): CreateCuentaDTO {
    return {
      idPersona: parseInt(String(this.formItem.idPersona || 0)),
      idStatusCuenta: parseInt(String(this.formItem.idStatusCuenta || 0)),
      idTipoSaldoCuenta: parseInt(String(this.formItem.idTipoSaldoCuenta || 0)),
      saldoAnterior: parseFloat(String(this.formItem.saldoAnterior || 0)),
      debitos: parseFloat(String(this.formItem.debitos || 0)),
      creditos: parseFloat(String(this.formItem.creditos || 0))
    };
  }

  addItem(payload: CreateCuentaDTO): void {
    const url = `${environment.apiUrl}/api/cuentas`;
    this.http.post<SaldoCuenta>(url, payload, { headers: this.getAuthHeaders() }).subscribe({
      next: (response) => {
        console.log('Cuenta creada:', response);
        if (response) {
          this.items.push(response);
        }
        this.errorMessage = '';
        this.resetForm();
        this.loadAllCuentas();
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = 'Error al crear cuenta: ' + (error.error?.message || error.message);
        console.error('Error:', error);
      }
    });
  }

  updateItem(payload: CreateCuentaDTO): void {
  const cuentaId = this.formItem.idSaldoCuenta;
  if (!cuentaId) {
    this.errorMessage = 'ID de cuenta requerido para actualización';
    return;
  }

  const url = `${environment.apiUrl}/api/cuentas/${cuentaId}`;
  this.http.put<SaldoCuenta>(url, payload, { headers: this.getAuthHeaders() }).subscribe({
    next: (response) => {
      console.log('Cuenta actualizada:', response);
      if (response) {
        const index = this.items.findIndex(item => item.idSaldoCuenta === cuentaId);
        if (index !== -1) {
          this.items[index] = response;
        }
      }
      this.errorMessage = '';

      // ✅ CAMBIO: Limpiar formulario después de actualizar exitosamente
      this.resetForm();

      // Recargar lista para datos actualizados
      this.loadAllCuentas();
    },
    error: (error: HttpErrorResponse) => {
      this.errorMessage = 'Error al actualizar cuenta: ' + (error.error?.message || error.message);
      console.error('Error:', error);
    }
  });
}

  deleteItem(id: number): void {
    if (confirm('¿Estás seguro de eliminar esta cuenta?')) {
      const url = `${environment.apiUrl}/api/cuentas/${id}`;
      this.http.delete(url, { headers: this.getAuthHeaders() }).subscribe({
        next: () => {
          console.log('Cuenta eliminada:', id);
          this.items = this.items.filter(item => item.idSaldoCuenta !== id);
          this.errorMessage = '';
        },
        error: (error: HttpErrorResponse) => {
          this.errorMessage = 'Error al eliminar cuenta: ' + (error.error?.message || error.message);
          console.error('Error:', error);
        }
      });
    }
  }

  editItem(item: SaldoCuenta): void {
    this.formItem = {
      idSaldoCuenta: item.idSaldoCuenta,
      idPersona: item.persona.idPersona,
      idStatusCuenta: item.statusCuenta.idStatusCuenta,
      idTipoSaldoCuenta: item.tipoSaldoCuenta.idTipoSaldoCuenta,
      saldoAnterior: item.saldoAnterior,
      debitos: item.debitos,
      creditos: item.creditos
    };
    this.isEdit = true;
    this.selectedPersonaId = item.persona.idPersona;
    this.loadDocumentosPersona(item.persona.idPersona);
    this.calcularSaldoActual();
  }

  onPersonaChange(): void {
    const idPersona = parseInt(String(this.formItem.idPersona || 0));
    if (idPersona > 0) {
      this.loadDocumentosPersona(idPersona);
      this.loadCuentasByPersona(idPersona);
      this.selectedPersonaId = idPersona;
    } else {
      this.documentos = [];
      this.selectedPersonaId = null;
    }
    this.calcularSaldoActual();
  }

  onSaldoChange(): void {
    this.calcularSaldoActual();
  }

  resetForm(): void {
    this.formItem = {
      idPersona: 0,
      idStatusCuenta: 0,
      idTipoSaldoCuenta: 0,
      saldoAnterior: 0,
      debitos: 0,
      creditos: 0,
      idSaldoCuenta: undefined
    };
    this.documentos = [];
    this.saldoActual = 0;
    this.isEdit = false;
    this.selectedPersonaId = null;
  }

  // Validaciones con retorno explícito de boolean
  hasPersonaData(): boolean {
    return !!(
      this.formItem.idPersona &&
      this.formItem.idPersona > 0 &&
      this.selectedPersonaId !== null
    );
  }

  hasBasicCuentaData(): boolean {
    return !!(
      this.formItem.idPersona &&
      this.formItem.idPersona > 0 &&
      this.formItem.idStatusCuenta &&
      this.formItem.idStatusCuenta > 0 &&
      this.formItem.idTipoSaldoCuenta &&
      this.formItem.idTipoSaldoCuenta > 0
    );
  }

  print(): void {
    window.print();
  }

  exportData(): void {
    const csv = 'ID Cuenta,Persona,Status,Tipo Saldo,Saldo Anterior,Debitos,Creditos,Saldo Actual\n' +
      this.items.map(item => {
        const persona = this.personas.find(p => p.idPersona === item.persona.idPersona);
        const personaNombre = persona ? `"${persona.nombre || ''} ${persona.apellido || ''}"`.trim() : '';
        const saldoActual = this.getSaldoActualItem(item);
        return `${item.idSaldoCuenta},${personaNombre},"${item.statusCuenta.nombre}","${item.tipoSaldoCuenta.nombre}",${item.saldoAnterior},${item.debitos},${item.creditos},${saldoActual}`;
      }).join('\n');

    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);
    link.setAttribute('href', url);
    link.setAttribute('download', `cuentas_${new Date().toISOString().split('T')[0]}.csv`);
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  getPersonaNombre(idPersona: number): string {
    const persona = this.personas.find(p => p.idPersona === idPersona);
    return persona ? `${persona.nombre || ''} ${persona.apellido || ''}`.trim() : 'Persona no encontrada';
  }

  getSaldoActualItem(item: SaldoCuenta): number {
    return item.saldoAnterior + item.debitos - item.creditos;
  }

  filtrarPorPersona(event: any): void {
    const idPersona = parseInt(event.target.value || '0');
    this.loadCuentasByPersona(idPersona);
  }
}
