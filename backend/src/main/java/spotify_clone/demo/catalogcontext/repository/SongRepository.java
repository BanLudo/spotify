package spotify_clone.demo.catalogcontext.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotify_clone.demo.catalogcontext.domain.Song;

public interface SongRepository extends JpaRepository<Song, Long> {

}
