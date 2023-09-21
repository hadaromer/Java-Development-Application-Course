package classes.dto;

import classes.Termination;

public class TerminationDTO {
    private int ticks = -1;
    private int seconds = -1;
    private boolean byUser;

    public TerminationDTO(Termination termination) {
        this.ticks = termination.getTicks();
        this.seconds = termination.getSeconds();
        this.byUser = termination.isByUser();
    }

    public int getTicks() {
        return ticks;
    }

    public int getSeconds() {
        return seconds;
    }

    public boolean isByUser() {
        return byUser;
    }

    @Override
    public String toString() {
        return "By ticks = " + ticks +
                "\nBy seconds = " + seconds +"\n";
    }
}
