package nodes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class NodesStarter {
    public void startAllNodes() throws IOException {

        ProcessBuilder builder = new ProcessBuilder("cmd.exe" , "/c" , "cd \"C:\\Users\\User\\Desktop\\Newfolder \" &  bash nodesScript.sh ");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line="";
        while (line != null){
            line = bufferedReader.readLine();
            System.out.println(line);
        }





        /*run a script to run all nodes*/
    }
}
