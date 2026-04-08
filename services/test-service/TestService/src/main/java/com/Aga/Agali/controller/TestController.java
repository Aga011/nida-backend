package com.Aga.Agali.controller;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.entity.*;
import com.Aga.Agali.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping("/assessment/start")
    public ResponseEntity<TestSession> startAssessment(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-GradeLevel") String gradeLevel) {
        return ResponseEntity.ok(
                testService.startAssessment(userEmail, gradeLevel));
    }

    @GetMapping("/assessment/questions/{sessionId}")
    public ResponseEntity<List<ShuffledQuestionDto>> getAssessmentQuestions(
            @PathVariable Long sessionId) {
        return ResponseEntity.ok(testService.getAssessmentQuestions(sessionId));
    }

    @PostMapping("/start")
    public ResponseEntity<TestSession> startTest(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestParam GradeLevel gradeLevel,
            @RequestParam SpecialtyGroup specialtyGroup) {
        return ResponseEntity.ok(
                testService.startTest(userEmail, gradeLevel, specialtyGroup));
    }

    @GetMapping("/questions/{sessionId}")
    public ResponseEntity<List<ShuffledQuestionDto>> getQuestions(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestParam GradeLevel gradeLevel,
            @RequestParam SpecialtyGroup specialtyGroup,
            @PathVariable Long sessionId) {
        return ResponseEntity.ok(
                testService.getQuestions(userEmail, gradeLevel, specialtyGroup, sessionId));
    }

    @GetMapping("/spaced")
    public ResponseEntity<List<ShuffledQuestionDto>> getSpacedQuestions(
            @RequestHeader("X-User-Email") String userEmail) {
        return ResponseEntity.ok(testService.getSpacedQuestions(userEmail));
    }

    @PostMapping("/submit/{sessionId}")
    public ResponseEntity<QuestionAttempt> submitAnswer(
            @RequestHeader("X-User-Email") String userEmail,
            @PathVariable Long sessionId,
            @RequestBody SubmitAnswerRequest request) {
        return ResponseEntity.ok(
                testService.submitAnswer(userEmail, sessionId, request));
    }


    @PostMapping("/finish/{sessionId}")
    public ResponseEntity<TestResultDto> finishTest(
            @RequestHeader("X-User-Email") String userEmail,
            @PathVariable Long sessionId) {
        return ResponseEntity.ok(testService.finishTest(userEmail, sessionId));
    }
}