package by.epam.bigdatalab.service;

public final class PoliceApiServiceHolder {

    private static final PoliceAPIService policeAPIService = new PoliceAPIService();

    private PoliceApiServiceHolder() {
    }

    public static PoliceApiServiceHolder getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public PoliceAPIService getPoliceAPIService() {
        return policeAPIService;
    }

    public static class SingletonHolder {
        public static PoliceApiServiceHolder HOLDER_INSTANCE = new PoliceApiServiceHolder();
    }
}