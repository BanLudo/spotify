package spotify_clone.demo.catalogcontext.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spotify_clone.demo.catalogcontext.application.dto.SaveSongDTO;
import spotify_clone.demo.catalogcontext.application.dto.SongContentDTO;
import spotify_clone.demo.catalogcontext.domain.SongContent;

@Mapper(componentModel="spring")
public interface SongContentMapper {

    @Mapping(source = "song.publicId", target = "publicId")
    SongContentDTO songContentToSongContentDTO(SongContent songContent);

    SongContent saveSongDTOToSong(SaveSongDTO saveSongDTO);
}
