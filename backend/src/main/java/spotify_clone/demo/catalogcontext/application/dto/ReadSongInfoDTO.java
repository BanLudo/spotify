package spotify_clone.demo.catalogcontext.application.dto;

import jakarta.validation.constraints.NotNull;
import spotify_clone.demo.catalogcontext.application.vo.SongAuthorVO;
import spotify_clone.demo.catalogcontext.application.vo.SongTitleVO;

import java.util.UUID;

public class ReadSongInfoDTO {

    private SongTitleVO title;
    private SongAuthorVO author;

    @NotNull
    private byte[] cover;

    @NotNull
    private String coverContentType;

    @NotNull
    private boolean isFavorite;

    @NotNull
    private UUID publicId;

    public SongTitleVO getTitle() {
        return title;
    }

    public void setTitle(SongTitleVO title) {
        this.title = title;
    }

    public SongAuthorVO getAuthor() {
        return author;
    }

    public void setAuthor(SongAuthorVO author) {
        this.author = author;
    }

    @NotNull
    public byte[] getCover() {
        return cover;
    }

    public void setCover(@NotNull byte[] cover) {
        this.cover = cover;
    }

    public @NotNull String getCoverContentType() {
        return coverContentType;
    }

    public void setCoverContentType(@NotNull String coverContentType) {
        this.coverContentType = coverContentType;
    }

    @NotNull
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(@NotNull boolean favorite) {
        isFavorite = favorite;
    }

    public @NotNull UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(@NotNull UUID publicId) {
        this.publicId = publicId;
    }
}
