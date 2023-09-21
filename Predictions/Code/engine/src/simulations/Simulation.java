package simulations;

import classes.Entity;
import classes.Rule;
import classes.World;
import classes.actions.Action;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Simulation implements Serializable {
    public enum State {INIT, RUNNING, PAUSED, ONE_FORWARD, ONE_PREVIOUS, STOPPED, FINISHED_BY_TICKS, FINISHED_BY_TIME, ERROR}

    private UUID uuid;
    private String startDate;
    private World world;
    private int currentTicks;
    private SimulationTimer simulationTimer;
    private State state;
    private LinkedList<World> ticksHistory;
    private String errorMessage;

    public Simulation(World world) {
        uuid = UUID.randomUUID();
        this.world = world;
        this.currentTicks = 0;
        simulationTimer = new SimulationTimer();
        state = State.INIT;
        ticksHistory = new LinkedList<>();
    }

    public void Start() {
        this.state = State.RUNNING;
        this.world.SetInitLocationForEntities();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy | HH.mm.ss");
        this.startDate = dateFormat.format(new Date());
        ticksHistory.add(new World(world, false));
        simulationTimer.Start();
        Run();
    }

    public boolean Pause() {
        if (!isFinished()) {
            this.state = State.PAUSED;
            this.simulationTimer.Pause();
            return true;
        } else {
            return false;
        }
    }

    public void Forward() {
        this.state = State.ONE_FORWARD;
    }

    public void Previous() {
        this.state = State.ONE_PREVIOUS;
    }

    public boolean Resume() {
        if (!isFinished()) {
            this.state = State.RUNNING;
            this.simulationTimer.Resume();
            return true;
        } else {
            return false;
        }
    }

    public void Stop() {
        this.state = State.STOPPED;
        this.simulationTimer.Stop();
    }

    public void Error(String errorMessage) {
        this.state = State.ERROR;
        this.errorMessage = errorMessage;
        this.simulationTimer.Stop();
    }

    private boolean isFinished() {
        if (state == State.STOPPED) {
            this.simulationTimer.Stop();
            return true;
        }
        if (world.getTermination().getTicks() != -1 && currentTicks > world.getTermination().getTicks()) {
            state = State.FINISHED_BY_TICKS;
            this.simulationTimer.Stop();
            return true;
        }
        if (world.getTermination().getSeconds() != -1 && simulationTimer.getTime() > world.getTermination().getSeconds()) {
            state = State.FINISHED_BY_TIME;
            this.simulationTimer.Stop();
            return true;
        }
        return false;
    }

    private void Run() {
        World presentWorld = null;
        while (!isFinished()) {
            try {
                Thread.sleep(250);
            } catch (Exception ignored) {
            }
            if (state == State.RUNNING || state == State.ONE_FORWARD) {
                currentTicks++;
                if (currentTicks < ticksHistory.size()) {
                    this.world = ticksHistory.get(currentTicks);
                } else {
                    if (presentWorld != null) {
                        this.world = presentWorld;
                        presentWorld = null;
                    }
                    handleTick();
                    ticksHistory.add(new World(world, false));
                }
                if (this.state == State.ONE_FORWARD) {
                    this.state = State.PAUSED;
                }
            }

            if (state == State.ONE_PREVIOUS && currentTicks > 0) {
                if (presentWorld == null) {
                    presentWorld = this.world;
                }
                currentTicks--;
                this.world = ticksHistory.get(currentTicks);
                this.state = State.PAUSED;
            }
        }
    }

    private void handleTick() {
        List<Runnable> actionsForEndTick = new ArrayList<>();
        world.moveEntities();
        List<Rule> activeRules = world.getRules().values().stream()
                .filter(r -> r.isActive(currentTicks))
                .collect(Collectors.toList());
        List<Action> actions = new ArrayList<>();
        activeRules.forEach(rule -> actions.addAll(rule.getActions()));
        world.getEntities().values().forEach(group -> {
            group.getEntities().forEach(entity -> {
                actions.forEach(action -> {
                    if (action.getEntity().equals(entity.getGroup())) {
                        if (action.getSecondayEntity() != null) {
                            List<Entity> secondEntities = action.getSecondayEntity().getEntities(world, currentTicks);
                            secondEntities.forEach(secondEntity -> {
                                action.Act(world, entity, secondEntity, currentTicks, actionsForEndTick);
                            });
                        } else {
                            action.Act(world, entity, null, currentTicks, actionsForEndTick);
                        }
                    }
                });
            });
        });

        actionsForEndTick.forEach(Runnable::run);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getStartDate() {
        return startDate;
    }

    public World getWorld() {
        return world;
    }

    public int getCurrentTicks() {
        return currentTicks;
    }

    public State getState() {
        return state;
    }

    public int getTime() {
        return simulationTimer.getTime();
    }

    public LinkedList<World> getTicksHistory() {
        return ticksHistory;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
