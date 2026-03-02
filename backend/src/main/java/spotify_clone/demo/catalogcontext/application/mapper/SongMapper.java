package spotify_clone.demo.catalogcontext.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spotify_clone.demo.catalogcontext.application.dto.ReadSongInfoDTO;
import spotify_clone.demo.catalogcontext.application.dto.SaveSongDTO;
import spotify_clone.demo.catalogcontext.application.vo.SongAuthorVO;
import spotify_clone.demo.catalogcontext.application.vo.SongTitleVO;
import spotify_clone.demo.catalogcontext.domain.Song;

@Mapper(componentModel="spring")
public interface SongMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    Song saveSongDTOToSong(SaveSongDTO saveSongDTO);

    @Mapping(target = "favorite", ignore = true)
    ReadSongInfoDTO songToReadSongInfoDTO(Song song);

    default SongTitleVO stringToSongTitleVO(String title) {
        return new SongTitleVO(title);
    }

    default SongAuthorVO stringToSongAuthorVO(String author) {
        return new SongAuthorVO(author);
    }

    default String songTitleVOToString(SongTitleVO title) {
        return title.value();
    }

    default String  songAuthorVOToString(SongAuthorVO author) {
        return author.value();
    }

}
