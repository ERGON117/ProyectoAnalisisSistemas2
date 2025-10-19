// src/app/app.module.ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { MenuComponent } from './components/menu/menu.component';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { OptionComponent } from './components/option/option.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { UsuariosComponent } from './components/usuarios/usuarios.component';
import { EmpresasComponent } from './components/empresas/empresas.component';
import { SucursalesComponent } from './components/sucursales/sucursales.component';
import { GenerosComponent } from './components/generos/generos.component';
import { StatusUsuariosComponent } from './components/status-usuarios/status-usuarios.component';
import { RolesComponent } from './components/roles/roles.component';
import { ModulosComponent } from './components/modulos/modulos.component';
import { MenusComponent } from './components/menus/menus.component';
import { OpcionesComponent } from './components/opciones/opciones.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { RoleOpcionComponent } from './components/role-opcion/role-opcion.component';
import { RoleOpcionService } from './core/services/role-opcion.service';
import { StatusCuentaComponent } from './components/status-cuenta/status-cuenta.component';
import { EstadoCivilComponent } from './components/estado-civil/estado-civil.component';
import { TipoDocumentoComponent } from './components/tipo-documento/tipo-documento.component';
import { TipoMovimientoCXCComponent } from './components/tipo-movimiento-cxc/tipo-movimiento-cxc.component';
import { TipoSaldoCuentaComponent } from './components/tipo-saldo-cuenta/tipo-saldo-cuenta.component';
import { GestionPersonasComponent } from './components/gestion-personas/gestion-personas.component';
import { GestionCuentasComponent } from './components/gestion-cuentas/gestion-cuentas.component';
import { ConsultaSaldosComponent } from './components/consulta-saldos/consulta-saldos.component';
import { CierreMesComponent } from './components/cierre-mes/cierre-mes.component';
import { EstadoCuentaComponent } from './components/estado-cuenta/estado-cuenta.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ProfileComponent,
    ChangePasswordComponent,
    MenuComponent,
    OptionComponent,
    DashboardComponent,
    UsuariosComponent,
    EmpresasComponent,
    SucursalesComponent,
    GenerosComponent,
    StatusUsuariosComponent,
    RolesComponent,
    ModulosComponent,
    MenusComponent,
    OpcionesComponent,
    ResetPasswordComponent,
    RoleOpcionComponent,
    StatusCuentaComponent,
    EstadoCivilComponent,
    TipoDocumentoComponent,
    TipoMovimientoCXCComponent,
    TipoSaldoCuentaComponent,
    GestionPersonasComponent,
    GestionCuentasComponent,
    ConsultaSaldosComponent,
    CierreMesComponent,
    EstadoCuentaComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,


  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
