package model;

import classes.dto.EntitiesDTO;
import classes.dto.PropertyDTO;
import classes.dto.RuleDTO;
import utils.enums;

import java.util.List;

public class SimulationDetailsTreeModel {
    public static SimulationTreeItem getEnvironmentPropertiesRoot(List<PropertyDTO> environmentProperties) {
        SimulationTreeItem root = new SimulationTreeItem("Environment Properties", enums.DetailType.TITLE);
        environmentProperties.forEach(e-> {
            root.getChildren().add(new SimulationTreeItem(e.getName(), enums.DetailType.PROPERTY,e));
        });
        return root;
    }

    public static SimulationTreeItem getEntitiesRoot(List<EntitiesDTO> entities) {
        SimulationTreeItem root = new SimulationTreeItem("Entities", enums.DetailType.TITLE);
        entities.forEach(e -> {
            SimulationTreeItem entity = new SimulationTreeItem(e.getName(), enums.DetailType.ENTITY);
            e.getProperties().forEach(p-> {
                entity.getChildren().add(new SimulationTreeItem(p.getName(), enums.DetailType.PROPERTY,p));

            });
            root.getChildren().add(entity);
        });
        return root;
    }

    public static SimulationTreeItem getRulesRoot(List<RuleDTO> rules) {
        SimulationTreeItem root = new SimulationTreeItem("Rules", enums.DetailType.TITLE);
        rules.forEach(r -> {
            SimulationTreeItem rule = new SimulationTreeItem(r.getName(), enums.DetailType.RULE,r);
            r.getActions().forEach(a -> {
                rule.getChildren().add(new SimulationTreeItem(a.getActionType(), enums.DetailType.ACTION,a));
            });
            root.getChildren().add(rule);
        });
        return root;
    }
}
