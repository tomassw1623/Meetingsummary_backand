package com.example.meetingsummary.entity;

import com.example.meetingsummary.dto.MeetingRecordUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // Lombok이 기본 생성자를 만들어줍니다.
@Entity
public class MeetingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String originalContent;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String decisions;

    @Column(columnDefinition = "TEXT")
    private String todo;

    private LocalDateTime createdAt;

    // --- [수정된 부분] User와의 연관 관계 ---
    // 이 기록이 어떤 사용자의 것인지를 나타냅니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    // --- [추가된 부분] ---
    // MeetingRecordUpdateRequest로부터 받은 데이터로 엔티티의 내용을 업데이트하는 메소드
    public void update(MeetingRecordUpdateRequest request) {
        if (request.summary() != null) {
            this.summary = request.summary();
        }
        if (request.decisions() != null) {
            this.decisions = request.decisions();
        }
        if (request.todo() != null) {
            this.todo = request.todo();
        }
    }
}
    // Lombok의 @Getter, @Setter 어노테이션이
    // 모든 필드에 대한 getter와 setter 메소드를 자동으로 만들어주므로,
    // 직접 작성할 필요가 없습니다.
