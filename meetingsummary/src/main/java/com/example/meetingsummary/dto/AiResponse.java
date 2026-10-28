package com.example.meetingsummary.dto;

// 'record' 키워드는 Java 16부터 정식으로 도입된 기능입니다.
// 데이터를 담는 목적의 클래스를 만들 때 사용하며,
// 생성자, getter, equals(), toString() 같은 코드를 직접 작성할 필요 없이
// 컴파일러가 알아서 만들어주는 매우 편리한 기능입니다.
public record AiResponse(String summary, String decisions, String todo) {
}
