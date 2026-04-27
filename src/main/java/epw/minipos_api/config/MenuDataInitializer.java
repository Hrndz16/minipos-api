package epw.minipos_api.config;

import epw.minipos_api.Entity.Role;
import epw.minipos_api.Entity.RoleMenuOption;
import epw.minipos_api.Repository.RoleMenuOptionRepository;
import epw.minipos_api.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MenuDataInitializer implements CommandLineRunner {
    private static final long ADMIN_ROLE_ID = 1L;
    private static final long USER_ROLE_ID = 2L;

    private final RoleRepository roleRepository;
    private final RoleMenuOptionRepository roleMenuOptionRepository;

    public MenuDataInitializer(RoleRepository roleRepository, RoleMenuOptionRepository roleMenuOptionRepository) {
        this.roleRepository = roleRepository;
        this.roleMenuOptionRepository = roleMenuOptionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedRole(
                new Role(ADMIN_ROLE_ID, "ADMIN"),
                List.of(
                        new MenuSeed("customers", "Customers", "CustomersPage", 1),
                        new MenuSeed("departments", "Departments", "DepartamentsPage", 2),
                        new MenuSeed("tmo", "TMO", "TestMenuOptionPage", 3),
                        new MenuSeed("about", "About...", "AboutPage", 4)
                )
        );

        seedRole(
                new Role(USER_ROLE_ID, "USER"),
                List.of(
                        new MenuSeed("customers", "Customers", "CustomersPage", 1),
                        new MenuSeed("about", "About...", "AboutPage", 2)
                )
        );
    }

    private void seedRole(Role role, List<MenuSeed> menuSeeds) {
        Role persistedRole = roleRepository.findById(role.getId())
                .orElseGet(() -> roleRepository.save(role));

        roleMenuOptionRepository.deleteByRoleId(persistedRole.getId());

        roleMenuOptionRepository.saveAll(menuSeeds.stream()
                .map(menuSeed -> new RoleMenuOption(
                        persistedRole,
                        menuSeed.name(),
                        menuSeed.content(),
                        menuSeed.component(),
                        menuSeed.displayOrder()
                ))
                .toList());
    }

    private record MenuSeed(String name, String content, String component, int displayOrder) {
    }
}
