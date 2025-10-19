import { Component } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { LoginRequest } from '../../core/models/login-request.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginRequest: LoginRequest = { username: '', password: '' };
  errorMessage: string = '';
  missingFields: string[] = [];
  showChangePasswordPrompt: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.missingFields = [];
    this.errorMessage = '';
    this.showChangePasswordPrompt = false;

    // Validar campos vacíos
    if (!this.loginRequest.username) {
      this.missingFields.push('Correo Electrónico');
    }
    if (!this.loginRequest.password) {
      this.missingFields.push('Contraseña');
    }

    if (this.missingFields.length > 0) {
      this.errorMessage = `Por favor, complete los siguientes campos: ${this.missingFields.join(', ')}.`;
      return;
    }

    this.authService.login(this.loginRequest).subscribe({
      next: (response) => {
        if (response.success) {
          if (response.requiereCambioPassword) {
            this.showChangePasswordPrompt = true;
            this.errorMessage = response.mensaje || 'Se requiere cambiar la contraseña.';
            // Almacenar el correo electrónico para usarlo en el componente de cambio de contraseña
            localStorage.setItem('changePasswordEmail', this.loginRequest.username);
          } else {
            this.router.navigate(['/dashboard']);
          }
        } else {
          this.errorMessage = response.mensaje || 'Error desconocido en la autenticación.';
        }
      },
      error: () => {
        this.errorMessage = 'Error en el servidor';
      }
    });
  }
}
