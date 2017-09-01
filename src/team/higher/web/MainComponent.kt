package team.higher.web

import team.higher.web.runtime.module.AppModule
import team.higher.web.runtime.module.ClientModule
import team.higher.web.runtime.module.ResourceModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
  modules = arrayOf(
    AppModule::class,
    ClientModule::class,
    ResourceModule::class
  )
)
interface MainComponent {

  fun app(): WebApp
}
