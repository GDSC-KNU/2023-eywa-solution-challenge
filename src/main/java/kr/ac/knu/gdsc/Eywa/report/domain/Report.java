package kr.ac.knu.gdsc.Eywa.report.domain;

import kr.ac.knu.gdsc.Eywa.common.domain.BaseTimeEntity;
import kr.ac.knu.gdsc.Eywa.dictionary.domain.Dictionary;
import kr.ac.knu.gdsc.Eywa.member.domain.Member;
import kr.ac.knu.gdsc.Eywa.report.dto.ReportResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "report_seq_generator",
        sequenceName = "report_seq",
        initialValue = 1,
        allocationSize = 1)
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_seq")
    @Column(name = "report_id")
    private Long id;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private String picture;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "dictionary_id")
    private Dictionary dictionary;

    @Builder
    public Report(BigDecimal latitude, BigDecimal longitude, String picture, Member member, Dictionary dictionary) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.picture = picture;
        this.member = member;
        this.dictionary = dictionary;
    }

    public ReportResponseDto toDto() {
        return ReportResponseDto.builder()
                .id(id)
                .latitude(latitude)
                .longitude(longitude)
                .picture(picture)
                .dictionary(dictionary.toDto())
                .member(member.toDto())
                .createdAt(getCreatedAt())
                .build();
    }
}
