import {Injectable} from '@angular/core';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/filter';
import {LISTOFAPK, TOP10APP} from '../testdata';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import * as util from 'util';
import {saveAs} from 'file-saver/FileSaver';
import {ApkView} from './apkview';


@Injectable()
export class ApkService {
  listOFApk: Array<ApkView>;
  top10app: Array<ApkView>;

  constructor(private http: HttpClient) {
    this.top10app = TOP10APP;
    this.listOFApk = LISTOFAPK;
  }

  downloadApk(apkId: number) {
    console.log('apkId: ' + apkId);
    const headers = new HttpHeaders();
    headers.append('Accept', 'application/zip');
    return this.http.get('apk/' + apkId, {
      headers: headers,
      observe: 'response',
      responseType: 'arraybuffer'
    }).subscribe(data => {
        this.saveToFileSystem(data);
      },
      err => {
        console.log('\n Something went wrong! ' + err.headers);
      }
    );
  }

  getTop10(): Array<ApkView> {
    console.log('get 10 top');
    const headers = new HttpHeaders();
    headers.append('Accept', 'application/json');
    const get10 = this.http.get('apk/get10top').subscribe(data => {
      console.log(util.inspect(data));
    });
    const result: Array<ApkView> = [];
    return result;
  }

  getByCategory(catName: string) {
    const headers = new HttpHeaders();
    headers.append('Accept', 'application/json');
    // let result: Array<ApkView>;
    return this.http.get('apk/category/' + catName);
      /*.subscribe(data => {
      // console.log(util.inspect(data));
      result = data as  Array<ApkView>;
      result.forEach(x => console.log(x));
    });*/
  }

  // upload file to server
  upload(formData: FormData) {
    const headers = new HttpHeaders();
    headers.append('Accept', 'application/json');
    return this.http.post('"apk/upload', formData, {headers: headers});
  }

  private saveToFileSystem(response) {
    const contentDispositionHeader: string = response.headers.get('Content-Disposition');
    const parts: string[] = contentDispositionHeader.split(';');
    const filename = parts[1].split('=')[1];
    console.log('file name is ' + filename);
    const blob = new Blob([response._body], {type: 'application/zip'});
    saveAs(blob, filename);
  }
}

