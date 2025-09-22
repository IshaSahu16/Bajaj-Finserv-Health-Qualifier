package com.bajajfinserv.healthqualifier.model;

public class SubmissionRequest {
    private String finalQuery;

    // Constructors
    public SubmissionRequest() {}

    public SubmissionRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    // Getters and Setters
    public String getFinalQuery() {
        return finalQuery;
    }

    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    @Override
    public String toString() {
        return "SubmissionRequest{" +
                "finalQuery='" + finalQuery + '\'' +
                '}';
    }
}