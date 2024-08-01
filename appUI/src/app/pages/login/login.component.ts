import { Component } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  qrCode: any;
  isMfaEnabled: boolean = false;

  loginData = {
    username: '',
    password: '',
  };

  otpVerificationData = {
    email: '',
    code: '',
  };

  constructor(
    private loginService: LoginService,
    private router: Router,
    private userService: UserService
  ) {}

  login() {
    this.loginService.generateToken(this.loginData).subscribe(
      (res: any) => {
        console.log(res);

        // set data in localStorage for login

        this.loginService.loginUser(res.jwtToken);
        this.loginService.getCurrentUser().subscribe(
          (res: any) => {
            this.loginService.setUserInLocalStorage(res.data);
            console.log(res.data);
            // setting data for otpValidation
            this.otpVerificationData.email = res.data.email;
            this.isMfaEnabled = res.data.mfaEnabled;
            this.userService
              .getQrForOtp(res.data.userID)
              .subscribe((res: any) => {
                this.qrCode = res.data;
              });

            // redirect
            if (!res.data.mfaEnabled) {
              this.redirectUser();
            }
          },
          (err: any) => {
            console.log(err);
          }
        );
      },
      (err: any) => {
        alert('Invalid Details');
        console.log(err);
      }
    );
  }

  verifyOtp() {
    this.userService.verifyOtp(this.otpVerificationData).subscribe(
      (res) => {
        alert('Account verified successfully !');
        console.log(res);
        this.redirectUser();
      },
      (err) => {
        alert('Error');
        console.log(err);
      }
    );
  }

  redirectUser() {
    let role = this.loginService.getUserRole();
    if (role == 'NORMAL') {
      this.router.navigateByUrl('/client/client-home');
      this.loginService.loginStatusSubject.next(true);
    } else {
      this.loginService.logout();
    }
  }
}
