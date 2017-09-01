package team.higher.web

import team.higher.web.runtime.config.ServerConfiguration
import team.higher.web.runtime.module.AppModule
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.views.ViewBundle

class MainApplication : Application<ServerConfiguration>(), MainComponent {

  private lateinit var component: MainComponent

  companion object {
    val instance = MainApplication()
    @JvmStatic
    fun main(args: Array<String>) {
      instance.run(*args)
    }
  }

  override fun initialize(bootstrap: Bootstrap<ServerConfiguration>) {
    bootstrap.addBundle(ViewBundle())
    bootstrap.addBundle(AssetsBundle("/assets", "/", "index.html"))
  }

  override fun run(config: ServerConfiguration, environment: Environment) {
    component = DaggerMainComponent
      .builder()
      .appModule(AppModule(config))
      .build()
    component.app().run(config, environment)
  }

  override fun app(): WebApp = component.app()
}
