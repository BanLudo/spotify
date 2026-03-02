package spotify_clone.demo.catalogcontext.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import spotify_clone.demo.catalogcontext.application.SongService;
import spotify_clone.demo.catalogcontext.application.dto.ReadSongInfoDTO;
import spotify_clone.demo.catalogcontext.application.dto.SaveSongDTO;
import spotify_clone.demo.usercontext.application.UserService;

import java.io.IOException;
import java.util.Set;
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
}
