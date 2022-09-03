package com.geancarloleiva.jetcomptrivia_13

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TriviaApplication: Application() {

    /*
    * Hilt configuration
    * 1. Add gradle project dependency, gradle module plugins and dependencies
    * 2. Create Application class and use @HiltAndroidApp on it (THIS)
    * 3. Update AndroidManifest and add android:name=".thisClassName"
    * 4. Update MainActivity class adding @AndroidEntryPoint
    * 5. Create "di" package to contain AppModule object class and fill the Providers
    * */
}