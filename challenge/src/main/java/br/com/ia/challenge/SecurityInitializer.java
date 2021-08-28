package br.com.ia.challenge;

import br.com.ia.challenge.config.Config;
import br.com.ia.challenge.config.SecurityConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

    public SecurityInitializer() {
        super(SecurityConfig.class, Config.class);
    }

}
