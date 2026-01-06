package spotify_clone.demo.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"spotify_clone.demo.usercontext.repository",
                        "spotify_clone.demo.catalogcontext.repository"}) //creation bean repository.. plus besoin de @repository
@EnableTransactionManagement
@EnableJpaAuditing
public class DatabaseConfiguration {

}
