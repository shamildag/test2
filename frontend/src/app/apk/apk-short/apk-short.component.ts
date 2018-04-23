import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import * as util from 'util';
import {ApkService} from '../apk.service';
import {AuthService} from '../../auth/auth.service';
import {MessageService} from 'primeng/components/common/messageservice';
import {ApkView} from '../apkview';

@Component({
  selector: 'app-apk-short',
  templateUrl: './apk-short.component.html',
  styleUrls: ['./apk-short.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ApkShortComponent implements OnInit {

  @Input() viewItem: ApkView;
  display = false;

  constructor(private apkService: ApkService,
              private authService: AuthService,
              private msg: MessageService) {
  }

  ngOnInit() {
  }

  showDialog() {
    this.display = true;
  }

  downloadApk(id: number) {
    if (this.authService.isLogged()) {
      this.display = true;
    } else {
      this.msg.clear();
      this.msg.add({severity: 'error', summary: 'Please login', detail: 'Download ability only for logged in users '});
    }
    this.apkService.downloadApk(id);
  }

}
