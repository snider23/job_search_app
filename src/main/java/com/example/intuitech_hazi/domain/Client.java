package com.example.intuitech_hazi.domain;

import com.example.intuitech_hazi.dto.incoming.ClientSaveCommand;
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
//    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
//            flags = Pattern.Flag.CASE_INSENSITIVE)
//    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Column(nullable = false)
//    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private String apiKey;

    public Client() {
    }

    public Client(ClientSaveCommand clientSaveCommand) {
        this.name = clientSaveCommand.getName();
        this.email = clientSaveCommand.getEmail();

    }
}
