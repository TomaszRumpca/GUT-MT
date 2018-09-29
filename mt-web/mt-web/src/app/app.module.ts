import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {AvailableForecastsListComponent} from "./available.forecasts.list.component";
import {AgmCoreModule, GoogleMapsAPIWrapper} from "@agm/core";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    AvailableForecastsListComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AgmCoreModule.forRoot({
      // apiKey: 'AIzaSyBkqeT1Cw4I_IO8hMX8F-i0OJAcTaVQbok'
      apiKey: 'AIzaSyDCKYgsbrTnuVLhkdloGxHeaAnjvvAF2Rw'
    })
  ],
  providers: [GoogleMapsAPIWrapper],
  bootstrap: [AppComponent]
})
export class AppModule {
}
