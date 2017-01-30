package com.OpenTasks;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.runemate.game.api.hybrid.util.Resources;
import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by VTM on 4/7/2016.
 */
public class Logger {

  /* ENUMS */

  public enum Level {
    All(0), DEBUG(10), INFO(20), WARNING(30), ERROR(40), CRITICAL(50), DISABLED(60);

    public final int level;

    Level(int level) {
      this.level = level;
    }

    @Override
    public String toString() {
      return "Level { " + this.name() + " }";
    }
  }


  /* CLASSES */

  public static class LoggerCustomRule {
    public final Level level;
    private Matcher matcher;

    public LoggerCustomRule(String rule, Level level) {
      this.matcher = Pattern.compile(prepareCustomRuleString(rule)).matcher("");
      this.level = level;
    }

    public boolean match(String canonicalPath) {
      matcher.reset(canonicalPath);
      return matcher.matches();
    }

    public static String prepareCustomRuleString(String rule) {
      // Input Example:
      // - "com.OpenTasks" => "com\.OpenTasks(?>\z|:.*)"
      // - "java.util.*" => "java\.util.*"
      // - "com.TheVTM.RainMaker.RainMaker:onLoop" => "com\.TheVTM\.RainMaker\.RainMaker:onLoop"

      String preparedRule = rule;

      // 1. Replace dots(".") in rules to "\."
      final String REGEX_DOT_RULE = "\\.(?=[^\\*])";
      String REGEX_DOT_REPLACEMENT = "\\\\.";

      Pattern pattern_dot = Pattern.compile(REGEX_DOT_RULE);
      Matcher matcher_dot = pattern_dot.matcher(preparedRule);
      preparedRule = matcher_dot.replaceAll(REGEX_DOT_REPLACEMENT);

      // 2. Replaces the ends of the string when its preceded by a dot followed by a word (ex ".Foo") with "(?>\z|:.*)"
      // "(?>\z|:.*)" is required for the rule that points to a class match its methods like the following example
      // Rule ("com.OpenTasks") to match CanonincalPath ("com.OpenTasks:onLoop")
      final String REGEX_END_RULE = "(?<=\\.\\w{1,32})\\z";
      final String REGEX_END_REPLACEMENT = "(?>\\\\z|:.*)";

      Pattern pattern_end = Pattern.compile(REGEX_END_RULE);
      Matcher matcher_end = pattern_end.matcher(preparedRule);
      preparedRule = matcher_end.replaceAll(REGEX_END_REPLACEMENT);

      return preparedRule;
    }

    @Override
    public String toString() {
      return "LoggerCustomRule{" +
          "level=" + level +
          ", matcher=" + matcher +
          '}';
    }
  }

  public static class LevelJsonDeserializer implements JsonDeserializer<Level> {
    @Override
    public Level deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      // 1. jsonElement should be a string
      String levelAsString = jsonElement.getAsString().toUpperCase();

      // 2. String => Level
      switch (levelAsString) {
        case "ALL":
          return Level.All;

        case "DEBUG":
          return Level.DEBUG;

        case "INFO":
          return Level.INFO;

        case "WARNING":
          return Level.WARNING;

        case "ERROR":
          return Level.ERROR;

        case "CRITICAL":
          return Level.CRITICAL;

        case "DISABLED":
          return Level.DISABLED;

        default:
          throw new JsonParseException(String.format("\"%s\" is not a valid log level.", levelAsString));
      }
    }
  }

  public static class LoggerJsonDeserializer implements JsonDeserializer<Logger> {
    @Override
    public Logger deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jDC) throws JsonParseException {
      // 1.
      JsonObject obj = jsonElement.getAsJsonObject();

      // 2. Parse level
      JsonElement jsonLevel = obj.get("level");
      Level level = jDC.deserialize(jsonLevel, Level.class);

      // 3. Parse customRules

      // 3.1 Parse disableCustomRules
      boolean disableCustomRules = false; // Default value
      JsonElement jsonDisableCustomRule  = obj.get("disableCustomRules");

      if (jsonDisableCustomRule.isJsonNull() == false) {
        disableCustomRules = jsonDisableCustomRule.getAsBoolean();
      }

      // 3.2 Parse customRules
      LoggerCustomRule[] customRules = null; // Default value
      JsonObject jsonCustomRules = obj.get("customRules").getAsJsonObject();

      if (jsonCustomRules.isJsonNull() == false && disableCustomRules == false) {
        JsonObject objCustomRules = jsonCustomRules.getAsJsonObject();

        // 3.3 Iterate over Json Object entries and create the LoggerCustomRules[]
        // Set<Map.Entry<String, JsonElement>> => LoggerCustomRule[]
        customRules = objCustomRules.entrySet().stream()

            // 3.3.1 Map.Entry<String, JsonElement> => LoggerCustomRule
            .map(e -> new LoggerCustomRule(e.getKey(), jDC.deserialize(e.getValue(), Level.class)))

            // 3.3.2
            .toArray(LoggerCustomRule[]::new);
      }

      return new Logger(level, customRules);
    }
  }

  /* FIELDS */

  @NotNull
  private final Level level;

  private final LoggerCustomRule[] customRules;
