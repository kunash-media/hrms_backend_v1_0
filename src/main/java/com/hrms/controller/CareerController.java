package com.hrms.controller;
import com.hrms.dto.request.CareerRequestDto;
import com.hrms.dto.response.CareerResponseDto;
import com.hrms.service.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/career")
public class CareerController {

    @Autowired
    private CareerService careerService;

    // ==================== DASHBOARD ====================

    @GetMapping("/get-dashboard-stats/dashboard/stats")
    public ResponseEntity<CareerResponseDto> getDashboardStats() {
        return ResponseEntity.ok(careerService.getDashboardStats());
    }

    // ==================== JOB OPENING APIS ====================

    @GetMapping("/get-all-job-openings/openings")
    public ResponseEntity<CareerResponseDto> getAllJobOpenings() {
        return ResponseEntity.ok(careerService.getAllJobOpenings());
    }

    @GetMapping("/get-job-opening-by-id/openings/{id}")
    public ResponseEntity<CareerResponseDto> getJobOpeningById(@PathVariable Long id) {
        return ResponseEntity.ok(careerService.getJobOpeningById(id));
    }

    @GetMapping("/get-job-opening-by-status/openings/status/{status}")
    public ResponseEntity<CareerResponseDto> getJobOpeningsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(careerService.getJobOpeningsByStatus(status));
    }

    @GetMapping("/get-job-openings-by-department/openings/department/{department}")
    public ResponseEntity<CareerResponseDto> getJobOpeningsByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(careerService.getJobOpeningsByDepartment(department));
    }

    @GetMapping("/get-active-job-openings/openings/active")
    public ResponseEntity<CareerResponseDto> getActiveJobOpenings() {
        return ResponseEntity.ok(careerService.getActiveJobOpenings());
    }

    @PostMapping("/create-job-opening/openings")
    public ResponseEntity<CareerResponseDto> createJobOpening(@RequestBody CareerRequestDto request) {
        CareerResponseDto response = careerService.createJobOpening(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update-job-opening/openings/{id}")
    public ResponseEntity<CareerResponseDto> updateJobOpening(@PathVariable Long id, @RequestBody CareerRequestDto request) {
        return ResponseEntity.ok(careerService.updateJobOpening(id, request));
    }

    @PatchMapping("/patch-job-opening/openings/{id}")
    public ResponseEntity<CareerResponseDto> patchJobOpening(@PathVariable Long id, @RequestBody CareerRequestDto request) {
        return ResponseEntity.ok(careerService.patchJobOpening(id, request));
    }

    @DeleteMapping("/delete-job-opening/openings/{id}")
    public ResponseEntity<CareerResponseDto> deleteJobOpening(@PathVariable Long id) {
        return ResponseEntity.ok(careerService.deleteJobOpening(id));
    }

    // ==================== APPLICATION APIS ====================

    @GetMapping("/get-all-applications/applications")
    public ResponseEntity<CareerResponseDto> getAllApplications() {
        return ResponseEntity.ok(careerService.getAllApplications());
    }

    @GetMapping("/get-application-by-id/applications/{id}")
    public ResponseEntity<CareerResponseDto> getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(careerService.getApplicationById(id));
    }

    @GetMapping("/get-application-by-job-id/applications/job/{jobId}")
    public ResponseEntity<CareerResponseDto> getApplicationsByJobId(@PathVariable Long jobId) {
        return ResponseEntity.ok(careerService.getApplicationsByJobId(jobId));
    }

    @GetMapping("/get-applications-by-status/applications/status/{status}")
    public ResponseEntity<CareerResponseDto> getApplicationsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(careerService.getApplicationsByStatus(status));
    }

    @PostMapping("/create-application/applications")
    public ResponseEntity<CareerResponseDto> createApplication(@RequestBody CareerRequestDto request) {
        CareerResponseDto response = careerService.createApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update-application-status/applications/{id}/status")
    public ResponseEntity<CareerResponseDto> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) Long reviewedByEmployeeId) {
        return ResponseEntity.ok(careerService.updateApplicationStatus(id, status, reviewedByEmployeeId));
    }

    @PatchMapping("/patch-application/applications/{id}")
    public ResponseEntity<CareerResponseDto> patchApplication(@PathVariable Long id, @RequestBody CareerRequestDto request) {
        return ResponseEntity.ok(careerService.patchApplication(id, request));
    }

    // ==================== REFERRAL APIS ====================

    @GetMapping("/get-all-referrals/referrals")
    public ResponseEntity<CareerResponseDto> getAllReferrals() {
        return ResponseEntity.ok(careerService.getAllReferrals());
    }

    @GetMapping("/get-referral-by-id/referrals/{id}")
    public ResponseEntity<CareerResponseDto> getReferralById(@PathVariable Long id) {
        return ResponseEntity.ok(careerService.getReferralById(id));
    }

    @GetMapping("/get-referrals-by-job-id/referrals/job/{jobId}")
    public ResponseEntity<CareerResponseDto> getReferralsByJobId(@PathVariable Long jobId) {
        return ResponseEntity.ok(careerService.getReferralsByJobId(jobId));
    }

    @GetMapping("/get-referrals-by-employee-id/referrals/employee/{employeeId}")
    public ResponseEntity<CareerResponseDto> getReferralsByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(careerService.getReferralsByEmployeeId(employeeId));
    }

    @PostMapping("/create-referral/referrals")
    public ResponseEntity<CareerResponseDto> createReferral(@RequestBody CareerRequestDto request) {
        CareerResponseDto response = careerService.createReferral(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update-referral-status/referrals/{id}/status")
    public ResponseEntity<CareerResponseDto> updateReferralStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(careerService.updateReferralStatus(id, status));
    }

    @PatchMapping("/patch-referral/referrals/{id}")
    public ResponseEntity<CareerResponseDto> patchReferral(@PathVariable Long id, @RequestBody CareerRequestDto request) {
        return ResponseEntity.ok(careerService.patchReferral(id, request));
    }

    // ==================== JD TEMPLATE APIS ====================

    @GetMapping("/get-all-jd-templates/jd-templates")
    public ResponseEntity<CareerResponseDto> getAllJdTemplates() {
        return ResponseEntity.ok(careerService.getAllJdTemplates());
    }

    @GetMapping("/get-jd-template-by-id/jd-templates/{id}")
    public ResponseEntity<CareerResponseDto> getJdTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(careerService.getJdTemplateById(id));
    }

    @GetMapping("/get-jd-templates-by-department/jd-templates/department/{department}")
    public ResponseEntity<CareerResponseDto> getJdTemplatesByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(careerService.getJdTemplatesByDepartment(department));
    }

    @PostMapping("/create-jd-template/jd-templates")
    public ResponseEntity<CareerResponseDto> createJdTemplate(@RequestBody CareerRequestDto request) {
        CareerResponseDto response = careerService.createJdTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update-jd-template/jd-templates/{id}")
    public ResponseEntity<CareerResponseDto> updateJdTemplate(@PathVariable Long id, @RequestBody CareerRequestDto request) {
        return ResponseEntity.ok(careerService.updateJdTemplate(id, request));
    }

    @PatchMapping("/patch-jd-template/jd-templates/{id}")
    public ResponseEntity<CareerResponseDto> patchJdTemplate(@PathVariable Long id, @RequestBody CareerRequestDto request) {
        return ResponseEntity.ok(careerService.patchJdTemplate(id, request));
    }

    @DeleteMapping("/delete-jd-template/jd-templates/{id}")
    public ResponseEntity<CareerResponseDto> deleteJdTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(careerService.deleteJdTemplate(id));
    }
}
