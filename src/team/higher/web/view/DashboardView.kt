package team.higher.web.view

import io.dropwizard.views.View

open class DashboardView(
  val key: String = "hi"
) : BaseView("Dashboard.ftl")
