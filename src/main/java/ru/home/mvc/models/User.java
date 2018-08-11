package ru.home.mvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.home.mvc.forms.UserForm;


import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fix_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;

    @OneToMany
    @JoinColumn(name = "owner_id")
    private List<Car> cars;

    public static User fromForm(UserForm form) {
        return User.builder()
                .first_name(form.getFirstName())
                .last_name(form.getLastName())
                .build();
    }
}
