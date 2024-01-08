package si.fri.rso.seznam.services.config;

import io.github.cdimascio.dotenv.Dotenv;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppConfig {
    private String koktejliServiceUrl;

    public AppConfig() {
        Dotenv dotenv = Dotenv.configure()
                .directory("../../../.env")
                .load();
        koktejliServiceUrl = dotenv.get("KOKTEJLI_SERVICE_URL", "http://localhost:8080/v1/koktejli");
    }

    public String getKoktejliServiceUrl() {
        return koktejliServiceUrl;
    }
}
