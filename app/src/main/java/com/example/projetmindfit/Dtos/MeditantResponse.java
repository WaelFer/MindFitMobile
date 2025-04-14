package com.example.projetmindfit.Dtos;

public class MeditantResponse {
    private String responseMessage;

    public MeditantResponse() {
    }

    public MeditantResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
