package spotify_clone.demo.catalogcontext.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotify_clone.demo.catalogcontext.domain.SongContent;


public interface SongContentRepository extends JpaRepository<SongContent, Long> {
}
