package team.higher.web.cookie

@Retention(AnnotationRetention.RUNTIME)
@Target(
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER,
  AnnotationTarget.CLASS,
  AnnotationTarget.FILE
)
annotation class IgnoreAuthentication
