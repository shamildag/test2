import {Injectable} from '@angular/core';
import {HttpInterceptor, HttpHandler, HttpRequest, HttpEvent} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import * as util from 'util';

@Injectable()
export class InterceptorService implements HttpInterceptor {
  constructor() {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {


    // const reqHeader = req.clone({headers: req.headers.set('Authorization', 'MyAuthToken')});
    console.log('Authorization  === ' + req.headers.get('Authorization'));
    const reqHeader2 = req.headers.append('shama', 'DUB');
    console.log('interceptor : \n ' + util.inspect(reqHeader2));
    return next.handle(req);
  }

}
