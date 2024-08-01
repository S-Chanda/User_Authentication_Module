import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { ProfileComponent } from './user-settings/profile/profile.component';
import { UpdateProfileComponent } from './user-settings/update-profile/update-profile.component';
import { ChangePasswordComponent } from './user-settings/change-password/change-password.component';
import { HomeComponent } from './pages/home/home.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { authInterceptorProviders } from './services/auth.interceptor';
import { ClientHomeComponent } from './client/client-home/client-home.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ForgotPasswordComponent } from './user-settings/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './user-settings/reset-password/reset-password.component';
import { ClientDashboardComponent } from './client/client-dashboard/client-dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    SignupComponent,
    ProfileComponent,
    UpdateProfileComponent,
    ChangePasswordComponent,
    HomeComponent,
    ClientHomeComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent,
    ClientDashboardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule
    ],
  providers: [
    provideAnimationsAsync(),
    authInterceptorProviders
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
