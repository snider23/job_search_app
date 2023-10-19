package com.example.jobsearch.domain;

import com.example.jobsearch.dto.incoming.ClientSaveCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;



@Entity
@Data
@AllArgsConstructor
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String apiKey;

    public Client() {
    }

    public Client(ClientSaveCommand clientSaveCommand) {
        if (clientSaveCommand.getName().length()>100){
            throw new IllegalArgumentException("The name is too long");
        }
        this.name = clientSaveCommand.getName();
        this.email = clientSaveCommand.getEmail();

    }
}
