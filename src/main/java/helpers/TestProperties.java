package helpers;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties",
        "system:env",
        "file:src/test/resources/tests.properties"
})
public interface TestProperties extends Config {
    @Config.Key("chrome.driver")
    String chromeDriver();

    @Config.Key("user.data.dir")
    String userDataDir();

    @Config.Key("timeout.sec")
    String timeoutSec();

    @Config.Key("market.url")
    String marketUrl();

    @Config.Key("market.title")
    String marketTitle();
}
