package ru.baccasoft.eatster.thrift.server;

import org.apache.thrift.TProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.baccasoft.eatster.service.AverageCheckService;
import ru.baccasoft.eatster.service.ActionService;
import ru.baccasoft.eatster.service.CuisineService;
import ru.baccasoft.eatster.service.OperationService;
import ru.baccasoft.eatster.service.ParkingService;
import ru.baccasoft.eatster.service.PhotoService;
import ru.baccasoft.eatster.service.PushTokenService;
import ru.baccasoft.eatster.service.RestaurantService;
import ru.baccasoft.eatster.service.UserBonusService;
import ru.baccasoft.eatster.service.UserService;
import ru.baccasoft.eatster.thrift.generated.ThriftService;

@Service("ThriftServlet")
public class ThriftServlet extends AbstractThriftServlet {

    @Autowired
    private UserService userService;
    @Autowired
    PushTokenService pushTokenService;
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    CuisineService cuisineService;
    @Autowired
    ActionService actionService;
    @Autowired
    PhotoService photoService;
    @Autowired
    AverageCheckService averageCheckService;
    @Autowired
    OperationService operationService;
    @Autowired
    ParkingService parkingService;
    @Autowired
    UserBonusService userBonusService;

    @Override
    protected TProcessor createProcessor() {
        return new ThriftService.Processor(
                new ThriftHandler(
                        userService,
                        pushTokenService,
                        restaurantService,
                        cuisineService,
                        actionService,
                        photoService,
                        averageCheckService,
                        operationService,
                        parkingService,
                        userBonusService
                )
        );
    }

    /*
     public ThriftServlet() {
     super(
     new SalonService.Processor(new ThriftHandler()),
     new TBinaryProtocol.Factory()
     );
     }
     */
}
