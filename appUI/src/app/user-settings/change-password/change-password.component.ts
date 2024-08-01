import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css',
})
export class ChangePasswordComponent implements OnInit {
  myForm!: FormGroup;
  id: any;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.myForm = this.fb.group({
      oldPassword: ['', [Validators.required, Validators.minLength(6)]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
    });
    this.id = this.route.snapshot.params['id'];
  }

  changePassword(form: FormGroup) {
    console.log(form.value);
    this.userService.changePassword(this.id, form.value).subscribe(
      (res: any) => {
        console.log(res);
        alert(res.message);
      },
      (err) => {
        console.log(err);
        alert('Error');
      }
    );
  }
}
