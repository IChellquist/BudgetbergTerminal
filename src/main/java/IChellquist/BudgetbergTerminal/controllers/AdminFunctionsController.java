package IChellquist.BudgetbergTerminal.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminFunctionsController {

@PostMapping("/testBackendReportFetch")
public ResponseEntity<?> testBackendReportFetch(){
    System.out.println("Test");


    return ResponseEntity.ok(null);
}



}
