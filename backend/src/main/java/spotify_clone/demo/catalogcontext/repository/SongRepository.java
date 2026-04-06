package spotify_clone.demo.catalogcontext.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spotify_clone.demo.catalogcontext.domain.Song;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("select s from Song s where lower(s.title) like lower(concat('%',:searchTerm,'%'))" +
           "or lower(s.author) like lower(concat('%',:searchTerm,'%'))")
    List<Song> findByTitleOrAuthorContaining(String searchTerm);
}
