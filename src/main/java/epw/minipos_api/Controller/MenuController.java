package epw.minipos_api.Controller;

import epw.minipos_api.Service.MenuService;
import epw.minipos_api.dto.MenuOptionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{id_rol}")
    public List<MenuOptionDto> listByRole(@PathVariable("id_rol") long roleId) {
        return menuService.listByRole(roleId);
    }
}
