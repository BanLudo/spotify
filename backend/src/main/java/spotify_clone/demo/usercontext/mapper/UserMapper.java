package spotify_clone.demo.usercontext.mapper;

import org.mapstruct.Mapper;
import spotify_clone.demo.usercontext.ReadUserDTO;
import spotify_clone.demo.usercontext.domain.User;

@Mapper(componentModel="spring")
public interface UserMapper {

    ReadUserDTO readUserDTOToUser(User entity);
}
