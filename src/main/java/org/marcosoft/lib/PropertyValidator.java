package org.marcosoft.lib;

/**
 * PropertyValidator.
 *
 */
public interface PropertyValidator {
    void validate(String property, String value) throws ValidatorException;
}
