package com.school.lostandfound.controller;

import com.school.lostandfound.utils.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${file.upload-path:./uploads/}")
    private String uploadPath;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + suffix;

        // Ensure upload directory exists and is absolute
        File uploadDir = new File(uploadPath);
        if (!uploadDir.isAbsolute()) {
            uploadDir = new File(System.getProperty("user.dir"), uploadPath);
        }
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File dest = new File(uploadDir, newFileName);
        try {
            file.transferTo(dest.getAbsoluteFile());
            return Result.success("/uploads/" + newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败: " + e.getMessage() + " (Path: " + dest.getAbsolutePath() + ")");
        }
    }
}
