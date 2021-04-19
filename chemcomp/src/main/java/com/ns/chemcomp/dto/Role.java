package com.ns.chemcomp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Role {
    private int roleId;

    @NotNull
    @Size(max = 30, message = "Role name must not exceed 30 characters")
    private String role;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role1 = (Role) o;
        return getRoleId() == role1.getRoleId() &&
                getRole().equals(role1.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getRole());
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + roleId +
                ", role='" + role + '\'' +
                '}';
    }
}
