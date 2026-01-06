package spotify_clone.demo.catalogcontext.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import spotify_clone.demo.catalogcontext.application.vo.SongAuthorVO;
import spotify_clone.demo.catalogcontext.application.vo.SongTitleVO;

public record SaveSongDTO(@Valid SongTitleVO songTitleVO,
                          @Valid SongAuthorVO songAuthorVO,
                          @NotNull byte[] cover,
                          @NotNull String coverContentType,
                          @NotNull byte[] file,
                          @NotNull String fileContentType) {
}
