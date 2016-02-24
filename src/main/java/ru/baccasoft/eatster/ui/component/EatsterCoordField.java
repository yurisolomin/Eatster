package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class EatsterCoordField extends HorizontalLayout {

    private static final long serialVersionUID = 1758219332186413685L;
    TextField numFieldInt = new TextField();
    TextField numFieldFloat = new TextField();
    private static final int DECIMAL_PRECISION = 4;
    private static final float WIDTH_INT = 1.4f;
    private static final float WIDTH_FLOAT = 1.8f;
    private double coordOld = 0f;
    private final String caption;
    
    public EatsterCoordField(String caption) {
        super();
        this.caption = caption;
        //super.addComponent(new Label(caption));
        super.addComponent(numFieldInt);
        super.addComponent(new Label("."));
        super.addComponent(numFieldFloat);
        numFieldInt.addValidator(new RegexpValidator("\\d{1,3}", "В целой части координаты допустимо значение от 0 до 999"));
        numFieldFloat.addValidator(new RegexpValidator("\\d{2,4}", "В дробной части координаты допустимо значение от 2х до 4х цифр"));
        numFieldInt.setWidth(WIDTH_INT,Unit.CM);
        numFieldFloat.setWidth(WIDTH_FLOAT,Unit.CM);
    }

    private static char decimalSeparator() {
        DecimalFormat format=(DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols symbols=format.getDecimalFormatSymbols();
        char sep=symbols.getDecimalSeparator();
        return sep;
    }

    private static String coordToString(double coord) {
        return String.format("%."+DECIMAL_PRECISION+"f",coord);
    }
    private static double coordToFloat(String partInt, String partFloat, double defValue) {
        String coordAsString = partInt.trim()+decimalSeparator()+partFloat.trim();
        double result;
        try {
            DecimalFormat format=(DecimalFormat) DecimalFormat.getInstance();
            result = format.parse(coordAsString).doubleValue();
//            result = Double.parseDouble(coordAsString);
        } catch (Exception e) {
            result = defValue;
        }
        return result;
    }
            
    public void setValue(double coord) {
        String result = coordToString(coord);
        result = result.replace(decimalSeparator(),';');
        String args[] = result.split(";");
        numFieldInt.setValue(args[0]);
        numFieldFloat.setValue(args[1]);
        coordOld = coord;
    }
    
    public double getValue() {
        double result = coordToFloat(numFieldInt.getValue(), numFieldFloat.getValue(), coordOld);
        return result;
    }
    
    @Override
    public void setReadOnly(boolean readOnly) {
        numFieldInt.setReadOnly(readOnly);
        numFieldFloat.setReadOnly(readOnly);
    }
}
