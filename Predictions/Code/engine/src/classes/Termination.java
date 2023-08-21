package classes;

import resources.generated.PRDBySecond;
import resources.generated.PRDByTicks;
import resources.generated.PRDTermination;

import java.io.Serializable;

public class Termination implements Serializable {
    int ticks = -1;
    int seconds = -1;

    public Termination(PRDTermination termination) {
        for (Object option : termination.getPRDByTicksOrPRDBySecond()) {
            if (option.getClass() == PRDByTicks.class) {
                this.ticks = ((PRDByTicks) option).getCount();
            } else {
                this.seconds = ((PRDBySecond) option).getCount();
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

    @Override
    public String toString() {
        return "Termination{" +
                "ticks=" + ticks +
                ", seconds=" + seconds +
                '}';
    }
}
