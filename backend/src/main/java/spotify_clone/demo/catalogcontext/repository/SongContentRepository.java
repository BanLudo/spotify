package spotify_clone.demo.catalogcontext.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotify_clone.demo.catalogcontext.domain.SongContent;

import java.util.Optional;
import java.util.UUID;


public interface SongContentRepository extends JpaRepository<SongContent, Long> {

    Optional<SongContent> findOneBySongPublicId(UUID publicId);
}
