package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "province")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "province_code", nullable = false, unique = true, length = 20)
    private String provinceCode;

    @Column(name = "province_name", nullable = false, unique = true, length = 100)
    private String provinceName;

    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<AdministrativeUnit> administrativeUnits = new HashSet<>();

    @OneToMany(mappedBy = "province",  fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Company> companies = new HashSet<>();

    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<CandidateProfile> candidateProfiles = new HashSet<>();

    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<JobPost> jobPosts = new HashSet<>();
}
