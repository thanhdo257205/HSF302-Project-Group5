package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recruiters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recruiter {
    @Id
    @Column(name = "recruiter_id")
    private Integer recruiterId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "company_id", nullable = false)
    private Integer companyId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;

    // Relationships
    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
    @Builder.Default
    private Set<JobPost> jobPosts = new HashSet<>();
}

