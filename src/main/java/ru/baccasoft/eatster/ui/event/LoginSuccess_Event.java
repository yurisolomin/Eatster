package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import ru.baccasoft.eatster.model.PartnerModel;

public class LoginSuccess_Event implements Event {

    private PartnerModel partnerModel = null;

    public LoginSuccess_Event(PartnerModel restaurant) {
        this.partnerModel = restaurant;
    }

    public PartnerModel getPartnerModel() {
        return partnerModel;
    }


}
