package org.example.infratest.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.infratest.entity.MemberEntity;
import org.example.infratest.global.api.CommonResponse;
import org.example.infratest.service.FileUploadService;
import org.example.infratest.service.impl.MemberServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {
    private final MemberServiceImpl memberServiceImpl;
    private final FileUploadService fileUploadService;

    @PostMapping("/save")
    public CommonResponse<?> save(@RequestBody String name) {
        MemberEntity member = memberServiceImpl.save(name);
        return CommonResponse.success(member, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public CommonResponse<?> list() {
        List<MemberEntity> members = memberServiceImpl.list();
        return CommonResponse.success(members);
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String url = fileUploadService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(url);
    }
}
