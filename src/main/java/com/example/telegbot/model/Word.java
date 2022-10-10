package com.example.telegbot.model;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "word")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Word extends AbstractBaseEntity {


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nametwo", nullable = false)
    private String nameTwo;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Objects.equals(id, word.id) && Objects.equals(name, word.name) && Objects.equals(nameTwo, word.nameTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameTwo);
    }
}

