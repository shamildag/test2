import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {
  ButtonModule,
  CarouselModule, CheckboxModule, DataListModule, DialogModule, FileUploadModule, GrowlModule, MenuModule,
  SplitButtonModule,
  ToolbarModule,
  TooltipModule, RatingModule, DropdownModule, PanelModule
} from 'primeng/primeng';

import {AppComponent} from './app.component';
import {RouterModule, Routes} from '@angular/router';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UploadApkComponent} from './apk/upload-apk/upload-apk.component';
import {TopFiveComponent} from './apk/top-five/top-five.component';
import {CategoriesComponent} from './apk/categories/categories.component';
import {ListOfApkComponent} from './apk/list-of-apk/list-of-apk.component';
import {AuthService} from './auth/auth.service';
import {ApkService} from './apk/apk.service';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';
import {ApkShortComponent} from './apk/apk-short/apk-short.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {InterceptorService} from './interceptor.service';

const appRoutes: Routes = [
  {path: '', redirectTo: '/categories', pathMatch: 'full'},
  {path: '#', redirectTo: '/categories', pathMatch: 'full'},
  {path: 'upload', component: UploadApkComponent},
  {path: 'topfive', component: TopFiveComponent},
  {path: 'categories', component: CategoriesComponent, children: [{path: ':catName', component: ListOfApkComponent}]},
];

@NgModule({
  declarations: [
    AppComponent,
    ListOfApkComponent,
    UploadApkComponent,
    TopFiveComponent,
    CategoriesComponent,
    ApkShortComponent

  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    ToolbarModule,
    SplitButtonModule,
    ButtonModule,
    MenuModule,
    CarouselModule,
    DataListModule,
    TooltipModule,
    DialogModule,
    GrowlModule,
    FileUploadModule,
    CheckboxModule,
    RatingModule,
    DropdownModule,
    PanelModule,
    HttpClientModule
  ],
  providers: [
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    {provide: HTTP_INTERCEPTORS, useClass: InterceptorService, multi: true},
    AuthService,
    ApkService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
