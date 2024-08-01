import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css',
})
export class ForgotPasswordComponent {
  myForm: FormGroup;

  constructor(private fb: FormBuilder, private userService: UserService) {
    this.myForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  sendResetLink() {
    this.userService.forgotPassword(this.myForm.value).subscribe(
      (res: any) => {
        alert(res.message);
        console.log(this.myForm.value);
        console.log(res);
      },
      (err) => {
        alert('Error');
        console.log(err);
      }
    );
  }
}
