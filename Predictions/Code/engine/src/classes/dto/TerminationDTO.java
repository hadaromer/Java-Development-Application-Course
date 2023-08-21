package classes.dto;

import classes.Termination;

public class TerminationDTO {
    int ticks = -1;
    int seconds = -1;

    public TerminationDTO(Termination termination) {
        this.ticks = termination.getTicks();
        this.seconds = termination.getSeconds();
    }

    public int getTicks() {
        return ticks;
    }

    public int getSeconds() {
        return seconds;
    }

    @Override
    public String toString() {
        return "\nBy ticks = " + ticks +
                "\nBy seconds = " + seconds +"\n";
    }
}
