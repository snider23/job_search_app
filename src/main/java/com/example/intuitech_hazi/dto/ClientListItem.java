package com.example.intuitech_hazi.dto;

import com.example.intuitech_hazi.domain.Client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ClientListItem {

    //TODO KELL EGYALTALAN ez az osztaly?

    @Size(min=1, max = 100)
    private String name;
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    public ClientListItem() {

    }

    public ClientListItem(@NotEmpty @Size(min=1,max=100)String name, @NotEmpty @Email String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
