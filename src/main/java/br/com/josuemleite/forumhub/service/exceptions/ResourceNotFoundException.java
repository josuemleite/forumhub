package br.com.josuemleite.forumhub.service.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Object id) {
        super("Resource not found. ID: " + id);
    }
}
