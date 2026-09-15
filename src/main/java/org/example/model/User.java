package org.example.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "User entity representing a registered person")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Schema(description = "Unique identifier of the user", example = "usr_1001", readOnly = true)
    private String id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Full name of the user", example = "Jane Doe", required = true)
    private String name;

    @NotBlank(message = "Address is required")
    @Size(min = 3, max = 255, message = "Address must be between 3 and 255 characters")
    @Schema(description = "Physical address of the user", example = "123 Main Street, Suite 400, Springfield", required = true)
    private String address;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 120, message = "Email cannot exceed 120 characters")
    @Schema(description = "Email address of the user", example = "jane.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Telephone is required")
    @Pattern(regexp = "^[+0-9()\\-\\s]{6,25}$", message = "Telephone must be a valid phone number (6-25 digits/punctuation)")
    @Schema(description = "Telephone contact number", example = "+1 (555) 234-5678", required = true)
    private String telephone;

    public User() {
    }

    public User(String id, String name, String address, String email, String telephone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.telephone = telephone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
