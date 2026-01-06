package com.example.notify.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.notify.component.EmailSender;
import com.example.notify.component.S3TemplateLoader;
import com.example.notify.dto.CreateMemberRequest;
import com.example.notify.dto.CreateMemberResponse;
import com.example.notify.entity.Member;
import com.example.notify.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final S3TemplateLoader s3TemplateLoader;
    private final EmailSender emailSender;
    // private final SmsSender smsSender;

    // 가입
    public CreateMemberResponse createMember(CreateMemberRequest request) {
        // DB에 넣을 PK, 가입일 시
        String memberId = UUID.randomUUID().toString().replace("-", "");
        String createdAt = LocalDateTime.now().toString();

        // DB에 넣을 엔티티
        Member member = Member.builder()
            .memberId(memberId)
            .name(request.name())
            .email(request.email())
            .phone(request.phone())
            .createdAt(createdAt)
            .build();

        // DB에 저장
        memberRepository.save(member);

        // S3 welcome.html 가져와 "{name}" 치환
        String html = s3TemplateLoader.loadWelcomeTemplate();
        html = html.replace("{name}", request.name());

        // 웰컴 메일 전송
        emailSender.sendEmail(request.email(), html, true);

        // // 문자 전송
        // smsSender.sendSms(request.name());

        // 반환
        return new CreateMemberResponse(memberId, "멤버십 가입 성공!", createdAt);
    }
}
