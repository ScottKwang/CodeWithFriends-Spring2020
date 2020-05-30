package util;

import java.util.TimerTask;

public class CustomTimerTask extends TimerTask {
    private boolean hasStarted = false;

    @Override
    public void run() {
        this.hasStarted = true;
        //rest of run logic here...
    }

    public boolean hasRunStarted() {
        return hasStarted;
    }
}
