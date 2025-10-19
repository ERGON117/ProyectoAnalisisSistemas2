// src/app/components/profile/profile.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { User } from '../../core/models/user.model';
import { ChangePasswordRequest } from '../../core/models/change-password-request.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  changePasswordRequest: ChangePasswordRequest = { correoElectronico: '', currentPassword: '', newPassword: '' };
  errorMessage: string = '';
  successMessage: string = '';
  showPasswordForm: boolean = false;

  constructor(private authService: AuthService) {}

 // src/app/components/profile/profile.component.ts
ngOnInit() {
  this.authService.user$.subscribe(user => {
    this.user = user;
    console.log('Datos del usuario en ProfileComponent:', user); // Verifica aquí
  });
  this.authService.getProfile().subscribe(); // Fuerza la carga inicial
}

}
