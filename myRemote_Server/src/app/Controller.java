package app;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;

public class Controller implements Runnable {

    // remote specific members
    private static ServerSocket server;
    private static Socket client;
    private static Robot robot;
    private static BufferedReader in;
    private static boolean isConnected;

    Controller() {
        try {
            server = new ServerSocket(0); // initializes server socket with random open port
        } catch (IOException e) {
            System.err.println("Error in creating ServerSocket");
            System.exit(-1);
        }
    }

    /**
     * This method finds the IP and port of the PC, and returns the info in a String array back to the UI where
     * it is displayed to the user.
     */
    String[] getConnectionInfo() {

        // attempt to get server IP
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.err.println("Unknown Host: " + e);
        }

        String[] info = {null, null}; // will hold IP and Port

        // get string rep of IP address
        info[0] = localhost.getHostAddress();
        // get string rep of port
        info[1] = Integer.toString(server.getLocalPort());

        return info;
    }

    public void run() {

        // listen for connection from client and set up input stream
        try {
            // create instance of Robot to simulate key presses
            robot = new Robot();
            // listen for client and accept it
            client = server.accept();
            // get input stream to receive data from client side
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            isConnected = true;
        } catch(IOException e) {
            System.err.println("Error in opening Socket");
            System.exit(-1);
        } catch(AWTException e) {
            System.err.println("Error in creating Robot");
            System.exit(-1);
        }

        // while connected, read in client input and interpret
        while (isConnected) {
            try {
                // used to store read in line from input stream
                String line = in.readLine(); //read input from client
                System.out.println(line); //print whatever we get from client

                Desktop desktop;
                if (line.equalsIgnoreCase("left")) {
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                }
                else if (line.equalsIgnoreCase("right")) {
                    robot.mousePress(InputEvent.BUTTON3_MASK);
                    robot.mouseRelease(InputEvent.BUTTON3_MASK);
                }
                else if (line.equalsIgnoreCase("space")) {
                    robot.keyPress(KeyEvent.VK_SPACE);
                    robot.keyRelease(KeyEvent.VK_SPACE);
                }
                else if (line.equalsIgnoreCase("vol_up")) {
                    robot.keyPress(KeyEvent.VK_UP);
                    robot.keyRelease(KeyEvent.VK_UP);
                }
                else if (line.equalsIgnoreCase("vol_down")) {
                    robot.keyPress(KeyEvent.VK_DOWN);
                    robot.keyRelease(KeyEvent.VK_DOWN);
                }
                else if (line.equalsIgnoreCase("fullscreen")) {
                    robot.keyPress(KeyEvent.VK_F11);
                    robot.keyRelease(KeyEvent.VK_F11);
                }
                else if (line.equalsIgnoreCase("scroll_up")) {
                    robot.keyPress(KeyEvent.VK_PAGE_UP);
                    robot.keyRelease(KeyEvent.VK_PAGE_UP);
                }
                else if (line.equalsIgnoreCase("scroll_down")) {
                    robot.keyPress(KeyEvent.VK_PAGE_DOWN);
                    robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
                }
                else if (line.equalsIgnoreCase("netflix")) {
                    desktop = Desktop.getDesktop();
                    desktop.browse(new URI("http://netflix.com/browse"));
                }
                else if (line.equalsIgnoreCase("youtube")) {
                    desktop = Desktop.getDesktop();
                    desktop.browse(new URI("http://youtube.com"));
                }
                else if (line.equalsIgnoreCase("exit")) {
                    break;
                }
                else if (line.equalsIgnoreCase("tab")) {
                    robot.keyPress(KeyEvent.VK_TAB);
                    robot.keyRelease(KeyEvent.VK_TAB);
                }
                // Code Reference: https://stackoverflow.com/questions/29665534/type-a-string-using-java-awt-robot
                else if (line.contains("$$$")) { // implies keyboard input
                    String input = line.substring(3); // remove "$$$"

                    // if the user clicked enter without entering input, register the enter key
                    if (input.isEmpty()) {
                        // press enter
                        robot.keyPress(KeyEvent.VK_ENTER);
                        robot.keyRelease(KeyEvent.VK_ENTER);
                        continue;
                    }

                    // otherwise, paste inputted text on the screen
                    StringSelection stringSelection = new StringSelection(input);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // get clipboard
                    clipboard.setContents(stringSelection, stringSelection);

                    // CTRL + V to paste
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                }
                else if (line.contains(",")) { // implies a mouse movement in x,y format
                    float moveX = Float.parseFloat(line.split(",")[0]); // get X movement
                    float moveY = Float.parseFloat(line.split(",")[1]); // get Y movement
                    Point point = MouseInfo.getPointerInfo().getLocation(); // get current mouse position
                    float currentX = point.x;
                    float currentY = point.y;
                    robot.mouseMove((int)(currentX + moveX), (int)(currentY + moveY)); // move pointer to new location
                }

            } catch(IOException e) {
                System.err.println("Error reading line in");
                isConnected = false;
            } catch (URISyntaxException e) {
                System.err.println("Cannot open URI");
            } catch (NullPointerException e) {
                isConnected = false;
            }

        }

    }

    boolean getStatus() {
        return isConnected;
    }

    void closeSockets() {
        try {
            server.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
