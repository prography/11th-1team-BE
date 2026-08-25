package org.example.knockin.life.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.knockin.meta.entity.File;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "life_pattern_file")
public class LifePatternFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "life_pattern_id", nullable = false)
    private LifePattern lifePattern;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    public void modifyFile(File file) {
        this.file = file;
    }
}
