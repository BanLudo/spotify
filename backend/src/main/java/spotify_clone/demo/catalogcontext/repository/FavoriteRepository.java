package spotify_clone.demo.catalogcontext.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotify_clone.demo.catalogcontext.domain.Favorite;
import spotify_clone.demo.catalogcontext.domain.FavoriteId;

import java.util.List;
import java.util.UUID;


public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    List<Favorite> findAllByUserEmailAndSongPublicIdIn(String email, List<UUID> songPublicIds);
}
