package com.ytUploads.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class YoutubeVideoUploadService {

    private static final String UPLOAD_URL =  "https://www.googleapis.com/upload/youtube/v3/videos?uploadType=resumable&part=snippet,status";

    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public String uploadVideo(String tittle, String desc, String visibility, MultipartFile videoFile, String accessToken) throws IOException {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

        String metaData= """
                {
                  "snippet": {
                    "title": "%s",
                    "description": "%s",
                    "tags": ["cool", "video", "bhakti"],
                    "categoryId": 22
                  },
                  "status": {
                    "privacyStatus": "%s",
                    "embeddable": true,
                    "license": "youtube"
                  }
                }
                """.formatted(
                        tittle,
                        desc,
                        visibility
                );

        HttpRequest request = requestFactory.buildPostRequest(
                new GenericUrl(UPLOAD_URL),
                ByteArrayContent.fromString("application/json", metaData)
        );

        request.getHeaders().setAuthorization(accessToken);
        request.getHeaders().setContentType("application/json");

        HttpResponse response = request.execute();

        // Upload Videos

        String videoUploadUrl = response.getHeaders().getLocation();
        HttpRequest httpRequest = requestFactory.buildPutRequest(
                new GenericUrl(videoUploadUrl),
                new InputStreamContent("video/*", videoFile.getInputStream())
        );

        HttpResponse httpResponse = httpRequest.execute();


        return "Upload Successfully";
    }

}
