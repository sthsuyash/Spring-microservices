package com.suyash.review_service.exception;

/**
 * An exception that is thrown when a review is not found.
 */
public class ReviewNotFoundException extends RuntimeException{
    public ReviewNotFoundException(String message) {
        super(message);
    }
}
