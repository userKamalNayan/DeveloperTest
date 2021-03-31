package com.sampleassignment.developertext.Events;

public class StopServiceEvent {
    boolean stopService;

    public StopServiceEvent(boolean stopService) {
        this.stopService = stopService;
    }

    public boolean isStopService() {
        return stopService;
    }

    public void setStopService(boolean stopService) {
        this.stopService = stopService;
    }
}
