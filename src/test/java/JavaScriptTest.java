import static org.junit.jupiter.api.Assertions.assertEquals;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import java.util.Map;
import javax.script.ScriptException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;

public class JavaScriptTest {

  @Test
  void readJsonArray() {

    try(Context context = Context.newBuilder("js")
        .build()) {
      // language=javascript
      Value value = context.eval("js", """
          [1,2,3]
          """);

      assertEquals(3, value.getArraySize());
      assertEquals(1, value.getArrayElement(0).asInt());
      assertEquals(2, value.getArrayElement(1).asInt());
      assertEquals(3, value.getArrayElement(2).asInt());

    }

  }

  @Test
  void readJsonObject() {

    try(Context context = Context.newBuilder("js")
        .build()) {

      // language=javascript
      Value value = context.eval("js", """
          ({
            "message": "Hello, world!"
          })
          """);

      assertEquals("Hello, world!", value.getMember("message").asString());

    }

  }

  @Test
  void invokeFunction_NoParameter_ReturnString() throws ScriptException, NoSuchMethodException {

    try(GraalJSScriptEngine engine = GraalJSScriptEngine.create(null, Context.newBuilder("js")
        .allowHostAccess(HostAccess.ALL)
        .allowHostClassLookup(s -> true)
        .option("js.ecmascript-version", "2021"))) {

      // language=javascript
      engine.eval("""
          function getMessage() {
            return "Hello, world!"
          }
          """);

      Object value = engine.invokeFunction("getMessage");
      assertEquals("Hello, world!", value);

    }

  }

  @Test
  void invokeFunction_OneParameter_ReturnString() throws ScriptException, NoSuchMethodException {

    try(GraalJSScriptEngine engine = GraalJSScriptEngine.create(null, Context.newBuilder("js")
        .allowHostAccess(HostAccess.ALL)
        .allowHostClassLookup(s -> true)
        .option("js.ecmascript-version", "2021"))) {

      // language=javascript
      engine.eval("""
          function getMessage(name) {
            return `Hello, ${name}!`
          }
          """);

      Object value = engine.invokeFunction("getMessage", "Bob");
      assertEquals("Hello, Bob!", value);

    }

  }


  @Test
  void invokeFunction_NoParameter_ReturnObject() throws ScriptException, NoSuchMethodException {

    try(GraalJSScriptEngine engine = GraalJSScriptEngine.create(null, Context.newBuilder("js")
        .allowHostAccess(HostAccess.ALL)
        .allowHostClassLookup(s -> true)
        .option("js.ecmascript-version", "2021"))) {

      // language=javascript
      engine.eval("""
          function getObject() {
            return {
              "message": "Hello, world!"
            }
          }
          """);

      Object value = engine.invokeFunction("getObject");
      Map obj = (Map) value;

      assertEquals("Hello, world!", obj.get("message"));
    }

  }

}
