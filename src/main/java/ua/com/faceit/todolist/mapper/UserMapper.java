package ua.com.faceit.todolist.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ua.com.faceit.todolist.data.User;
import ua.com.faceit.todolist.dto.UserDTO;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.SETTER)
public interface UserMapper {
    List<UserDTO> toUserDTOs(List<User> users);
}
