package spotify_clone.demo.catalogcontext.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spotify_clone.demo.catalogcontext.application.dto.ReadSongInfoDTO;
import spotify_clone.demo.catalogcontext.application.dto.SaveSongDTO;
import spotify_clone.demo.catalogcontext.application.mapper.SongContentMapper;
import spotify_clone.demo.catalogcontext.application.mapper.SongMapper;
import spotify_clone.demo.catalogcontext.domain.Song;
import spotify_clone.demo.catalogcontext.domain.SongContent;
import spotify_clone.demo.catalogcontext.repository.SongContentRepository;
import spotify_clone.demo.catalogcontext.repository.SongRepository;

import java.util.List;

@Service
@Transactional
public class SongService {

    private final SongMapper songMapper;
    private final SongRepository songRepository;
    private final SongContentRepository songContentRepository;
    private final SongContentMapper songContentMapper;


    public SongService(SongMapper songMapper, SongRepository songRepository, SongContentRepository songContentRepository, SongContentMapper songContentMapper) {
        this.songMapper = songMapper;
        this.songRepository = songRepository;
        this.songContentRepository = songContentRepository;
        this.songContentMapper = songContentMapper;
    }

    public ReadSongInfoDTO create(SaveSongDTO saveSongDTO){
        Song song = songMapper.saveSongDTOToSong(saveSongDTO);
        Song songSaved = songRepository.save(song);

        SongContent songContent = songContentMapper.saveSongDTOToSong(saveSongDTO);
        songContent.setSong(songSaved);

        songContentRepository.save(songContent);
        return songMapper.songToReadSongInfoDTO(songSaved);
    }

    @Transactional
    public List<ReadSongInfoDTO> getAll(){
        return songRepository.findAll().stream()
                    .map(songMapper::songToReadSongInfoDTO)
                    .toList();
    }

}
