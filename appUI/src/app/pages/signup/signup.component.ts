import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent {
  isMfaEnabled: boolean = false;
  confirmPassword = '';
  qrCode: any;

  public signupData = {
    username: '',
    email: '',
    password: '',
    mfaEnabled: false,
  };

  otpVerificationData = {
    email: '',
    code: '',
  };

  constructor(private userService: UserService, private router: Router) {}

  register() {
    this.userService.registerUser(this.signupData).subscribe(
      (res: any) => {
        alert(res.message);
        console.log(res);
        this.isMfaEnabled = res.data.mfaEnabled;
        this.qrCode = res.data.secretImageUri;

        this.otpVerificationData.email = this.signupData.email;
        if (!res.data.mfaEnabled) {
          this.router.navigateByUrl('/login');
        }
      },
      (error) => {
        alert('Error');
        console.log(error);
      }
    );
  }

  verifyOtp() {
    this.userService.verifyOtp(this.otpVerificationData).subscribe(
      (res) => {
        alert('Account created Successfully');
        console.log(res);
        this.router.navigateByUrl('/login');
      },
      (err) => {
        alert('Error');
        console.log(err);
      }
    );
    alert('Success');
  }
}
