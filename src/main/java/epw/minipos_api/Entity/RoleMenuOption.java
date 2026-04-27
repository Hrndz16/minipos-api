package epw.minipos_api.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "role_menu_options",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_role_menu_option_name", columnNames = {"role_id", "name"}),
                @UniqueConstraint(name = "uk_role_menu_option_order", columnNames = {"role_id", "display_order"})
        }
)
public class RoleMenuOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 120)
    private String content;

    @Column(nullable = false, length = 120)
    private String component;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    protected RoleMenuOption() {
    }

    public RoleMenuOption(Role role, String name, String content, String component, Integer displayOrder) {
        this.role = role;
        this.name = name;
        this.content = content;
        this.component = component;
        this.displayOrder = displayOrder;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getComponent() {
        return component;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }
}
