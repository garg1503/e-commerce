package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.modelmapper.internal.bytebuddy.agent.builder.AgentBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, message = "street name must contain atleast 5 characters")
    private String street;

    @NotBlank
    @Size(min = 5, message = "building name must contain atleast 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 5, message = "city name must contain atleast 5 characters")
    private String city;

    @NotBlank
    @Size(min = 5, message = "state must contain atleast 5 characters")
    private String state;

    @NotBlank
    @Size(min = 5, message = "country name must contain atleast 5 characters")
    private String country;

    @NotBlank
    @Size(min = 6, message = "pincode must contain atleast 5 characters")
    private String pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, String country, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
