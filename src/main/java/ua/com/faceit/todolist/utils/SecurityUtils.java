package ua.com.faceit.todolist.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import ua.com.faceit.todolist.data.ERole;
import ua.com.faceit.todolist.service.UserDetailsImpl;

public class SecurityUtils {
    public static Long getUserId() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userDetails.getId();
    }

    public static boolean isUserAdmin() {
        UserDetailsImpl userDetails = getUser();

        return userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(ERole.ROLE_ADMIN.name()));
    }

    private static UserDetailsImpl getUser() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
