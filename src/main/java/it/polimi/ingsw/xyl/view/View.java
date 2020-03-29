package it.polimi.ingsw.xyl.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class View{

    private Scanner scanner;

    private PrintStream output;

    public View(InputStream inputStream, OutputStream output) {
        this.scanner = new Scanner(inputStream);
        this.output = new PrintStream(output);
    }


//    @Override
//    public void update(Observable o, Object arg) {
//        if(!(o instanceof ModelView)){
//            throw new IllegalArgumentException();
//        }
//        showModel((ModelView)o);
//    }

}