//  private Map<String, Level> customRulesCache;


  /* METHODS */

  public Logger() {
    this(Level.INFO);
  }

  public Logger(Level level) {
    this(level, null);
  }

  public Logger(Level level, LoggerCustomRule[] customRules) {
    this.customRules = customRules;
    this.level = level;
  }

  public Level getLevel() {
    return level;
  }

  @Override
  public String toString() {
    return "Logger{" +
        "level=" + level +
        ", customRules=" + Arrays.toString(customRules) +
        '}';
  }

  /* STATIC METHODS */

  public static Logger fromJson(String jsonPath) {
    String jsonIStream;

    try {
      jsonIStream = Resources.getAsString(jsonPath, Charset.defaultCharset());
    } catch (IOException e) {
      throw new RuntimeException(String.format("Failed to read log config file at \"%f\"", jsonPath), e);
    }

    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Level.class, new LevelJsonDeserializer());
    gsonBuilder.registerTypeAdapter(Logger.class, new LoggerJsonDeserializer());
    Gson gson = gsonBuilder.create();

    return  gson.fromJson(String.valueOf(jsonIStream), Logger.class);
  }

  public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  private static void _log(Level level, String message, Exception exception) {
    if (!shouldPrint(level)) return;

    _log(level, message, exception, 4);
  }

  private static void _log(Level level, Exception exception, String format, Object... args) {
    if (!shouldPrint(level)) return;

    String message = String.format(format, args);
    _log(level, message, exception, 4);
  }

  private static void _log(Level level, String message, Exception exception, int stackTraceIndex) {
    // 1. Get current timestamp
    String timestamp = dateFormat.format(new Date());

    // 2. Get caller method name and class
    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
    StackTraceElement e = stacktrace[stackTraceIndex];
    String className = e.getClassName();
    String methodName = e.getMethodName();

    // 3. Print out
    System.out.println(String.format("%s %s:%s", timestamp, className, methodName));
    System.out.println(String.format("[%s] %s", level.name(), message));
    if (exception != null) {
      exception.printStackTrace();
    }
  }

  private static boolean shouldPrint(Level logLevel) {
    // 1.
    Logger logger = TaskBot.GetInstance().getLogger();

    // 2. Custom Rules
    if (logger.customRules != null) {
      // 2.1 Get canonical path
      final int STACK_TRACE_INDEX = 4;
      StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
      StackTraceElement e = stacktrace[STACK_TRACE_INDEX];
      String canonicalPath = String.format("%s:%s", e.getClassName(), e.getMethodName());

      // 2.2 Find a custom rule that matches the *canonicalPath*
      return shouldPrint(logLevel, logger.level, logger.customRules, canonicalPath);
    }

    // 3. Check if level is high enough to print
    return logLevel.ordinal() >= logger.level.ordinal();
  }

  public static boolean shouldPrint(Level logLevel, Level defaultLevel, LoggerCustomRule rules[], String canonicalPath) {
    // 1. Find a custom rule that matches the *canonicalPath*
    Optional<LoggerCustomRule> rule = Arrays.stream(rules).filter(r -> r.match(canonicalPath)).findFirst();

    // 2. If found compare it to logLevel
    if (rule.isPresent()) {
      return logLevel.ordinal() >= rule.get().level.ordinal();
    }

    // 3. Else compare it to the defaultLevel
    return logLevel.ordinal() >= defaultLevel.ordinal();
  }

  /* LOG */

  public static void log(Level level, String message) {
    _log(level, message, null);
  }

  public static void log(Level level, String format, Object... args) {
    _log(level, null, format, args);
  }

  public static void log(Level level, String message, Exception e) {
    _log(level, message, e);
  }

  public static void log(Level level, Exception e, String format, Object... args) {
    _log(level, e, format, args);
  }

  /* DEBUG */

  public static void debug(String message) {
    _log(Level.DEBUG, message, null);
  }

  public static void debug(String format, Object... args) {
    _log(Level.DEBUG, null, format, args);
  }

  /* INFO */

  public static void info(String message) {
    _log(Level.INFO, message, null);
  }

  public static void info(String format, Object... args) {
    _log(Level.INFO, null, format, args);
  }

  /* WARNING */

  public static void warning(String message) {
    _log(Level.WARNING, message, null);
  }

  public static void warning(String format, Object... args) {
    _log(Level.WARNING, null, format, args);
  }

  /* ERROR */

  public static void error(String message) {
    _log(Level.ERROR, message, null);
  }

  public static void error(String format, Object... args) {
    _log(Level.ERROR, null, format, args);
  }

  public static void error(String message, Exception e) {
    _log(Level.ERROR, message, e);
  }

  public static void error(Exception e, String format, Object... args) {
    _log(Level.ERROR, e, format, args);
  }

  /* CRITICAL */

  public static void critical(String message) {
    _log(Level.CRITICAL, message, null);
  }

  public static void critical(String format, Object... args) {
    _log(Level.CRITICAL, null, format, args);
  }

  public static void critical(String message, Exception e) {
    _log(Level.CRITICAL, message, e);
  }

  public static void critical(Exception e, String format, Object... args) {
    _log(Level.CRITICAL, e, format, args);
  }
}
