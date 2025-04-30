package com.ytUploads.controllers;

import com.ytUploads.services.YoutubeVideoUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/uploadVideo")
@CrossOrigin("*")
public class YoutubeUploadController {

    private YoutubeVideoUploadService service;

    public YoutubeUploadController(YoutubeVideoUploadService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> uploadVideo(
            @RequestParam("title") String title,
            @RequestParam("desc") String desc,
            @RequestParam("visibility") String visibility,
            @RequestParam("videoFile") MultipartFile videoFile,
            @RequestHeader("Authorization") String accessToken
    ) throws IOException {
        String response = service.uploadVideo(title, desc, visibility, videoFile, accessToken);
        return ResponseEntity.ok(response);
    }
}
