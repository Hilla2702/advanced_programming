package config;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import test.Agent;
import test.TopicManagerSingleton;

public class GenericConfig implements Config {
    private List<Agent> agents;
    private String confFile;

    public GenericConfig() {
        agents = new ArrayList<>();
        this.confFile = null;
    }

    public void setConfFile(String file) {
        if (this.confFile.equals(file)) {
            this.confFile = file;
            this.close();
        }
    }

    @Override
    public void create(){
        List<String>file_lines;
        try {
            file_lines = Files.readAllLines(Paths.get(this.confFile));
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("error can not read this file " + this.confFile + " in config " + this.getName());
            return;
        }

        if (file_lines.size()%3!=0){
            System.out.println("Incorrect input of the file:"+this.confFile);
            return;
        }

        for(int i= 0; i<file_lines.size();i=i+3){
            String [] subs= file_lines.get(i+1).split(",");
            String [] pubs= file_lines.get(i+1).split(",");

            try{
                Class<?>
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
