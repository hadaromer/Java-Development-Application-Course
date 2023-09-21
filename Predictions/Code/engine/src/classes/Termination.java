package classes;

import resources.generated.PRDBySecond;
import resources.generated.PRDByTicks;
import resources.generated.PRDTermination;

import java.io.Serializable;

public class Termination implements Serializable {
    private int ticks = -1;
    private int seconds = -1;
    private boolean byUser;

    public Termination(PRDTermination termination) {
        if (termination.getPRDByUser() != null) {
            byUser = true;
        } else {
            byUser = false;
            for (Object option : termination.getPRDBySecondOrPRDByTicks()) {
                if (option.getClass() == PRDByTicks.class) {
                    this.ticks = ((PRDByTicks) option).getCount();
                } else {
                    this.seconds = ((PRDBySecond) option).getCount();
                }
            }
        }
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean isByUser() {
        return byUser;
    }

    @Override
    public String toString() {
        return "Termination{" +
                "ticks=" + ticks +
                ", seconds=" + seconds +
                '}';
    }
}
