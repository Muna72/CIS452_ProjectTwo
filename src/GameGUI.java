import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.util.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.border.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
    /**
     *
     * @author Muna Gigowski
     * @version 1.0 (September 2018)
     */
    public class GameGUI extends JFrame implements ActionListener, Runnable {


        //Declaring instance variables
        private int DELAY = 20;
        private boolean isRunning;
        private boolean firstTimeStartPressed;
        private boolean loop = true;
        private double totalTime;
        private double timeLeft;
        private double moveForwardTime;
        public Timer simTimer;
        private Random r = new Random();
        DecimalFormat df = new DecimalFormat("#.00");
        private JPanel input;
        FtpClientAndServer connectionInfo;
        FtpClientAndServer cmdLine;
        private JPanel commandArea;
        private JPanel searchArea;

        //define buttons
        JButton connect;
        JButton search;
        JButton go;

        //define text fields
        JTextField serverHostName;
        JTextField portNum;
        JTextField userName;
        JTextField hostName;
        JTextField keyword;
        JTextField command;

        //define JComboBoxes
        JComboBox<String> speedSelection;

        //define JLabels
        private JLabel serverHostnameLabel;
        private JLabel portNumLabel;
        private JLabel userNameLabel;
        private JLabel hostNameLabel;
        private JLabel speedLabel;
        private JLabel keywordLabel;
        private JLabel commandLabel;
        private JLabel inputLabel;


        //define menu items
        private JMenuBar menu;
        JMenu file;
        JMenuItem reset;
        JMenuItem quit;

        /**
         * Main method
         * @param args
         */
        public static void main(String[] args) {
            try {
                GameGUI gui = new GameGUI();
                gui.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                gui.setTitle("FTP Client");
                gui.setPreferredSize(new Dimension(1800, 1000));
                gui.pack();
                gui.setVisible(true);
                gui.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {

                        int dialogResult = JOptionPane.showConfirmDialog(gui,
                                "Closing window while client is running" +
                                        " will cause you to lose all data. Proceed in closing?", "Close Window?",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            System.exit(0);
                        }
                    }
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Class constructor initializes instance variables
         */
        public GameGUI() {

            isRunning = false;
            firstTimeStartPressed = true;

            setLayout(new GridBagLayout());
            GridBagConstraints position = new GridBagConstraints();
            Font font = new Font("SansSerif Bold", Font.BOLD, 14);

            //Adding all panels to JFrame
            input = new JPanel(new GridBagLayout());
            input.setBorder(new EmptyBorder(30, 0, 30, 120));
            position = makeConstraints(10, 0, 1, 1, GridBagConstraints.LINE_END);
            add(input,position);

            searchArea = new JPanel(new GridBagLayout());
            searchArea.setBorder(new EmptyBorder(30, 200, 0, 120));
            position = makeConstraints(10, 5, 1, 1, GridBagConstraints.LINE_END);
            add(searchArea,position);

            commandArea = new JPanel(new GridBagLayout());
            commandArea.setBorder(new EmptyBorder(30, 200, 0, 120));
            position = makeConstraints(10, 5, 1, 1, GridBagConstraints.LINE_END);
            add(commandArea,position);

            connectionInfo = new FtpClientAndServer();
            connectionInfo.setMinimumSize(connectionInfo.getPreferredSize());
            position = makeConstraints(0, 0, 10, 10, GridBagConstraints.FIRST_LINE_START);
            searchArea.add(connectionInfo, position);

            cmdLine = new FtpClientAndServer();
            cmdLine.setMinimumSize(cmdLine.getPreferredSize());
            position = makeConstraints(0, 0, 10, 10, GridBagConstraints.FIRST_LINE_START);
            commandArea.add(cmdLine, position);

            //Adding input text fields and labels
            inputLabel = new JLabel("Input Information");
            inputLabel.setBorder(new EmptyBorder(10, 0, 30, 0));
            inputLabel.setFont(font);
            position = makeConstraints(2, 1, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(0, 120, 0, 20);
            input.add(inputLabel, position);

            font = new Font("SansSerif Bold", Font.BOLD, 13);

            String[] speedOptions = new String[] {"Low", "Medium",
                    "High", "Rush Hour"};


            speedSelection = new JComboBox<>(speedOptions);
            speedSelection.setMinimumSize(speedSelection.getPreferredSize());
            position = makeConstraints(3, 3, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(0, -20, 0, 20);
            input.add(speedSelection, position);

            //Adding stats to searchArea JPanel
            serverHostnameLabel = new JLabel("Server Hostname:");
            font = new Font("SansSerif Bold", Font.BOLD, 14);
            serverHostnameLabel.setFont(font);
            position = makeConstraints(1, 0, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(0, -185, 0, 0);
            serverHostnameLabel.setBorder(new EmptyBorder(10, 0, 30, 0));
            input.add(serverHostnameLabel, position);

            font = new Font("SansSerif Bold", Font.BOLD, 13);

            portNumLabel = new JLabel("Port:");
            portNumLabel.setFont(font);
            position = makeConstraints(0, 1, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(0, -110, 0, 20);
            input.add(portNumLabel, position);

            userNameLabel = new JLabel("Username:");
            userNameLabel.setFont(font);
            position = makeConstraints(2, 1, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(10, 0, 0, 20);
            input.add(userNameLabel, position);

            hostNameLabel = new JLabel("Hostname:");
            hostNameLabel.setFont(font);
            position = makeConstraints(0, 2, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(0, -110, 0, 20);
            input.add(hostNameLabel, position);

            speedLabel = new JLabel("Speed:");
            speedLabel.setFont(font);
            position = makeConstraints(2, 2, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(10, 0, 0, 20);
            input.add(speedLabel, position);

            keywordLabel = new JLabel("Keyword:");
            keywordLabel.setFont(font);
            position = makeConstraints(0, 3, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(10, -110, 0, 20);
            searchArea.add(keywordLabel, position);

            commandLabel = new JLabel("TBD");
            commandLabel.setFont(font);
            position = makeConstraints(2, 3, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(10, 0, 0, 20);
            commandArea.add(commandLabel, position);


            //Place the textfields
            serverHostName = new JTextField();
            serverHostName.setForeground(Color.GREEN);
            position = makeConstraints(3, 7, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(40, -170, 0, 20);
            input.add(serverHostName, position);

            portNum = new JTextField();
            portNum.setForeground(Color.GREEN);
            position = makeConstraints(3, 7, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(40, -170, 0, 20);
            input.add(portNum, position);

            userName = new JTextField();
            userName.setForeground(Color.GREEN);
            position = makeConstraints(3, 7, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(40, -170, 0, 20);
            input.add(userName, position);

            hostName = new JTextField();
            hostName.setForeground(Color.GREEN);
            position = makeConstraints(3, 7, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(40, -170, 0, 20);
            input.add(hostName, position);

            keyword = new JTextField();
            keyword.setForeground(Color.GREEN);
            position = makeConstraints(3, 7, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(40, -170, 0, 20);
            searchArea.add(keyword, position);

            command = new JTextField();
            command.setForeground(Color.GREEN);
            position = makeConstraints(3, 7, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(40, -170, 0, 20);
            commandArea.add(command, position);

            //place each button
            connect = new JButton( "Connect" );
            connect.setForeground(Color.GREEN);
            position = makeConstraints(3, 7, 1, 1, GridBagConstraints.LINE_START);
            position.insets =  new Insets(40, -170, 0, 20);
            input.add(connect, position);

            search = new JButton( "Search" );
            search.setForeground(Color.RED);
            position = makeConstraints(4,8,1,1,GridBagConstraints.LINE_START);
            position.insets =  new Insets(-26,-120,0,20);
            searchArea.add(search, position);

            go = new JButton( "Go" );
            go.setForeground(Color.RED);
            position = makeConstraints(4,8,1,1,GridBagConstraints.LINE_START);
            position.insets =  new Insets(-26,-120,0,20);
            input.add(go, position);

            //create and add menu items
            menu = new JMenuBar();
            file = new JMenu("File");
            quit = new JMenuItem("Quit");
            reset = new JMenuItem("Clear");
            menu.add(file);
            file.add(quit);
            file.add(reset);
            setJMenuBar(menu);

            //add all action listeners
            speedSelection.addActionListener(this);
            connect.addActionListener(this);
            search.addActionListener(this);
            go.addActionListener(this);
            serverHostName.addActionListener(this);
            portNum.addActionListener(this);
            userName.addActionListener(this);
            hostName.addActionListener(this);
            keyword.addActionListener(this);
            command.addActionListener(this);
            file.addActionListener(this);
            quit.addActionListener(this);
            reset.addActionListener(this);
        }

        /**
         * Action performed method
         * @param e
         */
        public void actionPerformed(ActionEvent e) {


            //exit application if QUIT menu item
            if (e.getSource() == quit) {
                System.exit(1);
            }

            //set running variable to true if START button
            if (e.getSource() == go) {
                connectionInfo.setCommand(command.getText());
            }


            //set running variable to false if STOP button
            if (e.getSource() == connect) {
                connectionInfo.connectCentralServerStartLocalUser(userName.getText(), serverHostName.getText(),
                        portNum.getText(), speedSelection.getSelectedItem().toString(), hostName.getText());
            }

            if (e.getSource() == search) {
                connectionInfo.search(keyword.getText());
                connectionInfo.repaint();
            }

            //reset simulation if RESET menu item
            if (e.getSource() == reset) {
                //out2.setText("TBD");
                //out3.setText("TBD");
                //out4.setText("TBD");
            }

            //update GUI
            //object.repaint();
        }
        /**
         * Method to update stats in the GUI
         */
        public void updateGUI() {

            //Will actively update
            //out2.setText("TBD");
            //out3.setText("TBD");
            //out4.setText("TBD");
        }

        /**
         * Run method called by the thread
         */
        public void run() {
            try {

                //TODO do we even need this?

                while(loop) {

                    //update simulation if it is running
                    if (isRunning) {


                    }
                    // pause between steps so it isn't too fast
                    Thread.sleep(DELAY);
                }
            }
            catch (InterruptedException ex) {
            }
        }

        /**
         * Method to set contraints for gridbag layout
         * @param x
         * @param y
         * @param h
         * @param w
         * @param align
         * @return
         */
        private GridBagConstraints makeConstraints(int x, int y, int h, int w, int align) {
            GridBagConstraints rtn = new GridBagConstraints();
            rtn.gridx = x;
            rtn.gridy = y;
            rtn.gridheight = h;
            rtn.gridwidth = w;

            rtn.anchor = align;
            return rtn;
        }
    }
