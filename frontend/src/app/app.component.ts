import {Component, OnInit} from '@angular/core';
import {MenuItem, Message} from 'primeng/primeng';
import {AuthService} from './auth/auth.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {User} from './auth/user.model';
import * as util from 'util';
import {MessageService} from 'primeng/components/common/messageservice';
import * as JwtDecode from 'jwt-decode';
import {ROLE_DEVELOPER} from './auth/auth.constant';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [AuthService, MessageService]
})
export class AppComponent implements OnInit {
  items: MenuItem[];
  displayRegister = false;
  displayLogin = false;
  msgs: Message[];
  loginForm: FormGroup;
  registerForm: FormGroup;
  userName = '';

  constructor(private  authservice: AuthService,
              private router: Router,
              private messageService: MessageService) {
  }

  ngOnInit() {
    this.items = [{
      label: 'Login', command: (event) => {
        this.displayLogin = true;
      }
    },
      {
        label: 'Register', command: (event) => {
        this.displayRegister = true;
      }
      }
    ];
    this.loginForm = new FormGroup({
      email: new FormControl(null, [
        Validators.required,
        Validators.pattern('[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*@(?:[a-z0-9]' +
          '(?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?')
      ]),
      password: new FormControl(null, Validators.required),
    });
    this.registerForm = new FormGroup({
      email: new FormControl(null, [
        Validators.required,
        Validators.pattern('[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*@(?:[a-z0-9]' +
          '(?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?')
      ]),
      password: new FormControl(null, Validators.required),
      name: new FormControl(null, Validators.required),
      isdeveloper: new FormControl(null, null)
    });
  }

  isLogged() {
    return this.authservice.isLogged();
  }

  isDeveloper() {
    return this.authservice.isDev();
  }

  logout() {
    this.authservice.logout();
    this.router.navigate(['/']);
  }

  showUpload() {
    this.router.navigateByUrl('/upload');
  }

  onRegister() {
    const user = new User(
      this.registerForm.value.email,
      this.registerForm.value.password,
      this.registerForm.value.isdeveloper,
      this.registerForm.value.name
    );
    user.developer = this.registerForm.value.isdeveloper ? this.registerForm.value.isdeveloper : false;
    console.log('user ' + util.inspect(user));
    this.authservice.register(user)
      .subscribe(
        data => {
          console.log('data on register ' + util.inspect(data));
        },
        error => {
          console.error('error in login submit');
          console.error(util.inspect(error));
        }
      );
    this.registerForm.reset();
    this.displayRegister = false;
  }

  onLogin() {
    const user: User = new User(this.loginForm.value.email, this.loginForm.value.password, false, '');
    this.authservice.login(user)
      .subscribe(
        data => {
          const token = data as TokenTest;
          const tokenContent: TokenContentTest = JwtDecode(token.access_token);
          this.authservice.isDeveloper = (tokenContent.authorities.indexOf(ROLE_DEVELOPER) !== -1);
          sessionStorage.setItem('token', token.access_token);
          this.userName = tokenContent.user_name;
          this.messageService.add({severity: 'success', summary: 'Welcome ' + this.userName});

        },
        error => {
          console.error('error in login submit');
          console.error(error);
          this.messageService.add({severity: 'error', summary: 'Could not login'});
        }
      );
    this.loginForm.reset();
    this.displayLogin = false;
    this.router.navigate(['/']);
  }

}

interface TokenTest {
  access_token: string;
  token_type: string;
  expires_in: number;
  scope: string;
  jti: string;
}

export interface TokenContentTest {
  aud: string[];
  user_name: string;
  scope: string[];
  exp: number;
  authorities: string[];
  jti: string;
  client_id: string;
}
