import { EstadoCivilComponent } from './components/estado-civil/estado-civil.component';
import { TipoDocumentoComponent } from './components/tipo-documento/tipo-documento.component';
import { TipoMovimientoCXCComponent } from './components/tipo-movimiento-cxc/tipo-movimiento-cxc.component';
import { TipoSaldoCuentaComponent } from './components/tipo-saldo-cuenta/tipo-saldo-cuenta.component';
import { GestionPersonasComponent } from './components/gestion-personas/gestion-personas.component';
import { GestionCuentasComponent } from './components/gestion-cuentas/gestion-cuentas.component';
import { ConsultaSaldosComponent } from './components/consulta-saldos/consulta-saldos.component';
import { CierreMesComponent } from './components/cierre-mes/cierre-mes.component';
import { EstadoCuentaComponent } from './components/estado-cuenta/estado-cuenta.component';
// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ProfileComponent } from './components/profile/profile.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { OptionComponent } from './components/option/option.component';
import { AuthGuard } from './core/guards/auth.guard';
import { UsuariosComponent } from './components/usuarios/usuarios.component';
import { EmpresasComponent } from './components/empresas/empresas.component';
import { SucursalesComponent } from './components/sucursales/sucursales.component';
import { GenerosComponent } from './components/generos/generos.component';
import { StatusUsuariosComponent } from './components/status-usuarios/status-usuarios.component';
import { StatusCuentaComponent } from './components/status-cuenta/status-cuenta.component';
import { RolesComponent } from './components/roles/roles.component';
import { ModulosComponent } from './components/modulos/modulos.component';
import { MenusComponent } from './components/menus/menus.component';
import { OpcionesComponent } from './components/opciones/opciones.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { RoleOpcionComponent } from './components/role-opcion/role-opcion.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'usuarios', component: UsuariosComponent, canActivate: [AuthGuard] },
  { path: 'empresas', component: EmpresasComponent, canActivate: [AuthGuard] },
  { path: 'sucursales', component: SucursalesComponent, canActivate: [AuthGuard] },
  { path: 'generos', component: GenerosComponent, canActivate: [AuthGuard] },
  { path: 'status-usuarios', component: StatusUsuariosComponent, canActivate: [AuthGuard] },
  { path: 'status-cuenta', component: StatusCuentaComponent, canActivate: [AuthGuard] },
  { path: 'estado-civil', component: EstadoCivilComponent, canActivate: [AuthGuard] },
  { path: 'tipo-documento', component: TipoDocumentoComponent, canActivate: [AuthGuard] },
  { path: 'tipo-movimiento-cxc', component: TipoMovimientoCXCComponent, canActivate: [AuthGuard] },
  { path: 'tipo-saldo-cuenta', component: TipoSaldoCuentaComponent, canActivate: [AuthGuard] },
  { path: 'gestion-personas', component: GestionPersonasComponent, canActivate: [AuthGuard] },
  { path: 'gestion-cuentas', component: GestionCuentasComponent, canActivate: [AuthGuard] },
  { path: 'consulta-saldos', component: ConsultaSaldosComponent, canActivate: [AuthGuard] },
  { path: 'estado-cuenta', component: EstadoCuentaComponent, canActivate: [AuthGuard] },
  { path: 'cierre-mes', component: CierreMesComponent, canActivate: [AuthGuard] },
  { path: 'roles', component: RolesComponent, canActivate: [AuthGuard] },
  { path: 'modulos', component: ModulosComponent, canActivate: [AuthGuard] },
  { path: 'menus', component: MenusComponent, canActivate: [AuthGuard] },
  { path: 'opciones', component: OpcionesComponent, canActivate: [AuthGuard] },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'role-opcion', component: RoleOpcionComponent, canActivate: [AuthGuard] },
  { path: 'change-password', component: ChangePasswordComponent},
  { path: ':option', component: OptionComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '/login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
