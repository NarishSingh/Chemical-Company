package com.ns.chemcomp.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class User {
    private int id;

    @NotBlank(message = "Must have a username for account")
    @Size(max = 30, message = "Username cannot exceed 30 chars")
    private String username;

    @NotBlank(message = "Every account must be secured with a password")
    @Size(max = 50, message = "Password cannot exceed 50 char")
    private String password;

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
    private String Address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                getUsername().equals(user.getUsername()) &&
                password.equals(user.password) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getPhone(), user.getPhone()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                getAddress().equals(user.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), password, getName(), getPhone(), getEmail(), getAddress());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }
}
