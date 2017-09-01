package team.higher.web.runtime.module

import team.higher.web.client.*
import team.higher.web.resource.*
import team.higher.web.runtime.config.ServerConfiguration
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Module(includes = arrayOf(
  ClientModule::class
))
class ResourceModule {

  @Provides
  @Singleton
  fun provideUserInModelFilter(): UserInModelFilter
    = UserInModelFilter()

  @Provides
  @Singleton
  fun provideUserResource(client: UserClient, washClient: WashClient): UserResource
    = UserResource(client, washClient)

  @Provides
  @Singleton
  fun provideDashboardResource(client: UserClient): DashboardResource
    = DashboardResource(client)


  @Provides
  @Singleton
  fun provideWelcomeResource(): WelcomeResource
    = WelcomeResource()
}
