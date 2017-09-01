package team.higher.web.runtime.module

import team.higher.web.runtime.config.ServerConfiguration
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Module
class AppModule(private val serverConfiguration: ServerConfiguration) {

  @Singleton
  @Provides
  fun provideServerConfiguration(): ServerConfiguration = serverConfiguration
}
