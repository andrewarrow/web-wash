package team.higher.web.view

import io.dropwizard.views.View

open class BaseView : View {

  var who = ""
  var mobile = true

  constructor(template: String): super(template)
}
