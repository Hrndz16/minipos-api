package epw.minipos_api.Service.impl;

import epw.minipos_api.Repository.RoleMenuOptionRepository;
import epw.minipos_api.Repository.RoleRepository;
import epw.minipos_api.Service.MenuService;
import epw.minipos_api.dto.MenuOptionDto;
import epw.minipos_api.exception.InvalidRoleException;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl implements MenuService {
    private final RoleRepository roleRepository;
    private final RoleMenuOptionRepository roleMenuOptionRepository;

    public MenuServiceImpl(RoleRepository roleRepository, RoleMenuOptionRepository roleMenuOptionRepository) {
        this.roleRepository = roleRepository;
        this.roleMenuOptionRepository = roleMenuOptionRepository;
    }

    @Override
    public java.util.List<MenuOptionDto> listByRole(long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new InvalidRoleException(roleId);
        }

        return roleMenuOptionRepository.findByRole_IdOrderByDisplayOrderAsc(roleId)
                .stream()
                .map(option -> new MenuOptionDto(
                        option.getName(),
                        option.getContent(),
                        option.getComponent()
                ))
                .toList();
    }
}
