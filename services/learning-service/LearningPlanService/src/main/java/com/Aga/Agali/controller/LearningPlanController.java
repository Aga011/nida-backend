package com.Aga.Agali.controller;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.service.LearningPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class LearningPlanController {

    private final LearningPlanService learningPlanService;

    @PostMapping("/create")
    public ResponseEntity<LearningPlanResponse> createPlan(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestBody LearningPlanRequest request) {
        return ResponseEntity.ok(
                learningPlanService.createPlan(userEmail, request));
    }

    @GetMapping("/my")
    public ResponseEntity<LearningPlanResponse> getMyPlan(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestParam String stage) {
        return ResponseEntity.ok(
                learningPlanService.getPlan(userEmail, stage));
    }

    @PutMapping("/complete/{itemId}")
    public ResponseEntity<LearningPlanResponse> completeItem(
            @PathVariable Long itemId) {
        return ResponseEntity.ok(
                learningPlanService.completeItem(itemId));
    }
}