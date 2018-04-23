import {Component, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {FileUpload, SelectItem} from 'primeng/primeng';
import {CATEGORIES} from '../categories.properties';
import {MessageService} from 'primeng/components/common/messageservice';


@Component({
  selector: 'app-upload-apk',
  templateUrl: './upload-apk.component.html',
  styleUrls: ['./upload-apk.component.css'],
  providers: [MessageService]
})
export class UploadApkComponent implements OnInit {
  uploadform: FormGroup;
  description: string;
  categories: SelectItem[];
  @ViewChild('fileInput') fileInput: FileUpload;

  constructor(private mesService: MessageService) {
  }

  ngOnInit() {
    this.uploadform = new FormGroup({
      app_name: new FormControl(null, [
        Validators.required,
      ]),
      description: new FormControl(null, Validators.required),
      category: new FormControl(null, Validators.required),
    });
    this.categories = [];
    this.categories.push({label: 'Select category', value: ''});
    CATEGORIES.map(x =>
      this.categories.push({label: x.label, value: x.label}));
  }

  onBeforeSend($event: any) {
    $event.formData.append('name', this.uploadform.value.app_name);
    $event.formData.append('description', this.uploadform.value.description);
    $event.formData.append('category', this.uploadform.value.category);
    this.mesService.add({severity: 'success', summary: 'Service Message', detail: 'onBeforeSend'});
  }
}
