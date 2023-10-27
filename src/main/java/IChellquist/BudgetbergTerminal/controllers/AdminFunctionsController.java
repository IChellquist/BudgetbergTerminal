package IChellquist.BudgetbergTerminal.controllers;


import IChellquist.BudgetbergTerminal.payload.request.ReportSettingsRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminFunctionsController {

@PostMapping("/testBackendReportFetch")
public ResponseEntity<?> testBackendReportFetch(@Valid @RequestBody ReportSettingsRequest reportSettingsRequest){



    return ResponseEntity.ok(null);
}



}
