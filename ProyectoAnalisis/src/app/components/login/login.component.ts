// src/app/components/login/login.component.ts
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

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.missingFields = [];
    this.errorMessage = '';

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
          this.router.navigate(['/dashboard']);
        } else {
          this.errorMessage = 'Advertencia: Contraseña incorrecta o usuario no válido.';
        }
      },
      error: () => {
        this.errorMessage = 'Error en el servidor';
      }
    });
  }
}
