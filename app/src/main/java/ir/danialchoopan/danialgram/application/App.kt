package ir.danialchoopan.danialgram.application

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        startKoin {
//            androidContext(this@App)
//            modules(appModules)
//        }

    }
}