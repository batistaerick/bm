package com.budgetmanager.bm.domain.entities;

import jakarta.persistence.*;
import java.sql.Blob;
import java.util.UUID;
import lombok.*;

@Table(name = "t_user_image")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserImage {

    @Id
    @GeneratedValue
    private UUID id;

    @Lob
    private Blob profileImage;

    private String name;
    private String type;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
