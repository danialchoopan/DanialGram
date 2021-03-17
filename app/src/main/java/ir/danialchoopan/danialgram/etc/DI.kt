package ir.danialchoopan.danialgram.etc

import ir.danialchoopan.danialgram.action.ImgDownloader
import org.koin.dsl.module

val appModules = module {
    single {
        ImgDownloader()
    }
}