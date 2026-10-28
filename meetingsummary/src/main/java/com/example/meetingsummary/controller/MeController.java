//package com.example.meetingsummary.controller;
//
//import com.example.meetingsummary.dto.MeetingRecordUpdateRequest;
//import com.example.meetingsummary.entity.MeetingRecord;
//import com.example.meetingsummary.entity.User;
//import com.example.meetingsummary.repository.MeetingRecordRepository;
//import com.example.meetingsummary.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/me")
//@RequiredArgsConstructor
//public class MeController {
//
//    private final UserRepository userRepository;
//    private final MeetingRecordRepository meetingRecordRepository;
//
//    // 내 프로필 정보 조회
//    @GetMapping
//    public ResponseEntity<User> getMyProfile(Authentication authentication) {
//        String providerId = authentication.getName();
//        User user = userRepository.findByProviderId(providerId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//        return ResponseEntity.ok(user);
//    }
//
//    // 내 회의록 기록 전체 조회
//    @GetMapping("/records")
//    public ResponseEntity<List<MeetingRecord>> getMyRecords(Authentication authentication) {
//        String providerId = authentication.getName();
//        User user = userRepository.findByProviderId(providerId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//        List<MeetingRecord> records = meetingRecordRepository.findAllByUserOrderByIdDesc(user);
//        return ResponseEntity.ok(records);
//    }
//
//    // --- [추가된 부분] ---
//    /**
//     * 특정 회의록 기록 단일 조회
//     * @param recordId 조회할 기록의 ID
//     * @param authentication 현재 로그인한 사용자 정보
//     * @return 조회된 MeetingRecord 객체
//     */
//    @GetMapping("/records/{recordId}")
//    public ResponseEntity<MeetingRecord> getMyRecordById(
//            @PathVariable Long recordId,
//            Authentication authentication) {
//
//        String providerId = authentication.getName();
//        User user = userRepository.findByProviderId(providerId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//
//        MeetingRecord record = meetingRecordRepository.findById(recordId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 기록을 찾을 수 없습니다. ID: " + recordId));
//
//        if (!record.getUser().getId().equals(user.getId())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        return ResponseEntity.ok(record);
//    }
//    // --- [추가된 부분 끝] ---
//
//    @Transactional
//    @PutMapping("/records/{recordId}")
//    public ResponseEntity<MeetingRecord> updateMyRecord(
//            @PathVariable Long recordId,
//            @RequestBody MeetingRecordUpdateRequest request,
//            Authentication authentication) {
//
//        String providerId = authentication.getName();
//        User user = userRepository.findByProviderId(providerId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//
//        MeetingRecord record = meetingRecordRepository.findById(recordId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 기록을 찾을 수 없습니다. ID: " + recordId));
//
//        if (!record.getUser().getId().equals(user.getId())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        record.update(request);
//        MeetingRecord updatedRecord = meetingRecordRepository.save(record);
//
//        return ResponseEntity.ok(updatedRecord);
//    }
//
//    @DeleteMapping("/records/{recordId}")
//    public ResponseEntity<Void> deleteMyRecord(
//            @PathVariable Long recordId,
//            Authentication authentication) {
//
//        String providerId = authentication.getName();
//        User user = userRepository.findByProviderId(providerId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//
//        MeetingRecord record = meetingRecordRepository.findById(recordId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 기록을 찾을 수 없습니다. ID: " + recordId));
//
//        if (!record.getUser().getId().equals(user.getId())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        meetingRecordRepository.delete(record);
//        return ResponseEntity.noContent().build();
//    }
//}

package com.example.meetingsummary.controller;

import com.example.meetingsummary.dto.MeetingRecordUpdateRequest;
import com.example.meetingsummary.entity.MeetingRecord;
import com.example.meetingsummary.entity.User;
import com.example.meetingsummary.repository.MeetingRecordRepository;
import com.example.meetingsummary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {

    private final UserRepository userRepository;
    private final MeetingRecordRepository meetingRecordRepository;

    // 내 프로필 정보 조회
    @GetMapping
    public ResponseEntity<User> getMyProfile(Authentication authentication) {
        String providerId = authentication.getName();
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return ResponseEntity.ok(user);
    }

    // 내 회의록 기록 전체 조회
    @GetMapping("/records")
    public ResponseEntity<List<MeetingRecord>> getMyRecords(Authentication authentication) {
        String providerId = authentication.getName();
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<MeetingRecord> records = meetingRecordRepository.findAllByUserOrderByIdDesc(user);
        return ResponseEntity.ok(records);
    }

    // 특정 회의록 기록 단일 조회
    @GetMapping("/records/{recordId}")
    public ResponseEntity<MeetingRecord> getMyRecordById(
            @PathVariable Long recordId,
            Authentication authentication) {

        String providerId = authentication.getName();
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        MeetingRecord record = meetingRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록을 찾을 수 없습니다. ID: " + recordId));

        if (!record.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(record);
    }

    // 특정 회의록 기록 수정
    @Transactional
    @PutMapping("/records/{recordId}")
    public ResponseEntity<MeetingRecord> updateMyRecord(
            @PathVariable Long recordId,
            @RequestBody MeetingRecordUpdateRequest request,
            Authentication authentication) {

        String providerId = authentication.getName();
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        MeetingRecord record = meetingRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록을 찾을 수 없습니다. ID: " + recordId));

        if (!record.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        record.update(request);
        MeetingRecord updatedRecord = meetingRecordRepository.save(record);

        return ResponseEntity.ok(updatedRecord);
    }

    // 특정 회의록 기록 삭제
    @DeleteMapping("/records/{recordId}")
    public ResponseEntity<Void> deleteMyRecord(
            @PathVariable Long recordId,
            Authentication authentication) {

        String providerId = authentication.getName();
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        MeetingRecord record = meetingRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록을 찾을 수 없습니다. ID: " + recordId));

        if (!record.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        meetingRecordRepository.delete(record);
        return ResponseEntity.noContent().build();
    }

    // createRecord (@PostMapping("/records")) 메소드가 여기서 삭제되었습니다.
}
