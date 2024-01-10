package si.fri.rso.seznam.services.config;

import io.github.cdimascio.dotenv.Dotenv;
import si.fri.rso.seznam.services.beans.SeznamMetadataBean;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

@ApplicationScoped
public class AppConfig {
    private String koktejliServiceUrl;

    private Logger log = Logger.getLogger(AppConfig.class.getName());

    public AppConfig() {
        log.info("Poskusam prebrati okoljsko spremenljivko KOKTEJLI_SERVICE_URL");
        // Poskusite prebrati okoljsko spremenljivko
        koktejliServiceUrl = "http://koktejli-service:8080/v1/koktejli";
        log.info("Koktejli service URL: " + koktejliServiceUrl);
    }

    public String getKoktejliServiceUrl() {
        return koktejliServiceUrl;
    }
}
