package org.example.hm2.result;

import java.util.Collections;
import java.util.List;

public class Result<T> {
    private final T data;
    private final List<String> errors;
    private final boolean success;

    private Result(T data, List<String> errors, boolean success) {
        this.data = data;
        this.errors = errors != null ? errors : Collections.emptyList();
        this.success = success;
    }

    // TODO: дописать медоты success, failure, getErrors
}
