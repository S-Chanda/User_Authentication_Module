import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from '../services/login.service';

export const clientGuard: CanActivateFn = (route, state) => {
  let isLoggedIn = inject(LoginService).isLoggedIn();
  let role = inject(LoginService).getUserRole();

  let router = inject(Router);

  if (isLoggedIn && role == 'NORMAL') {
    return true;
  } else {
    router.navigate(['login']);
    return false;
  }
};
