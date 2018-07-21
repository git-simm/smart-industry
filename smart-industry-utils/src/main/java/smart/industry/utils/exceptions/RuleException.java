package smart.industry.utils.exceptions;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class RuleException extends BindException {
    private static final long serialVersionUID = 1623027510104774925L;

    public RuleException(BindingResult bindingResult) {
        super(bindingResult);
    }
}
