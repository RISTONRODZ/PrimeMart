package org.riston.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.riston.ecommerce.domain.HomeCategorySection;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class HomeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @JsonProperty("imageUrl")
    private String image;

    private String categoryId;
    @Enumerated(EnumType.STRING)
    private HomeCategorySection section;
}
