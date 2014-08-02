package net.and0.metarogue.controller.ruby;

import net.and0.metarogue.main.Main;
import net.and0.metarogue.util.FileUtil;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;

import java.io.IOException;

/**
 * MetaRogue class RubyContainer
 * User: andrew
 * Date: 5/16/13
 * Time: 4:50 PM
 */
public class RubyContainer {

    //static String rubyDir = "C:/metarogue/rb/";
    static String rubyDir = System.getProperty("user.dir") + "\\rb\\default";
    public static ScriptingContainer container;
    public static Object receiver;

    public RubyContainer() {
        container = new ScriptingContainer(LocalContextScope.SINGLETON, LocalVariableBehavior.PERSISTENT);
        container.put("message", System.getProperty("user.dir") + "\\lib\\default");
        container.put("$world", Main.gameClient.getActiveWorld());
        String script = null;
        try {
            script = FileUtil.readFile(rubyDir + "/main.rb");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        receiver = container.runScriptlet(script);
        // container.callMethod(receiver, "update", Object.class);
    }

}
