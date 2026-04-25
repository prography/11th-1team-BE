package org.example.infratest.controller;

import lombok.RequiredArgsConstructor;
import org.example.infratest.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {
    private final MemberService memberService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody String name) {
        return ResponseEntity.ok(memberService.save(name));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(memberService.list());
    }
}
