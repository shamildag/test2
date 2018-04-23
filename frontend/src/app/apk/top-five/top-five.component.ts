import {Component, OnInit} from '@angular/core';
import {ApkService} from '../apk.service';
import {ApkView} from '../apkview';

@Component({
  selector: 'app-top-five',
  templateUrl: './top-five.component.html',
  styleUrls: ['./top-five.component.css']
})
export class TopFiveComponent implements OnInit {
  public top10app: ApkView[];

  constructor(private apkService: ApkService) {
    this.top10app = this.apkService.top10app;
  }

// todo subscribe to new top 5

  ngOnInit() {
  }
}
