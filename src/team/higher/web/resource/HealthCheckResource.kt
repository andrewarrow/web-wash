package team.higher.web.resource

import com.codahale.metrics.health.HealthCheck

class HealthCheckResource : HealthCheck() {
  override fun check(): Result = Result.healthy()
}
