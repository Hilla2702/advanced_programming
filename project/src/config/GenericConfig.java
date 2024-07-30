package config;

import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import graph.Agent;
import graph.ParallelAgent;
import graph.TopicManagerSingleton;

public class GenericConfig implements Config {
    private List<Agent> agents;
    private String confFile;

    public GenericConfig() {
        agents = new ArrayList<>();
        this.confFile = null;
    }

    public void setConfFile(String file) {
        if (!this.confFile.equals(file)) {
            this.confFile = file;
            this.close();
        }
    }

    @Override
    public void create() {
        List<String> file_lines;
        int capacity = 50;
        try {
            file_lines = Files.readAllLines(Paths.get(this.confFile));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error can not read this file " + this.confFile + " in config " + this.getName());
            return;
        }

        if (file_lines.size() % 3 != 0) {
            System.out.println("Incorrect input of the file:" + this.confFile);
            return;
        }

        for (int i = 0; i < file_lines.size(); i = i + 3) {
            String className = file_lines.get(i);
            String[] subs = file_lines.get(i + 1).split(",");
            String[] pubs = file_lines.get(i + 2).split(",");

            try {
                Class<?> agentClass = Class.forName(className);
                Constructor<?> constructor = agentClass.getConstructor(String[].class, String[].class);
                Agent agent = (Agent) constructor.newInstance((Object) subs, (Object) pubs);
                Agent parallelAgent = new ParallelAgent(agent, capacity);

                if (agents.stream().noneMatch(parallelAgent::equals)) {
                    agents.add(parallelAgent);
                } else {
                    parallelAgent.close();
                }
            } catch (ClassNotFoundException e) {
                System.out.println("No class matches line " + (i + 1) + ": " + className + " in file " + this.confFile);
            } catch (NoSuchMethodException e) {
                System.out
                        .println("No suitable constructor found for class " + className + " in file " + this.confFile);
            } catch (Exception e) {
                System.out.println("Failed to instantiate class " + className + " in file " + this.confFile);
                e.printStackTrace();
            }

        }

    }

    @Override
    public String getName() {
        return "GenericConfig";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void close() {
        for (Agent a : this.agents) { // Close all agents in List
            a.close();
        }
        TopicManagerSingleton.get().clear();
    }

}
