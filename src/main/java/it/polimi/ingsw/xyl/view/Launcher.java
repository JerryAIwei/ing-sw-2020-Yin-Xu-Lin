package it.polimi.ingsw.xyl.view;

import it.polimi.ingsw.xyl.network.server.Server;
import it.polimi.ingsw.xyl.view.cli.CLI;
import it.polimi.ingsw.xyl.view.gui.GUI;

/**
 * add -Dprism.forceGPU=true in Run/Debug Configure VM options
 * */
public class Launcher {
    public static void main(final String[] args) {
        if(args.length == 0){
            System.out.println("-S for server, -C for client");
        }else if (args[0].equals("-S")){
            Server.main(args);
        }else if (args[0].equals("-C")) {
            if (args[1].equals("-c"))
                CLI.main(args);
            else if (args[1].equals("-g"))
                GUI.main(args);
        }
    }
}
