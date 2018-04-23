import {Injectable} from '@angular/core';
import {User} from './user.model';
import {OAUTH2_PATH, ROLE_DEVELOPER, TOKEN_AUTH_PASSWORD, TOKEN_AUTH_USERNAME} from './auth.constant';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable()
export class AuthService {
  private _isDeveloper = false;

  constructor(private http: HttpClient) {
  }


  isLogged() {
    return sessionStorage.getItem('token') !== null;
  }

  isDev() {
    return this._isDeveloper;
  }

  login(user: User) {
    const body = `username=${encodeURIComponent(user.email)}&password=${encodeURIComponent(user.password)}&grant_type=password`;

    const headers2 = new HttpHeaders()
      .set('Content-Type', 'application/x-www-form-urlencoded')
      .set('Authorization', 'Basic ' + btoa(TOKEN_AUTH_USERNAME + ':' + TOKEN_AUTH_PASSWORD));

    return this.http.post('http://localhost:8080/oauth/token', body,
      {
        headers: headers2
      });

  }

  register(user: User) {
    const body = JSON.stringify(user);
    const headers = new HttpHeaders({'Content-Type': 'application/json'});
    headers.append('Authorization', 'Basic ' + btoa(TOKEN_AUTH_USERNAME + ':' + TOKEN_AUTH_PASSWORD));
    return this.http.post('/user/register', body, {headers: headers});
  }

  logout() {
    sessionStorage.clear();
  }

  set isDeveloper(value: boolean) {
    this._isDeveloper = value;
  }
}


