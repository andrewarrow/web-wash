package team.higher.web.runtime.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import ch.qos.logback.core.util.FileSize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.AbstractAppenderFactory;
import io.dropwizard.logging.async.AsyncAppenderFactory;
import io.dropwizard.logging.filter.LevelFilterFactory;
import io.dropwizard.logging.layout.LayoutFactory;
import io.dropwizard.util.Size;
import io.dropwizard.validation.ValidationMethod;
import javax.validation.constraints.Min;

@JsonTypeName("filejson")
public class FileJsonAppenderFactory<E extends DeferredProcessingAware> extends AbstractAppenderFactory<E> {

  private String currentLogFilename;

  private boolean archive = true;

  private String archivedLogFilenamePattern;

  @Min(0)
  private int archivedFileCount = 5;

  private Size maxFileSize;

  @JsonProperty
  public String getCurrentLogFilename() {
    return currentLogFilename;
  }

  @JsonProperty
  public void setCurrentLogFilename(String currentLogFilename) {
    this.currentLogFilename = currentLogFilename;
  }

  @JsonProperty
  public boolean isArchive() {
    return archive;
  }

  @JsonProperty
  public void setArchive(boolean archive) {
    this.archive = archive;
  }

  @JsonProperty
  public String getArchivedLogFilenamePattern() {
    return archivedLogFilenamePattern;
  }

  @JsonProperty
  public void setArchivedLogFilenamePattern(String archivedLogFilenamePattern) {
    this.archivedLogFilenamePattern = archivedLogFilenamePattern;
  }

  @JsonProperty
  public int getArchivedFileCount() {
    return archivedFileCount;
  }

  @JsonProperty
  public void setArchivedFileCount(int archivedFileCount) {
    this.archivedFileCount = archivedFileCount;
  }

  @JsonProperty
  public Size getMaxFileSize() {
    return maxFileSize;
  }

  @JsonProperty
  public void setMaxFileSize(Size maxFileSize) {
    this.maxFileSize = maxFileSize;
  }

  @JsonIgnore
  @ValidationMethod(message = "must have archivedLogFilenamePattern if archive is true")
  public boolean isValidArchiveConfiguration() {
    return !archive || (archivedLogFilenamePattern != null);
  }

  @JsonIgnore
  @ValidationMethod(message = "when specifying maxFileSize, archivedLogFilenamePattern must contain %i")
  public boolean isValidForMaxFileSizeSetting() {
    return !archive || maxFileSize == null ||
      (archivedLogFilenamePattern != null && archivedLogFilenamePattern.contains("%i"));
  }

  @JsonIgnore
  @ValidationMethod(message = "when archivedLogFilenamePattern contains %i, maxFileSize must be specified")
  public boolean isMaxFileSizeSettingSpecified() {
    return !archive || !(archivedLogFilenamePattern != null && archivedLogFilenamePattern.contains("%i")) ||
      maxFileSize != null;
  }

  @JsonIgnore
  @ValidationMethod(message = "currentLogFilename can only be null when archiving is enabled")
  public boolean isValidFileConfiguration() {
    return archive || currentLogFilename != null;
  }

  @Override
  public Appender<E> build(LoggerContext context, String applicationName, LayoutFactory<E> layoutFactory,
                           LevelFilterFactory<E> levelFilterFactory, AsyncAppenderFactory<E> asyncAppenderFactory) {


    final FileAppender<E> appender = buildAppender(context);
    JsonLayout layout = new JsonLayout();
    layout.setJsonFormatter(new JacksonJsonFormatter());
    layout.setAppendLineSeparator(true);
    layout.setTimestampFormatTimezoneId("UTC");
    LayoutWrappingEncoder<E> layoutEncoder = new LayoutWrappingEncoder<>();
    layoutEncoder.setLayout((Layout<E>)layout);
    appender.setName("file-appender");
    appender.setAppend(true);
    appender.setContext(context);
    appender.setEncoder(layoutEncoder);
    appender.setPrudent(false);
    appender.addFilter(levelFilterFactory.build(threshold));
    getFilterFactories().forEach(f -> appender.addFilter(f.build()));
    appender.start();

    return wrapAsync(appender, asyncAppenderFactory);
  }

  protected FileAppender<E> buildAppender(LoggerContext context) {
    if (archive) {
      final RollingFileAppender<E> appender = new RollingFileAppender<>();
      appender.setFile(currentLogFilename);

      if (maxFileSize != null && !archivedLogFilenamePattern.contains("%d")) {
        final FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
        rollingPolicy.setContext(context);
        rollingPolicy.setMaxIndex(getArchivedFileCount());
        rollingPolicy.setFileNamePattern(getArchivedLogFilenamePattern());
        rollingPolicy.setParent(appender);
        rollingPolicy.start();
        appender.setRollingPolicy(rollingPolicy);

        final SizeBasedTriggeringPolicy<E> triggeringPolicy = new SizeBasedTriggeringPolicy<>();
        triggeringPolicy.setMaxFileSize(new FileSize(maxFileSize.toBytes()));
        triggeringPolicy.setContext(context);
        triggeringPolicy.start();
        appender.setTriggeringPolicy(triggeringPolicy);

        return appender;
      } else {
        final TimeBasedFileNamingAndTriggeringPolicy<E> triggeringPolicy;
        if (maxFileSize == null) {
          triggeringPolicy = new DefaultTimeBasedFileNamingAndTriggeringPolicy<>();
        } else {
          final SizeAndTimeBasedFNATP<E> maxFileSizeTriggeringPolicy = new SizeAndTimeBasedFNATP<>();
          maxFileSizeTriggeringPolicy.setMaxFileSize(new FileSize(maxFileSize.toBytes()));
          triggeringPolicy = maxFileSizeTriggeringPolicy;
        }
        triggeringPolicy.setContext(context);

        final TimeBasedRollingPolicy<E> rollingPolicy = new TimeBasedRollingPolicy<>();
        rollingPolicy.setContext(context);
        rollingPolicy.setFileNamePattern(archivedLogFilenamePattern);
        rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(
          triggeringPolicy);
        triggeringPolicy.setTimeBasedRollingPolicy(rollingPolicy);
        rollingPolicy.setMaxHistory(archivedFileCount);

        appender.setRollingPolicy(rollingPolicy);
        appender.setTriggeringPolicy(triggeringPolicy);

        rollingPolicy.setParent(appender);
        rollingPolicy.start();
        return appender;
      }
    }

    final FileAppender<E> appender = new FileAppender<>();
    appender.setFile(currentLogFilename);
    return appender;
  }
}
