package com.example.user_service.utils;


import com.example.user_service.exception.ConflictException;
import com.example.user_service.exception.NotFoundException;

import java.util.Optional;


public class CommonObjectUtils {

    public static <T> void requireAbsent(Optional<T> object, String conflictMessage) throws ConflictException {
        if (object.isPresent()) {
            throw new ConflictException(conflictMessage);
        }
    }

    public static <T> T requirePresent(Optional<T> object, String presenceMessage) throws NotFoundException {
        if (object.isPresent()) { return object.get(); }
        else { throw new NotFoundException(presenceMessage); }
    }
}
