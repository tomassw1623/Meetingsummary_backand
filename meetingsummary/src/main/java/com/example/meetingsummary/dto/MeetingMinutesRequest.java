package com.example.meetingsummary.dto;

// MeetingMinutesRequest: 사용자가 회의록 내용을 담아 서버로 보낼 때 사용하는 '요청용 상자'입니다.
public record MeetingMinutesRequest(String content) {
}
