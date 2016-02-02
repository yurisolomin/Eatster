package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.Property;
import com.vaadin.data.validator.AbstractValidator;
import org.vaadin.addons.maskedtextfield.MaskedTextField;
import ru.baccasoft.utils.logging.Logger;

public class EatsterPhoneField extends MaskedTextField {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(RestaurantInformationPanel.class);
/*
https://github.com/EduFrazao/vaadin-masked-textfield/tree/v0.19
# - any digit
U - upper-case letter
L - lower-case letter
? - any letter
A - any number or character
* - anything
H - hex sign (0-9, a-f or A-F)
' - Escape character, used to escape any of the special formatting characters.
~ - +/- sign    
*/
    private static final String DELIMITERS = "()-";
    private static final String MASK = "*#(###)###-####";
    private static final String VALIDATOR_MESSAGE = "Заполните номер телефона по маске +7(###)###-#### или _8(###)###-####";
//    private static final String EMPTY_VALIDATOR_MESSAGE = "Номер телефона должен быть заполнен";
    public static final String DEFAULT_VALUE = "+70000000000";
    public static final String EMPTY_VALUE = "0000000000"; //проверяется конец строки
    
    public EatsterPhoneField() {
        super(null, MASK);
//        super.addValidator( new EmptyValidator(EMPTY_VALIDATOR_MESSAGE) );
        super.addValidator( new PhoneValidator(VALIDATOR_MESSAGE) );
        super.setImmediate(false);
    }
    public EatsterPhoneField(String caption) {
        super(caption, MASK);
//        super.addValidator( new EmptyValidator(EMPTY_VALIDATOR_MESSAGE) );
        super.addValidator( new PhoneValidator(VALIDATOR_MESSAGE) );
        super.setImmediate(false);
    }
    
    private static String unmask(String value) {
        //старый формат записи, когда символ плюс опущен
        if (value.startsWith("7")) {
            value = "+"+value;
        }
        for (int i=0; i<DELIMITERS.length();++i) {
            String ch = DELIMITERS.substring(i,i+1);
            value = value.replace(ch,"");
        }
        value = value.replace(" ","");
        return value;
    }

    private String tomask(String unmaskedPhone) {
        if (unmaskedPhone == null) {
            return null;
        }
        if (unmaskedPhone.equals("")) {
            return "";
        }
        if (unmaskedPhone.startsWith("8")) {
            unmaskedPhone = " "+unmaskedPhone;
        }
        String mask = getMask();
        String maskedValue = "";
        for(int i=0,n=0; i<mask.length(); ++i) {
            String ch = mask.substring(i,i+1);
            if (!DELIMITERS.contains(ch)) {
                maskedValue += unmaskedPhone.substring(n,n+1);
                ++n;
                if (n >= unmaskedPhone.length()) {
                    break;
                }
            } else {
                maskedValue += ch;
            }
        }
        return maskedValue;
    }
    
    //приведем телефон к формату с маской
    private String fixmask(String phone) {
        //отрежем лишнее на случай хранения в БД номера с разделителями
        String unmasked = unmask(phone);
        //а теперь приведем к нужному формату
        String masked = tomask(unmasked);
        return masked;
    }
 
    @Override
    public void setPropertyDataSource(Property newDataSource) {    
        if (newDataSource == null) {
            super.setPropertyDataSource(newDataSource);
            return;
        }
        String masked = fixmask(newDataSource.toString());
        newDataSource.setValue(masked);
        super.setPropertyDataSource(newDataSource);
    }
    
    @Override
    public Property getPropertyDataSource() {
        Property property = super.getPropertyDataSource();
        if (property == null) {
            return null;
        }
        //отрежем разделители
        String unmasked = unmask(property.toString());
        property.setValue(unmasked);
        return property;
    }

    @Override
    public void setValue(String value) {
        value = fixmask(value);
        super.setValue(value);
    }
/*
    @Override
    public String getValue() {
        String value = super.getValue();
        value = unmask(value);
        value = value.replace(" ","");
        return value;
    }
*/    
    private static final class PhoneValidator extends AbstractValidator<String> {

        private static final long serialVersionUID = 1L;

        public PhoneValidator(String errorMessage) {
            super(errorMessage);
        }

        @Override
        public Class<String> getType() {
            return String.class;
        }

        @Override
        protected boolean isValidValue(String value) {
            if (value == null) {
                return false;
            }
            value = unmask(value);
            if (value.endsWith(EMPTY_VALUE)) {
                return false;
            }
            if (value.matches("\\+\\d{11}")) {
                return true;
            }
            if (value.matches("[8]\\d{10}")) {
                return true;
            }
            return false;
        }
    }
/*    
    private static final class EmptyValidator extends AbstractValidator<String> {

        private static final long serialVersionUID = 1L;

        public EmptyValidator(String errorMessage) {
            super(errorMessage);
        }

        @Override
        public Class<String> getType() {
            return String.class;
        }

        @Override
        protected boolean isValidValue(String value) {
            if (value == null) {
                return true;
            }
            value = unmask(value);
            if (value.endsWith(EMPTY_VALUE)) {
                return false;
            }
            if (value.equals("")) {
                return false;
            }
            return true;
        }
    }
*/    
}
