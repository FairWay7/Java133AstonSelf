package org.example.webapp.model.result;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Result<T> {
    private final T data;
    private final List<String> errors;
    private final boolean success;

    private Result(T data, List<String> errors, boolean success) {
        this.data = data;
        this.errors = errors != null ? errors : Collections.emptyList();
        this.success = success;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, true);
    }

    public static <T> Result<T> failure(List<String> errors) {
        return new Result<>(null, errors, false);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public Optional<T> getData() {
        return isSuccess() ? Optional.of(data) : Optional.empty();
    }

    public List<String> getErrors() {
        return errors;
    }
}
