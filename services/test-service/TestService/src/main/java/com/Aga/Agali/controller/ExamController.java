package com.Aga.Agali.controller;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping("/create")
    public ResponseEntity<ExamResponse> createExam(
            @RequestHeader("X-User-Email") String teacherEmail,
            @RequestHeader("X-User-Role") String role,
            @RequestBody @Valid ExamRequest request) {
        if (!role.equals("TEACHER")) {
            throw new RuntimeException("Yalnız müəllimlər imtahan yarada bilər");
        }
        return ResponseEntity.ok(
                examService.createExam(teacherEmail, request));
    }

    @PostMapping("/{examId}/pay")
    public ResponseEntity<ExamResponse> payForExam(
            @RequestHeader("X-User-Email") String teacherEmail,
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long examId) {
        if (!role.equals("TEACHER")) {
            throw new RuntimeException("Yalnız müəllimlər ödəniş edə bilər");
        }
        return ResponseEntity.ok(
                examService.payForExam(examId, teacherEmail));
    }

    @PostMapping("/{examId}/activate")
    public ResponseEntity<ExamResponse> activateExam(
            @RequestHeader("X-User-Email") String teacherEmail,
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long examId) {
        if (!role.equals("TEACHER")) {
            throw new RuntimeException("Yalnız müəllimlər imtahanı aktivləşdirə bilər");
        }
        return ResponseEntity.ok(
                examService.activateExam(examId, teacherEmail));
    }

    @PostMapping("/{examId}/start")
    public ResponseEntity<ExamSessionResponse> startExam(
            @RequestHeader("X-User-Email") String studentEmail,
            @PathVariable Long examId) {
        return ResponseEntity.ok(
                examService.startExam(examId, studentEmail));
    }

    @PostMapping("/session/{sessionId}/finish")
    public ResponseEntity<ExamSessionResponse> finishExam(
            @RequestHeader("X-User-Email") String studentEmail,
            @PathVariable Long sessionId) {
        return ResponseEntity.ok(
                examService.finishExam(sessionId, studentEmail));
    }

    @PostMapping("/session/{sessionId}/comment")
    public ResponseEntity<ExamSessionResponse> addComment(
            @RequestHeader("X-User-Email") String teacherEmail,
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long sessionId,
            @RequestBody @Valid ExamCommentRequest request) {
        if (!role.equals("TEACHER")) {
            throw new RuntimeException("Yalnız müəllimlər şərh əlavə edə bilər");
        }
        return ResponseEntity.ok(
                examService.addComment(sessionId, teacherEmail, request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ExamResponse>> getMyExams(
            @RequestHeader("X-User-Email") String teacherEmail,
            @RequestHeader("X-User-Role") String role) {
        if (!role.equals("TEACHER")) {
            throw new RuntimeException("Yalnız müəllimlər öz imtahanlarına baxa bilər");
        }
        return ResponseEntity.ok(
                examService.getTeacherExams(teacherEmail));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ExamResponse>> getGroupExams(
            @PathVariable Long groupId) {
        return ResponseEntity.ok(
                examService.getGroupExams(groupId));
    }

    @GetMapping("/{examId}/results")
    public ResponseEntity<List<ExamSessionResponse>> getExamResults(
            @RequestHeader("X-User-Email") String teacherEmail,
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long examId) {
        if (!role.equals("TEACHER")) {
            throw new RuntimeException("Yalnız müəllimlər nəticələrə baxa bilər");
        }
        return ResponseEntity.ok(
                examService.getExamResults(examId, teacherEmail));
    }

    @GetMapping("/{examId}/my-result")
    public ResponseEntity<ExamSessionResponse> getMyResult(
            @RequestHeader("X-User-Email") String studentEmail,
            @PathVariable Long examId) {
        return ResponseEntity.ok(
                examService.getStudentResult(examId, studentEmail));
    }
}