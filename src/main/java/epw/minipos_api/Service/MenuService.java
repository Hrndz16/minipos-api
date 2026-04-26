package epw.minipos_api.Service;

import epw.minipos_api.dto.MenuOptionDto;

import java.util.List;

public interface MenuService {
    List<MenuOptionDto> listByRole(long roleId);
}
