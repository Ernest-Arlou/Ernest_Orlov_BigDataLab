package by.epam.bigdatalab.service.factory;

import by.epam.bigdatalab.service.PoliceAPIService;
import by.epam.bigdatalab.service.impl.PoliceAPIServiceImp;

public final class ServiceFactory {
    private static volatile ServiceFactory instance;

    private final PoliceAPIService policeAPIService = new PoliceAPIServiceImp();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {

        ServiceFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (ServiceFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ServiceFactory();
                }
            }
        }
        return localInstance;
    }


    public PoliceAPIService getPoliceAPIService() {
        return policeAPIService;
    }
}
