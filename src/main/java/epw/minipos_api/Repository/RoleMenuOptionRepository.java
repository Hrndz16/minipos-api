package epw.minipos_api.Repository;

import epw.minipos_api.Entity.RoleMenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleMenuOptionRepository extends JpaRepository<RoleMenuOption, Long> {
    List<RoleMenuOption> findByRole_IdOrderByDisplayOrderAsc(Long roleId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from RoleMenuOption option where option.role.id = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);
}
