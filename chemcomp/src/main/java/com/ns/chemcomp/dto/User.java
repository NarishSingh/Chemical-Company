package com.ns.chemcomp.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

public class User {
    private int userId;

    @NotBlank(message = "Must have a username for account")
    @Size(max = 30, message = "Username cannot exceed 30 chars")
    private String username;

    @NotBlank(message = "Every account must be secured with a password")
    @Size(max = 50, message = "Password cannot exceed 50 char")
    private String password;

    @NotNull(message = "Please indicate user status")
    private boolean enabled;

    @Size(max = 50, message = "Name cannot exceed 50 chars")
    private String name;

    @Size(min = 13, max = 13, message = "Phone number must be well-formed, beginning with 1 and the area code")
    private String phone;

    @NotNull(message = "E-mail required for account")
    @Email(message = "E-mail address must be well formed")
    @Size(max = 50, message = "Email cannot exceed 50 chars")
    private String email;

    @NotNull(message = "Address required for ordering")
    @Size(max = 50, message = "Address cannot exceed 50 chars")
    private String address;

    @Valid
    Set<Role> roles;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUserId() == user.getUserId() && isEnabled() == user.isEnabled() && getUsername().equals(user.getUsername())
                && getPassword().equals(user.getPassword()) && Objects.equals(getName(), user.getName())
                && Objects.equals(getPhone(), user.getPhone()) && getEmail().equals(user.getEmail())
                && getAddress().equals(user.getAddress()) && getRoles().equals(user.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getUsername(), getPassword(), isEnabled(), getName(), getPhone(), getEmail(),
                getAddress(), getRoles());
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", roles=" + roles +
                '}';
    }
}
