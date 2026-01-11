package spotify_clone.demo.catalogcontext.domain;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "favorite_song")
@IdClass(FavoriteId.class) //clé primaire composée de plusieurs champs (songPublicId, userEmail)
public class Favorite implements Serializable { //unicité couple (song, user) - Un utilisateur peut mettre une chanson en favori une seule fois.

    @Id
    private UUID songPublicId; //id public de la chanson

    @Id
    @Column(name = "user_email")
    private String userEmail; //email utilisateur


    public UUID getSongPublicId() {
        return songPublicId;
    }

    public void setSongPublicId(UUID songPublicId) {
        this.songPublicId = songPublicId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
