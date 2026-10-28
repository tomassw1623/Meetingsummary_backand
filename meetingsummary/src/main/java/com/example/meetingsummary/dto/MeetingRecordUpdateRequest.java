package com.example.meetingsummary.dto;

// 사용자가 회의록 수정을 요청할 때 사용할 데이터 상자(DTO)입니다.
// summary, decisions, todo 세 가지 필드의 수정을 허용합니다.
public record MeetingRecordUpdateRequest(
        String summary,
        String decisions,
        String todo
) {
}
