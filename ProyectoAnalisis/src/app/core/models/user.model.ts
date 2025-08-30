// src/app/core/models/user.model.ts
export interface User {
  idUsuario: string;
  nombre: string;
  correoElectronico: string;
  role: {
    nombre: string;
  };
}
