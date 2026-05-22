package spotify_clone.demo.catalogcontext.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spotify_clone.demo.catalogcontext.application.SongService;
import spotify_clone.demo.catalogcontext.application.dto.FavoriteSongDTO;
import spotify_clone.demo.catalogcontext.application.dto.ReadSongInfoDTO;
import spotify_clone.demo.catalogcontext.application.dto.SaveSongDTO;
import spotify_clone.demo.catalogcontext.application.dto.SongContentDTO;
import spotify_clone.demo.catalogcontext.domain.Favorite;
import spotify_clone.demo.infrastructure.service.dto.State;
import spotify_clone.demo.infrastructure.service.dto.StatusNotification;
import spotify_clone.demo.usercontext.ReadUserDTO;
import spotify_clone.demo.usercontext.application.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SongRessource {
    private final SongService  songService;
    private final Validator validator;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SongRessource(SongService songService, Validator validator, UserService userService) {
        this.songService = songService;
        this.validator = validator;
        this.userService = userService;
    }

    @PostMapping(value = "/songs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReadSongInfoDTO> add(@RequestPart(name = "cover") MultipartFile cover,
                                               @RequestPart(name = "file") MultipartFile file,
                                               @RequestPart(name = "dto")String saveSongDTOString) throws IOException {

        SaveSongDTO saveSongDTO = objectMapper.readValue(saveSongDTOString, SaveSongDTO.class); //transformation du JSON (String) en objet Java car je recois des FormData du frontend
        saveSongDTO = new SaveSongDTO(saveSongDTO.title(), saveSongDTO.author(),
                cover.getBytes(), cover.getContentType(), file.getBytes(), file.getContentType());

        //validation des champs qui ont été crée
        Set<ConstraintViolation<SaveSongDTO>> violations = validator.validate(saveSongDTO);

        if(!violations.isEmpty()){
            String violationJoined = violations.stream()
                                                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                                                .collect(Collectors.joining());

            ProblemDetail validationIssue = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation errors for the fiels : "+ violationJoined);
            return ResponseEntity.of(validationIssue).build();
        }else {
            return ResponseEntity.ok(songService.create(saveSongDTO));
        }
    }

    @GetMapping("/songs")
    public ResponseEntity<List<ReadSongInfoDTO>> getAll(){
        return ResponseEntity.ok(songService.getAll());
    }

    @GetMapping("/songs/get-content")
    public ResponseEntity<SongContentDTO> getOneByPublicId(@RequestPart UUID publicId){
        Optional<SongContentDTO> songContentByPublicId = songService.getOneByPublicId(publicId);
        return songContentByPublicId.map(ResponseEntity::ok)
                                    .orElseGet(() -> ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "UUID unknown")).build());
    }

    @GetMapping("/songs/search")
    public ResponseEntity<List<ReadSongInfoDTO>> search(@RequestParam String term){
        return ResponseEntity.ok(songService.search(term));
    }

    @PostMapping("/songs/like")
    public ResponseEntity<FavoriteSongDTO> addOrRemoveFromFavorite(@Valid @RequestBody FavoriteSongDTO favoriteSongDTO){
        ReadUserDTO userFromAuthentication = userService.getAuthenticatedUserFromSecurityContext();
        State<FavoriteSongDTO, String> favoriteSongResponse = songService.addOrRemoveFromFavorite(favoriteSongDTO, userFromAuthentication.email());

        if(favoriteSongResponse.getStatus().equals(StatusNotification.ERROR)){
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, favoriteSongResponse.getError());
            return ResponseEntity.of(problemDetail).build();
        }else {
            return ResponseEntity.ok(favoriteSongResponse.getValue());
        }
    }

    @GetMapping("/songs/like")
    public ResponseEntity<List<ReadSongInfoDTO>> fetchFavoriteSongs(){
        ReadUserDTO userFromAuthentication = userService.getAuthenticatedUserFromSecurityContext();
        return ResponseEntity.ok(songService.fetchFavoriteSongs(userFromAuthentication.email()));
    }

}
