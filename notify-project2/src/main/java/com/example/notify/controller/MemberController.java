package com.example.notify.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notify.dto.CreateMemberRequest;
import com.example.notify.dto.CreateMemberResponse;
import com.example.notify.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

  private final MemberService memberService;

    @PostMapping
    public ResponseEntity<CreateMemberResponse> createMember(@RequestBody CreateMemberRequest request) {
      return ResponseEntity.status(201).body(memberService.createMember(request));
    }
}