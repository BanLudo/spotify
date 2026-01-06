package spotify_clone.demo.catalogcontext.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotify_clone.demo.catalogcontext.domain.Favorite;
import spotify_clone.demo.catalogcontext.domain.FavoriteId;


public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
}
