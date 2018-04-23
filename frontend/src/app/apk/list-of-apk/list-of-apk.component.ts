import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {ApkService} from '../apk.service';
import {Subscription} from 'rxjs/Subscription';
import {ApkView} from "../apkview";

@Component({
  selector: 'app-list-of-apk',
  templateUrl: './list-of-apk.component.html',
  styleUrls: ['./list-of-apk.component.css']
})
export class ListOfApkComponent implements OnInit {
  private category: string;
  private listOfViews: ApkView[];
  @Input() view: ApkView;
  subscription: Subscription;

  constructor(private  route: ActivatedRoute, private apkService: ApkService) {
  }

  ngOnInit() {
    this.category = this.route.snapshot.params['catName'];

    this.subscription = this.route.params.subscribe((params: Params) => {
      this.category = params['catName'];
      this.apkService.getByCategory(this.category).subscribe(data => {
        // console.log(util.inspect(data));
        this.listOfViews = data as  Array<ApkView>;
        this.listOfViews.forEach((x) => {
          x.image128 = 'apk/' + x.id + '/image128';
          x.image512 = 'apk/' + x.id + '/image512';
        });
        this.listOfViews.forEach(x => console.log(x));
      });
    });
  }

}

