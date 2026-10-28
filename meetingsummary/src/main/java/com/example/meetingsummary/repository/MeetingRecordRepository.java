package com.example.meetingsummary.repository;

import com.example.meetingsummary.entity.MeetingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.meetingsummary.entity.User;

// 1. @Repository: 이 인터페이스가 데이터 저장소 역할을 하는 컴포넌트임을 Spring에게 알려줍니다.
@Repository
// 2. JpaRepository<MeetingRecord, Long> 상속:
//    - JpaRepository를 상속받기만 하면, 기본적인 데이터베이스 작업(저장, 조회, 삭제 등)을
//      수행하는 메소드들을 우리가 직접 만들 필요 없이 바로 사용할 수 있습니다.
//    - <MeetingRecord, Long>: 이 Repository가 'MeetingRecord' 엔티티를 다루고,
//      그 엔티티의 ID(기본 키) 타입이 'Long'임을 의미합니다.
public interface MeetingRecordRepository extends JpaRepository<MeetingRecord, Long> {

    // 특정 사용자의 모든 기록을 ID 역순(최신순)으로 조회하는 메서드
    List<MeetingRecord> findAllByUserOrderByIdDesc(User user);

}