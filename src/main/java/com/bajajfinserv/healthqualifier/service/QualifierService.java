package com.bajajfinserv.healthqualifier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bajajfinserv.healthqualifier.model.SubmissionRequest;
import com.bajajfinserv.healthqualifier.model.WebhookRequest;
import com.bajajfinserv.healthqualifier.model.WebhookResponse;

@Service
public class QualifierService {

    @Autowired
    private RestTemplate restTemplate;

    // API URLs
    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private static final String TEST_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

    public void executeQualifierFlow() {
        try {
            // Step 1: Generate webhook
            System.out.println("Step 1: Generating webhook...");
            WebhookResponse webhookResponse = generateWebhook();
            
            if (webhookResponse == null) {
                System.out.println("Failed to generate webhook. Exiting...");
                return;
            }
            
            System.out.println("Webhook generated successfully: " + webhookResponse);

            // Step 2: Determine question based on regNo and solve SQL problem
            System.out.println("Step 2: Solving SQL problem...");
            String finalQuery = solveSqlProblem("0101CS221059"); // Using the regNo from request
            
            System.out.println("SQL Query prepared: " + finalQuery);

            // Step 3: Submit solution
            System.out.println("Step 3: Submitting solution...");
            boolean success = submitSolution(webhookResponse.getAccessToken(), finalQuery);
            
            if (success) {
                System.out.println("Solution submitted successfully!");
            } else {
                System.out.println("Failed to submit solution.");
            }

        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private WebhookResponse generateWebhook() {
        try {
            // Create request body
            WebhookRequest request = new WebhookRequest("Isha Sahu", "0101CS221059", "ishasahu.2004@gmail.com");

            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create HTTP entity
            HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);

            // Make POST request
            ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(
                GENERATE_WEBHOOK_URL, entity, WebhookResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                System.out.println("Failed to generate webhook. Status: " + response.getStatusCode());
                return null;
            }

        } catch (Exception e) {
            System.out.println("Error generating webhook: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String solveSqlProblem(String regNo) {
        // Extract last two digits of regNo
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastTwoDigitsInt = Integer.parseInt(lastTwoDigits);

        String sqlQuery;
        
        if (lastTwoDigitsInt % 2 == 1) {
            // Odd - Question 1
            System.out.println("RegNo ends with odd digits (" + lastTwoDigits + "). Using Question 1 solution.");
            sqlQuery = "SELECT " +
                       "    p.AMOUNT AS SALARY, " +
                       "    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
                       "    FLOOR(DATEDIFF(CURDATE(), e.DOB) / 365.25) AS AGE, " +
                       "    d.DEPARTMENT_NAME " +
                       "FROM PAYMENTS p " +
                       "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                       "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                       "WHERE DAY(p.PAYMENT_TIME) != 1 " +
                       "ORDER BY p.AMOUNT DESC " +
                       "LIMIT 1;";
            
        } else {
            // Even - Question 2
            System.out.println("RegNo ends with even digits (" + lastTwoDigits + "). Using Question 2 solution.");
            sqlQuery = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT " +
                    "FROM EMPLOYEE e1 " +
                    "JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID " +
                    "LEFT JOIN EMPLOYEE e2 ON e2.DEPARTMENT = e1.DEPARTMENT AND e2.DOB > e1.DOB " +
                    "GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME " +
                    "ORDER BY e1.EMP_ID DESC;";
        }

        return sqlQuery;
    }

    private boolean submitSolution(String accessToken, String finalQuery) {
        try {
            // Create request body
            SubmissionRequest request = new SubmissionRequest(finalQuery);

            // Create headers with JWT authorization
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken); // JWT token

            // Create HTTP entity
            HttpEntity<SubmissionRequest> entity = new HttpEntity<>(request, headers);

            // Make POST request to the webhook URL (using the static test webhook URL)
            ResponseEntity<String> response = restTemplate.postForEntity(
                TEST_WEBHOOK_URL, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Submission response: " + response.getBody());
                return true;
            } else {
                System.out.println("Failed to submit solution. Status: " + response.getStatusCode());
                System.out.println("Response: " + response.getBody());
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error submitting solution: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}