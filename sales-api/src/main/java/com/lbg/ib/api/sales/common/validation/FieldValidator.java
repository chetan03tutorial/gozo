/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.common.validation;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.validation.*;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.regex.Pattern;

import static java.lang.String.format;

@Component
public class FieldValidator {

    @Autowired
    private CountryFieldValidation countryFieldValidation;
    @Autowired
    private ValidationService      validationService;

    @Autowired
    private LoggerDAO              logger;

    public ValidationError validateInstanceFields(Object instance) {
        try {
            validateInstanceFields(instance, null);
        } catch (ValidationException e) {
            logger.logException(this.getClass(), e);
            return e.getValidationError();
        }
        return null;
    }

    public boolean validateInstanceFields(Object instance, AccountType accountType) throws ValidationException {
        Field[] declaredFields = instance.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Object fieldValue = extractStringFieldValue(declaredField, instance);

            doRequiredFieldValidation(accountType, declaredField, fieldValue);
            if (fieldValue != null) {
                validateDetails(instance, accountType, declaredField,
						fieldValue);
            }
        }
        return true;
    }

	/**
	 * @param instance
	 * @param accountType
	 * @param declaredField
	 * @param fieldValue
	 * @throws ValidationException
	 */
	private void validateDetails(Object instance, AccountType accountType,
			Field declaredField, Object fieldValue) throws ValidationException {
		doStringFieldValidation(declaredField, fieldValue);

		validateCustom(declaredField, fieldValue);

		validateCountryCombo(declaredField, fieldValue);

		validateNumber(declaredField, fieldValue);

		if (fieldValue != null && fieldValue.getClass().isAnnotationPresent(Validate.class)
		        && !instance.getClass().equals(declaredField.getType())
		        && !(validationService.isOptional(declaredField))) {
		    validateInstanceFields(fieldValue, accountType);
		}

		validateBinaryFieldValue(declaredField, fieldValue);

		doValidateAnnotationCheck(accountType, declaredField, fieldValue);

		if (fieldValue.getClass().isArray()
		        && fieldValue.getClass().getComponentType().isAnnotationPresent(Validate.class)
		        && !instance.getClass().equals(fieldValue.getClass().getComponentType())) {
		    for (Object o : (Object[]) fieldValue) {
		        validateInstanceFields(o, accountType);
		    }
		}
	}

    private void doValidateAnnotationCheck(AccountType accountType, Field declaredField, Object fieldValue)
            throws ValidationException {
        if (Collection.class.isAssignableFrom(declaredField.getType())) {
            ParameterizedType pType = (ParameterizedType) declaredField.getGenericType();
            Class<?> clazzName = (Class<?>) pType.getActualTypeArguments()[0];
            if (clazzName.isAnnotationPresent(Validate.class)) {

                @SuppressWarnings("rawtypes")
                Collection c = (Collection) fieldValue;
                Object[] oArray = new Object[c.size()];
                c.toArray(oArray);
                for (Object o : oArray) {
                    validateInstanceFields(o, accountType);
                }
            }
        }
    }

    private boolean doStringFieldValidation(Field declaredField, Object fieldValue) throws ValidationException {
        if (declaredField.isAnnotationPresent(StringFieldValidation.class) && !isFieldEmpty(fieldValue)
                && fieldValue instanceof String) {
            validatePattern(declaredField, (String) fieldValue);

            validateMaxLength(declaredField, (String) fieldValue);

            validateMinLength(declaredField, (String) fieldValue);
        }
        return true;
    }

    private boolean doRequiredFieldValidation(AccountType accountType, Field declaredField, Object fieldValue)
            throws ValidationException {
        if (declaredField.isAnnotationPresent(RequiredFieldValidation.class)
                && !(validationService.isOptional(declaredField))) {
            if (accountType == null) {
                // doValidation - accountType param would be null for all
                // the services except offer service
                validateRequired(declaredField, fieldValue);
            } else if (declaredField.isAnnotationPresent(AccountTypeValidation.class)) {
                // doValidation against the accountType param
                AccountType[] types = declaredField.getAnnotation(AccountTypeValidation.class).value();
                for (AccountType type : types) {
                    if (type.equals(accountType)) {
                        validateRequired(declaredField, fieldValue);
                        break;
                    }
                }
            } else {
                // doValidation
                validateRequired(declaredField, fieldValue);
            }
        }
        return true;
    }

    private boolean validateCustom(Field field, Object value) throws ValidationException {
        /*
         * Annotation annotation =
         * field.getAnnotation(CustomFieldValidation.class).
         */
        if (field.isAnnotationPresent(CustomFieldValidation.class)) {
            Class<? extends FieldValidation> clazz = field.getAnnotation(CustomFieldValidation.class).validation();
            FieldValidation validation;
            try {
                validation = clazz.getConstructor().newInstance();
                if (validation.doesFail(value)) {
                    throw new ValidationException(new ValidationError(
                            format("Field validation for '%s' fails: %s", field.getName(), validation.message())));
                }
            } catch (InstantiationException e) {
                logger.logException(this.getClass(), e);
            } catch (IllegalAccessException e) {
                logger.logException(this.getClass(), e);
            } catch (IllegalArgumentException e) {
                logger.logException(this.getClass(), e);
            } catch (InvocationTargetException e) {
                logger.logException(this.getClass(), e);
            } catch (NoSuchMethodException e) {
                logger.logException(this.getClass(), e);
            } catch (SecurityException e) {
                logger.logException(this.getClass(), e);
            }
        }
        return true;
    }

    private boolean validateCountryCombo(Field field, Object value) throws ValidationException {
        if (field.isAnnotationPresent(CountryValidation.class) && countryFieldValidation.doesFail(value)) {
            throw new ValidationException(new ValidationError(
                    format("Field validation for '%s' fails: %s", field.getName(), countryFieldValidation.message())));
        }
        return true;
    }

    private boolean validateRequired(Field field, Object fieldValue) throws ValidationException {
        if (isFieldEmpty(fieldValue)) {
            throw new ValidationException(new ValidationError(
                    format("'%s' required to be set but it was '%s'", field.getName(), fieldValue)));
        }
        int size = -1;
        if (fieldValue instanceof Iterable) {
            size = IteratorUtils.toList(((Iterable) fieldValue).iterator()).size();
        } else if (fieldValue.getClass().isArray()) {
            size = ((Object[]) fieldValue).length;
        }
        if (size == 0) {
            throw new ValidationException(
                    new ValidationError(format("'%s' required to be set but it was 'empty'", field.getName())));
        } else {
            int max = field.getAnnotation(RequiredFieldValidation.class).max();
            if (size > max) {
                throw new ValidationException(new ValidationError(
                        format("'%s' size can be maximum '%s' it is %s", field.getName(), max, size)));
            }
        }
        return true;
    }

    private boolean isFieldEmpty(Object fieldValue) {
        return fieldValue == null || (fieldValue instanceof String && "".equals(fieldValue));
    }

    private boolean validateNumber(Field field, Object fieldValue) throws ValidationException {
        if (field.isAnnotationPresent(IntegerFieldValidation.class) && fieldValue instanceof Integer) {
            Integer intFieldValue = (Integer) fieldValue;
            int min = field.getAnnotation(IntegerFieldValidation.class).min();
            int max = field.getAnnotation(IntegerFieldValidation.class).max();
            if (intFieldValue > max) {
                throw new ValidationException(new ValidationError(format(
                        "'%s' is greater than maximum limit '%s' in field '%s'", intFieldValue, max, field.getName())));
            }
            if (intFieldValue < min) {
                throw new ValidationException(new ValidationError(format(
                        "'%s' is lower than minimum limit '%s' in field '%s'", intFieldValue, min, field.getName())));
            }
        }
        if (field.isAnnotationPresent(BigDecimalFieldValidation.class) && fieldValue instanceof BigDecimal) {
            BigDecimal bigDecimalFieldValue = (BigDecimal) fieldValue;
            String min = field.getAnnotation(BigDecimalFieldValidation.class).min();
            String max = field.getAnnotation(BigDecimalFieldValidation.class).max();
            if (bigDecimalFieldValue.compareTo(new BigDecimal(max))==1) {
                throw new ValidationException(new ValidationError(format(
                        "'%s' is greater than maximum limit '%s' in field '%s'", bigDecimalFieldValue, max, field.getName())));
            }
            if (bigDecimalFieldValue.compareTo(new BigDecimal(min))==-1) {
                throw new ValidationException(new ValidationError(format(
                        "'%s' is lower than minimum limit '%s' in field '%s'", bigDecimalFieldValue, min, field.getName())));
            }
        }
        return true;
    }

    private boolean validateMaxLength(Field field, String fieldValue) throws ValidationException {
        int maxLength = field.getAnnotation(StringFieldValidation.class).maxLength();
        if (fieldValue.length() <= maxLength) {
            return true;
        }
        throw new ValidationException(new ValidationError(
                format("'%s' is longer than '%s' characters in field '%s'", fieldValue, maxLength, field.getName())));
    }

    public ValidationService getValidationService() {
        return validationService;
    }

    public void setValidationService(ValidationService validationService) {
        this.validationService = validationService;
    }

    private boolean validateMinLength(Field field, String fieldValue) throws ValidationException {
        int minLength = field.getAnnotation(StringFieldValidation.class).minLength();
        if (fieldValue.length() >= minLength) {
            return true;
        }
        throw new ValidationException(new ValidationError(
                format("'%s' is shorter than '%s' characters in field '%s'", fieldValue, minLength, field.getName())));
    }

    private boolean validatePattern(Field field, String fieldValue) throws ValidationException {
        StringFieldValidation annotation = field.getAnnotation(StringFieldValidation.class);
        String pattern = annotation.pattern();
        String message = annotation.message();
        if ("".equals(pattern)) {
            return true;
        }
        if (Pattern.compile(pattern).matcher(fieldValue).matches()) {
            return true;
        }
        throw new ValidationException(new ValidationError("".equals(message)
                ? format("'%s' is not matching with format in field '%s'", fieldValue, field.getName()) : message));
    }

    private Object extractStringFieldValue(Field field, Object instance) {
        try {
            field.setAccessible(true);
            Object value = field.get(instance);
            if (value == null) {
                return null;
            }
            return value;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean validateBinaryFieldValue(Field field, Object fieldValue) throws ValidationException {
        if (field.isAnnotationPresent(BinaryStringValidation.class) && fieldValue instanceof String) {
            String strFieldValue = (String) fieldValue;
            if (!BinaryType.validateBinaryString(strFieldValue)) {
                throw new ValidationException(new ValidationError(
                        format("'%s' is not matching with format in field '%s'", strFieldValue, field.getName())));
            }
        }
        return true;
    }

}
