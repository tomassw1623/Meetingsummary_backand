package com.example.meetingsummary.controller;

import com.example.meetingsummary.dto.AiResponse;
import com.example.meetingsummary.dto.MeetingMinutesRequest;
import com.example.meetingsummary.entity.MeetingRecord;
import com.example.meetingsummary.entity.User;
import com.example.meetingsummary.repository.MeetingRecordRepository;
import com.example.meetingsummary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api") // 1. 공통 API 경로 설정
@RequiredArgsConstructor // 2. final 필드에 대한 생성자 자동 생성 (Lombok)
public class AiController {

    private final ChatClient.Builder chatClientBuilder;
    private final MeetingRecordRepository recordRepository;
    private final UserRepository userRepository;

    // 3. 요약 API 수정
    @PostMapping("/summarize")
    public ResponseEntity<AiResponse> summarize(@RequestBody MeetingMinutesRequest request, Authentication authentication) {
        // Authentication 객체에서 로그인한 사용자의 이메일 가져오기
        // MeController.java 또는 AiController.java 내부

        String providerId = authentication.getName();
        User user = userRepository.findByProviderId(providerId) // findByEmail 대신 findByProviderId 사용
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        ChatClient chatClient = chatClientBuilder.build();
        String prompt = createPrompt(request.content());

        AiResponse aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .entity(AiResponse.class);

        // 데이터베이스에 저장할 때, 로그인한 사용자 정보도 함께 저장
        MeetingRecord newRecord = new MeetingRecord();
        newRecord.setOriginalContent(request.content());
        newRecord.setSummary(aiResponse.summary());
        newRecord.setDecisions(aiResponse.decisions());
        newRecord.setTodo(aiResponse.todo());
        newRecord.setCreatedAt(LocalDateTime.now());
        newRecord.setUser(user); // 사용자 정보 설정

        recordRepository.save(newRecord);

        return ResponseEntity.ok(aiResponse);
    }

    // 4. 전체 기록 조회 API 삭제
    // @GetMapping("/ai/records") -> MeController의 /api/me/records 로 대체되었으므로 삭제합니다.

    // 프롬프트를 생성하는 private 메소드
    private String createPrompt(String content) {
        return """
                [CONTEXT]
                        당신은 기업 커뮤니케이션을 전문으로 하는 '수석 비즈니스 분석가'입니다. 당신의 임무는 복잡한 회의록을 명확하고 실행 가능한 세 가지 핵심 항목으로 구조화하여 정리하는 것입니다. 결과물은 경영진과 실무진 모두가 빠르고 정확하게 상황을 파악할 수 있도록 전문적이고 간결해야 합니다.

                        [INSTRUCTIONS]
                        주어진 [회의록 내용]을 분석하여 아래 [OUTPUT FORMAT]에 명시된 JSON 형식에 따라 '핵심 요약', '결정 사항', '할 일 목록'을 추출하고 정리해주세요.

                        1.  **핵심 요약 (summary)**:
                            * 회의의 핵심 목적과 배경, 가장 중요한 논의 결과를 1~2 문장으로 압축해주세요.
                            * 누가, 무엇을, 왜 논의했는지가 명확히 드러나야 합니다.

                        2.  **결정 사항 (decisions)**:
                            * 회의를 통해 최종적으로 확정된 모든 결정을 번호 목록 형식으로 명확하게 나열해주세요.
                            * 각 결정 사항 뒤에는 그 결정이 내려진 핵심 이유나 배경을 간략히 덧붙여주세요.
                            * 만약 결정된 사항이 없다면, "결정된 사항 없음"이라고 명시해주세요.

                        3.  **할 일 목록 (todo)**:
                            * 회의 결과 발생한 모든 실행 항목(Action Item)을 번호 목록 형식으로 나열해주세요.
                            * 각 항목은 "[담당자] 마감일: YYYY-MM-DD - 내용" 형식으로 작성해야 합니다.
                            * 담당자나 마감일 정보가 명확하지 않다면, 각각 '[담당자 미지정]', '마감일 미지정'으로 표기해주세요.
                            * 만약 실행 항목이 없다면, "실행 항목 없음"이라고 명-시해주세요.

                        [OUTPUT FORMAT]
                        결과는 반드시 아래와 같은 키(key)를 가진 유효한 JSON 형식이어야 합니다. 다른 어떤 텍스트나 설명도 포함하지 마세요.
            {
              "summary": "...",
              "decisions": "...",
              "todo": "..."
            }

            ---
            회의록 내용:
            %s
            ---
            """.formatted(content);
    }
}