import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginRequest } from '../models/login-request.model';
import { AuthResponse } from '../models/auth-response.model';
import { User } from '../models/user.model';
import { ChangePasswordRequest } from '../models/change-password-request.model';
import { PasswordResetRequest } from '../models/password-reset-request.model';
import { SecurityQuestionRequest } from '../models/security-question-request.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;
  private userSubject = new BehaviorSubject<User | null>(null);
  user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, loginRequest)
      .pipe(
        tap(response => {
          if (response.success && response.token) {
            localStorage.setItem('token', response.token);
            this.getProfile().subscribe();
          }
        })
      );
  }

  logout(): Observable<AuthResponse> {
    const token = localStorage.getItem('token');
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/logout`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    }).pipe(
      tap(() => {
        localStorage.removeItem('token');
        this.userSubject.next(null);
      })
    );
  }

  getProfile(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/auth/profile`).pipe(
      tap(user => {
        console.log('Profile response:', user);
        this.userSubject.next(user);
      })
    );
  }

  changePassword(request: ChangePasswordRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/change-password`, request);
  }

  resetPassword(request: PasswordResetRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/reset-password`, request);
  }

  setSecurityQuestion(request: SecurityQuestionRequest): Observable<AuthResponse> {
    const token = localStorage.getItem('token');
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/set-security-question`, request, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }

  getSecurityQuestion(correoElectronico: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/security-question`, { correoElectronico });
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }
}
