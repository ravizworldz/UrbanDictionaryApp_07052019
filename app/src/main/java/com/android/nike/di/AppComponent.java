package com.android.nike.di;
import com.android.nike.network.UrbanAPIService;

import dagger.Component;

@ApplicationScope
@Component(modules = {AppModule.class})
public interface AppComponent {
   UrbanAPIService provideUrbanAPIService();
}
