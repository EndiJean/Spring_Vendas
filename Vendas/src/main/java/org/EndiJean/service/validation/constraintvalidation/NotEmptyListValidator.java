package org.EndiJean.service.validation.constraintvalidation;

import org.EndiJean.service.validation.NotEmptyList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NotEmptyListValidator implements ConstraintValidator<NotEmptyList, List> {

    @Override
    public void initialize(NotEmptyList constraintAnnotation) {
    }

    @Override
    public boolean isValid(List list, ConstraintValidatorContext context) {
        return list != null && !list.isEmpty();
    }
}
