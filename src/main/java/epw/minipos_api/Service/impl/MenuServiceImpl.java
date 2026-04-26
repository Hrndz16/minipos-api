package epw.minipos_api.Service.impl;

import epw.minipos_api.Service.MenuService;
import epw.minipos_api.dto.MenuOptionDto;
import epw.minipos_api.exception.InvalidRoleException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    private static final long ADMIN_ROLE_ID = 1L;
    private static final long USER_ROLE_ID = 2L;

    private static final List<MenuOptionDto> ADMIN_MENU = List.of(
            new MenuOptionDto("customers", "Customers"),
            new MenuOptionDto("departments", "Departments"),
            new MenuOptionDto("tmo", "TMO"),
            new MenuOptionDto("about", "About...")
    );

    private static final List<MenuOptionDto> USER_MENU = List.of(
            new MenuOptionDto("customers", "Customers"),
            new MenuOptionDto("about", "About...")
    );

    @Override
    public List<MenuOptionDto> listByRole(long roleId) {
        return switch ((int) roleId) {
            case (int) ADMIN_ROLE_ID -> ADMIN_MENU;
            case (int) USER_ROLE_ID -> USER_MENU;
            default -> throw new InvalidRoleException(roleId);
        };
    }
}
