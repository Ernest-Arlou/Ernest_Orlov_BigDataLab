package by.epam.bigdatalab.service;

import by.epam.bigdatalab.service.impl.PoliceAPIServiceImp;

public final class PoliceApiHolder {
    private final PoliceAPIService policeAPIService = new PoliceAPIServiceImp();

    private PoliceApiHolder() {
    }

    public static PoliceApiHolder getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public PoliceAPIService getPoliceAPIService() {
        return policeAPIService;
    }

    public static class SingletonHolder {
        public static final PoliceApiHolder HOLDER_INSTANCE = new PoliceApiHolder();
    }
}
