package test_package;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.FontFormatException;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.GridLayout;
//import java.awt.Image;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.net.SocketException;
//import java.sql.Timestamp;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.concurrent.TimeUnit;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.imageio.ImageIO;
//import javax.swing.BorderFactory;
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JProgressBar;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import javax.swing.SwingConstants;
//import javax.swing.border.Border;
//import javax.swing.border.EmptyBorder;
////import org.apache.commons.lang3.time.StopWatch;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.block.BlockBorder;
//import org.jfree.chart.plot.XYPlot;
//import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;
//
//public class HUC_GroundStation_GUI {
//
//    private static HUC_UDP_Connection udp_conn = null;
//    private static CSV_File_Writer data_writer = null;
//    private static boolean exit_thread = false;
//    private static Thread loop_thread;
////    private static StopWatch watch;
//    private static double maxVelocity = 50;
//    private static double minVelocity = 45;
//    private static double maxTemperature = 50;
//    private static double minTemperature = 45;
//    private static double maxVoltage = 16.8;
//    private static double thresholdVoltage = 12;
//    private static double maxAcceleration = 1000;
//    private static double minAcceleration = 0;
//    private static JProgressBar podProgressBar;
//    static JTextArea system_log;
//    private static int counter = 0;
//    private static Font Roboto_Condensed;
//    private static Font Roboto_Condensed_Light;
//    private static Font Open_Sans_Condensed;
//    private static Font Lato;
//    private static int stripeCount = 0;
//    private static boolean stopClicked = false;
//    static JPanel connection_status_outer_panel;
//    static JLabel indicator_label;
//    static BufferedImage connection_status_connected;
//    static BufferedImage connection_status_disconnected;
//    static BufferedImage red_indicator;
//    static BufferedImage green_indicator;
//    static BufferedImage yellow_indicator;
//    static BufferedImage blue_indicator;
//    static JLabel arduino_health_status[];
//    static JLabel rpi_health_status;
//    static JButton motor_control_forward;
//    static JButton motor_control_backward;
//    static JButton motor_control_stop;
//    static String timestamp_str;
//
//    static void updateIndicators(String binary_health) {
//
//        String individual_health_nodes[] = binary_health.split("");
//        for (int i = 0; i < 5; i++) {
//
//            if (Integer.parseInt(individual_health_nodes[7 - i]) == 1) {
//                arduino_health_status[i].setIcon(new ImageIcon(green_indicator));
//            } else if (Integer.parseInt(individual_health_nodes[7 - i]) == 0) {
//                arduino_health_status[i].setIcon(new ImageIcon(red_indicator));
//            } else {
//                continue;
//            }
//        }
//        System.out.println();
//    }
//
//    static void connectionLost() throws InterruptedException {
//
//        //Stop the thread
//        exit_thread = true;
//
//        //system_log.append("Disconnected...");
//        indicator_label.setIcon(new ImageIcon(connection_status_disconnected));
//        timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//        system_log.append(timestamp_str + " : Network with Raspberry Pi disconnected");
//
//        for (int i = 0; i < 5; i++) {
//
//            arduino_health_status[i].setIcon(new ImageIcon(red_indicator));
//        }
//        rpi_health_status.setIcon(new ImageIcon(red_indicator));
//        //loop_thread.stop();
//    }
//
//    public static JFreeChart createChart(String title, String domain, String range, XYSeriesCollection[] dataset, Color background) {
//        // Create and customize the chart
//        JFreeChart chart = ChartFactory.createXYLineChart(title, domain, range, dataset[0]);
//        chart.getTitle().setFont(Open_Sans_Condensed.deriveFont(Font.PLAIN, 18));
//        chart.getLegend().setItemFont(Open_Sans_Condensed.deriveFont(Font.PLAIN, 12));
//        chart.getLegend().setFrame(BlockBorder.NONE);
//        chart.setBackgroundPaint(background);
//        chart.setBorderVisible(false);
//
//        // Create and customize the plot
//        XYPlot plot = chart.getXYPlot();
//        plot.setDomainGridlinesVisible(false);
//        plot.setRangeGridlinesVisible(false);
//        plot.setBackgroundPaint(Color.BLACK);
//        plot.getDomainAxis().setLabelFont(Open_Sans_Condensed.deriveFont(Font.PLAIN, 10));
//        plot.getRangeAxis().setLabelFont(Open_Sans_Condensed.deriveFont(Font.PLAIN, 10));
//
//        for (int datasetCounter = 1; datasetCounter < dataset.length; datasetCounter++) {
//            plot.setDataset(datasetCounter, dataset[1]);
//            plot.setRenderer(datasetCounter, new StandardXYItemRenderer());
//        }
//        return chart;
//    }
//
//    public static JLabel createNameLabel(String name) {
//        JLabel labelName = new JLabel(name, SwingConstants.CENTER);
//        labelName.setFont(Open_Sans_Condensed.deriveFont(Font.BOLD, 20));
//        return labelName;
//    }
//
//    public static JTextField createValueTextField() {
//        JTextField valueTextField = new CSSTextField(0);
//        valueTextField.setEditable(false);
//        valueTextField.setBackground(new Color(50, 205, 50));
//        valueTextField.setText(" -- ");
//        valueTextField.setHorizontalAlignment(JTextField.CENTER);
//        valueTextField.setFont(Open_Sans_Condensed.deriveFont(Font.PLAIN, 20));
//        valueTextField.setMaximumSize(new Dimension(25, 25));
//        return valueTextField;
//    }
//
//    public static void checkAndUpdate(Double value, JTextField textField, Double maxValue) {
//        textField.setText(String.format("%.2f", value));
//
//        if (value > maxValue) {
//            textField.setBackground(Color.RED);
////        } else if (value > minValue && value <= maxValue) {
////            textField.setBackground(Color.YELLOW);
//        } else {
//            textField.setBackground(new Color(50, 205, 50));
//        }
//    }
//
//    public static void voltageUpdate(Double value, JTextField textField, Double threshold) {
//        textField.setText(String.format("%.2f", value));
//
//        if (value < threshold) {
//            textField.setBackground(Color.RED);
////        } else if (value > minValue && value <= maxValue) {
////            textField.setBackground(Color.YELLOW);
//        } else {
//            textField.setBackground(new Color(50, 205, 50));
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//
//        //Create the window to display graphs
//        BufferedImage uc_image = ImageIO.read(new File("HUC_Images\\Hyperloop_Icon.png"));
//        BufferedImage huc_logo = ImageIO.read(new File("HUC_Images\\huc_big_logo.png"));
//        final JFrame graphs_window = new JFrame();
//        graphs_window.setIconImage(uc_image);
//        graphs_window.setBackground(Color.white);
//        graphs_window.setTitle("HUC GCS - Telemetry Stream");
//
//        graphs_window.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        graphs_window.setLayout(new BorderLayout());
//        graphs_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        try {
//            Roboto_Condensed = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("lib\\Roboto_Condensed\\RobotoCondensed-Regular.ttf")));
//            Roboto_Condensed_Light = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("lib\\Roboto_Condensed\\RobotoCondensed-Light.ttf")));
//            Lato = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("lib\\Lato\\Lato-Regular.ttf")));
//            Open_Sans_Condensed = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("lib\\Open_Sans_Condensed\\OpenSans-CondLight.ttf")));
//        } catch (FontFormatException e2) {
//            e2.printStackTrace();
//        }
//
//        connection_status_connected = ImageIO.read(new File("HUC_Images\\Connected_Image.png"));
//        connection_status_disconnected = ImageIO.read(new File("HUC_Images\\Disconnected_Image.png"));
//
//        indicator_label = new JLabel(new ImageIcon(connection_status_disconnected));
//        indicator_label.setPreferredSize(new Dimension(100, 100));
//
//        //Button padding and dimensions
//        Border button_padding = BorderFactory.createEmptyBorder(10, 10, 20, 10);
//
//        //Create button to reset the graphs
//        final JButton reset_graphs_button = new JButton("Reset all GCS values");
//        reset_graphs_button.setUI(new CSSButton(new Color(0x00000), new Color(0x00000)));
//
//        final JButton help_button = new JButton("?");
//        help_button.setUI(new CSSButton(Color.white, Color.white, Color.BLACK));
//        help_button.setBorderPainted(true);
//
//        //Create a panel to hold the progress bar, reset button and indicator
//        JPanel graphs_window_4th_quadrant = new JPanel(new BorderLayout());
//        graphs_window_4th_quadrant.setBackground(Color.white);
//
//        //Progress bar Panel
//        JPanel progress_bar_panel = new JPanel();
//        progress_bar_panel.setLayout(new GridLayout(1, 0));
//        progress_bar_panel.setBackground(Color.white);
//
//        podProgressBar = new JProgressBar(0, 42);
//        podProgressBar.setStringPainted(true);
//        podProgressBar.setBackground(Color.WHITE);
//        podProgressBar.setForeground(new Color(0xe00102));
//        podProgressBar.setString("Strips Passed: 0" + "    Percent Complete: " + "0%");
//        podProgressBar.setFont(Open_Sans_Condensed.deriveFont(Font.PLAIN, 30));
//        Border pod_progress_bar_padding = BorderFactory.createEmptyBorder(40, 28, 40, 12);
//        progress_bar_panel.add(podProgressBar, BorderLayout.LINE_END);
//        progress_bar_panel.setBorder(pod_progress_bar_padding);
//        graphs_window_4th_quadrant.add(progress_bar_panel, BorderLayout.NORTH);
//        JPanel reset_button_outer_panel = new JPanel();
//        reset_button_outer_panel.add(reset_graphs_button, BorderLayout.WEST);
//        reset_button_outer_panel.setBackground(Color.white);
//        reset_button_outer_panel.add(help_button, BorderLayout.EAST);
//        graphs_window_4th_quadrant.add(reset_button_outer_panel, BorderLayout.CENTER);
//
//        connection_status_outer_panel = new JPanel();
//        connection_status_outer_panel.setBackground(Color.white);
//        connection_status_outer_panel.add(indicator_label, BorderLayout.LINE_END);
//        graphs_window_4th_quadrant.add(connection_status_outer_panel, BorderLayout.SOUTH);
//        graphs_window_4th_quadrant.setBorder(button_padding);
//
//        //Create a line graph to display Position x Velocity series
//        final XYSeries[] Position_Velocity_Series = new XYSeries[3];
//        Position_Velocity_Series[0] = new XYSeries("Ideal Maximum Readings");
//        Position_Velocity_Series[1] = new XYSeries("Actual Readings");
//        Position_Velocity_Series[2] = new XYSeries("Ideal Minimum Readings");
//        XYSeriesCollection[] Position_Velocity_Dataset = new XYSeriesCollection[3];
//        Position_Velocity_Dataset[0] = new XYSeriesCollection(Position_Velocity_Series[0]);
//        Position_Velocity_Dataset[1] = new XYSeriesCollection(Position_Velocity_Series[1]);
//        Position_Velocity_Dataset[2] = new XYSeriesCollection(Position_Velocity_Series[2]);
//
//        //Create a line graph to display Acceleration series
//        final XYSeries[] Acceleration_Series = new XYSeries[4];
//        XYSeriesCollection[] Acceleration_Dataset = new XYSeriesCollection[4];
//        Acceleration_Series[0] = new XYSeries("Ax");
//        Acceleration_Dataset[0] = new XYSeriesCollection(Acceleration_Series[0]);
//        Acceleration_Series[1] = new XYSeries("Ay");
//        Acceleration_Dataset[1] = new XYSeriesCollection(Acceleration_Series[1]);
//        Acceleration_Series[2] = new XYSeries("Az");
//        Acceleration_Dataset[2] = new XYSeriesCollection(Acceleration_Series[2]);
//        Acceleration_Series[3] = new XYSeries("Anet");
//        Acceleration_Dataset[3] = new XYSeriesCollection(Acceleration_Series[3]);
//
//        //Create a line graph to display Temperature series
//        final XYSeries Position_Series = new XYSeries("Actual Readings");
//        XYSeriesCollection[] Position_Dataset = new XYSeriesCollection[1];
//        Position_Dataset[0] = new XYSeriesCollection(Position_Series);
//
//        //Create charts to display each of the above series
//        JFreeChart Position_velocity_chart = createChart("Velocity(m/s) vs Distance(m)", "", "2000", Position_Velocity_Dataset, graphs_window.getBackground());
//        JFreeChart Acceleration_chart = createChart("Acceleration(cm/s2) vs Time(s)", "", "", Acceleration_Dataset, graphs_window.getBackground());
//        JFreeChart Position_chart = createChart("Distance(m) vs Time(s)", "", "", Position_Dataset, graphs_window.getBackground());
//
//        Position_velocity_chart.setBackgroundPaint(graphs_window.getBackground());
//        Acceleration_chart.setBackgroundPaint(graphs_window.getBackground());
//        Position_chart.setBackgroundPaint(graphs_window.getBackground());
//
//        XYPlot Position_velocity_plot = Position_velocity_chart.getXYPlot();
//        Position_velocity_plot.setBackgroundPaint(Color.BLACK);
//
//        XYPlot Acceleration_plot = Acceleration_chart.getXYPlot();
//        Acceleration_plot.setBackgroundPaint(Color.BLACK);
//        Acceleration_plot.setDataset(0, Acceleration_Dataset[0]);
//        Acceleration_plot.setRenderer(0, new StandardXYItemRenderer());
//        Acceleration_plot.setDataset(1, Acceleration_Dataset[1]);
//        Acceleration_plot.setRenderer(1, new StandardXYItemRenderer());
//        Acceleration_plot.setDataset(2, Acceleration_Dataset[2]);
//        Acceleration_plot.setRenderer(2, new StandardXYItemRenderer());
//        Acceleration_plot.setDataset(3, Acceleration_Dataset[3]);
//        Acceleration_plot.setRenderer(3, new StandardXYItemRenderer());
//
//        XYPlot Position_Plot = Position_chart.getXYPlot();
//        Position_Plot.setBackgroundPaint(Color.BLACK);
//
//        //Charts Panel padding
//        Border chart_padding = BorderFactory.createEmptyBorder(10, 20, 0, 20);
//
//        //Create a panel to hold the graphs
//        GridLayout graph_layout = new GridLayout(2, 2);
//        //graph_layout.setVgap(16); //to set the minimum gap between two graphs
//        JPanel graphs_window_graphs_panel = new JPanel(graph_layout);
//        graphs_window_graphs_panel.setBackground(Color.WHITE);
//        graphs_window_graphs_panel.setBorder(chart_padding);
//        graphs_window_graphs_panel.add(new ChartPanel(Position_velocity_chart), 0);
//        graphs_window_graphs_panel.add(new ChartPanel(Acceleration_chart), 1);
//        graphs_window_graphs_panel.add(new ChartPanel(Position_chart), 2);
//        graphs_window_graphs_panel.add(graphs_window_4th_quadrant, 3);
//        graphs_window.add(graphs_window_graphs_panel, BorderLayout.CENTER);
//
//        //Create a panel to hold logos and header
//        JPanel graphs_window_logo_panel = new JPanel(new BorderLayout());
//        graphs_window_logo_panel.setBackground(Color.white);
//
//        JLabel text_label = new JLabel("GROUND CONTROL STATION");
//        text_label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
//        text_label.setForeground(new Color(0xe00122));
//        text_label.setFont(Open_Sans_Condensed.deriveFont(Font.PLAIN, 30));
//
//        JLabel uc_label = new JLabel(new ImageIcon(uc_image));
//        uc_label.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
//        uc_label.setHorizontalAlignment(JLabel.CENTER);
//
//        graphs_window_logo_panel.add(text_label, BorderLayout.WEST);
//        graphs_window_logo_panel.add(uc_label, BorderLayout.EAST);
//
//        graphs_window.add(graphs_window_logo_panel, BorderLayout.NORTH);
//        graphs_window.pack();
//        graphs_window.setVisible(true);
//
//        //Create the window to show log and values
//        final JFrame system_window = new JFrame();
//        system_window.setTitle("System Window");
//        system_window.setIconImage(uc_image);
//        system_window.setExtendedState(system_window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
//        system_window.setLayout(new BorderLayout());
//        system_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // Panel for control and functional buttons
//        JPanel buttons_panel = new JPanel(new BorderLayout());
//        buttons_panel.setBackground(Color.white);
//
//        JPanel log_panel = new JPanel();
//        log_panel.setBackground(Color.white);
//
//        //Panel for control and functional buttons
//        JPanel control_buttons_panel = new JPanel(new GridLayout(5, 1, 55, 55));
//        control_buttons_panel.setBorder(BorderFactory.createEmptyBorder(32, 0, 0, 0));
//        control_buttons_panel.setBackground(Color.white);
//
//        //Control buttons
//        final JButton start_button = new JButton("Start Communication");
//        final JButton stop_pod_button = new JButton("Pod Stop");
//        final JButton retract_button = new JButton("Retract");
//        final JButton start_timer_button = new JButton("Start Timer");
//        final JButton stop_button = new JButton("<html><u>Exit Program</u></html>");
//        motor_control_forward = new JButton();
//        motor_control_backward = new JButton();
//        motor_control_stop = new JButton();
//
////        motor_control_forward.setEnabled(false);
////        motor_control_backward.setEnabled(false);
////        motor_control_stop.setEnabled(false);
//
//        start_button.setUI(new CSSButton(new Color(0x1E824C), Color.GREEN.darker().darker()));
//        stop_pod_button.setUI(new CSSButton(new Color(0xcf000f), new Color(0xF22613)));
//        retract_button.setUI(new CSSButton(new Color(0x3A539B), new Color(0x7f0114)));
//        start_timer_button.setUI(new CSSButton(new Color(0x3A539B), new Color(0x7f0114)));
//        stop_button.setUI(new CSSButton(Color.WHITE, Color.WHITE, Color.BLACK));
//
//        stop_button.setFont(stop_button.getFont().deriveFont(Font.BOLD | Font.ITALIC));
//
//        BufferedImage temp_buffer = ImageIO.read(new File("HUC_Images\\Circled_Chevron_Left_100_1.png"));
//        Image backward_icon = temp_buffer.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
//        temp_buffer = ImageIO.read(new File("HUC_Images\\Circled_Chevron_Right_100_1.png"));
//        Image forward_icon = temp_buffer.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
//        temp_buffer = ImageIO.read(new File("HUC_Images\\Stop_Filled_100.png"));
//        Image stop_icon = temp_buffer.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
//
//        motor_control_forward.setIcon(new ImageIcon(forward_icon));
//        motor_control_backward.setIcon(new ImageIcon(backward_icon));
//        motor_control_stop.setIcon(new ImageIcon(stop_icon));
//
//        motor_control_forward.setBorder(new EmptyBorder(0, 0, 0, 0));
//        motor_control_forward.setBackground(Color.white);
//
//        motor_control_stop.setBorder(new EmptyBorder(0, 0, 0, 0));
//        motor_control_stop.setBackground(Color.white);
//
//        motor_control_backward.setBorder(new EmptyBorder(0, 0, 0, 0));
//        motor_control_backward.setBackground(Color.white);
//
//        /*
//         * Change this to the preferred screen size *
//         */
//        motor_control_forward.setPreferredSize(new Dimension(60, 60));
//        motor_control_backward.setPreferredSize(new Dimension(40, 40));
//        motor_control_stop.setPreferredSize(new Dimension(60, 60));
//
//        JLabel sps_label = createNameLabel("SPS Control Buttons");
//        sps_label.setBackground(Color.white);
//
//        JPanel motor_control_buttons = new JPanel(new GridLayout(1, 2, 10, 10));
//        //motor_control_buttons.add(motor_control_backward);
//        motor_control_buttons.add(motor_control_stop);
//        motor_control_buttons.add(motor_control_forward);
//        
//        motor_control_forward.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
//        motor_control_forward.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
//        
//        motor_control_buttons.setBackground(Color.white);
//
//        control_buttons_panel.add(start_button, 0);
//        control_buttons_panel.add(stop_pod_button, 1);
//        control_buttons_panel.add(retract_button, 2);
//        control_buttons_panel.add(sps_label, 3);
//        control_buttons_panel.add(motor_control_buttons, 4);
//
//        buttons_panel.add(control_buttons_panel, BorderLayout.NORTH);
//        buttons_panel.add(stop_button, BorderLayout.SOUTH);
//
//        //Panel to display stats and healths obtained from rpi
//        JPanel stats_panel = new JPanel(new GridLayout(3, 1));
//        stats_panel.setBackground(Color.white);
//
//        //Panel to display arduino, rpi and brake status
//        JPanel indicator_panel = new JPanel(new GridBagLayout());
//        indicator_panel.setBackground(Color.white);
//        indicator_panel.setBorder(BorderFactory.createEmptyBorder(4, 20, 0, 20));
//        GridBagConstraints c = new GridBagConstraints();
//        c.weighty = 1;
//
//        red_indicator = ImageIO.read(new File("HUC_Images\\Red_Indicator.png"));
//        green_indicator = ImageIO.read(new File("HUC_Images\\Green_Indicator.png"));
//        yellow_indicator = ImageIO.read(new File("HUC_Images\\Yellow_Indicator.png"));
//        blue_indicator = ImageIO.read(new File("HUC_Images\\Blue_Indicator.png"));
//
//        arduino_health_status = new JLabel[5];
//        JLabel arduino_label[] = new JLabel[5];
//        int arduino_x = 0;
//
//        for (int i = 0; i < 5; i++) {
//
//            arduino_health_status[i] = new JLabel(new ImageIcon(red_indicator));
//            if (i == 3 || i == 2) {
//                continue;
//            }
//            c.gridx = arduino_x;
//            c.gridy = 0;
//            c.weightx = 1;
//            c.weighty = 1;
//            indicator_panel.add(arduino_health_status[i], c);
//
//            arduino_label[i] = createNameLabel("Arduino " + (i + 1));
//            c.gridx = arduino_x;
//            c.gridy = 1;
//            c.weightx = 1;
//            indicator_panel.add(arduino_label[i], c);
//
//            arduino_x++;
//        }
//
//        rpi_health_status = new JLabel(new ImageIcon(red_indicator));
//        c.gridx = 0;
//        c.gridy = 2;
//        c.weightx = 1;
//        indicator_panel.add(rpi_health_status, c);
//
//        final JLabel magnetic_brakes_indicator = new JLabel(new ImageIcon(blue_indicator));
//        c.gridx = 1;
//        c.gridy = 2;
//        c.weightx = 1;
//        indicator_panel.add(magnetic_brakes_indicator, c);
//
//        JLabel friction_brakes_indicator = new JLabel(new ImageIcon(blue_indicator));
//        c.gridx = 2;
//        c.gridy = 2;
//        c.weightx = 1;
//        indicator_panel.add(friction_brakes_indicator, c);
//
//        JLabel rpi_label = createNameLabel("RPi");
//        c.gridx = 0;
//        c.gridy = 3;
//        c.weightx = 1;
//        indicator_panel.add(rpi_label, c);
//
//        JLabel magnetic_brakes_label = createNameLabel("Pusher Indicator");
//        c.gridx = 1;
//        c.gridy = 3;
//        c.weightx = 1;
//        indicator_panel.add(magnetic_brakes_label, c);
//
//        final JLabel friction_brakes_label = createNameLabel("Friction Brakes");
//        c.gridx = 2;
//        c.gridy = 3;
//        c.weightx = 1;
//        indicator_panel.add(friction_brakes_label, c);
//
//        //Panel to display Velocity, Net Acceleration, Lift
//        JPanel movement_stats_panel = new JPanel(new GridBagLayout());
//        movement_stats_panel.setBackground(Color.white);
//        movement_stats_panel.setBorder(BorderFactory.createEmptyBorder(32, 0, 0, 0));
//        GridBagConstraints d = new GridBagConstraints();
//        d.weighty = 1;
//
//        final JTextField value_status = createValueTextField();
//        value_status.setColumns(16);
//        d.gridx = 0;
//        d.gridy = 0;
//        d.weightx = 1;
//        movement_stats_panel.add(value_status, d);
//
//        final JTextField value_velocity = createValueTextField();
//        value_velocity.setColumns(16);
//        d.gridx = 1;
//        d.gridy = 0;
//        d.weightx = 1;
//        movement_stats_panel.add(value_velocity, d);
//
//        final JTextField value_acceleration_net = createValueTextField();
//        value_acceleration_net.setColumns(16);
//        d.gridx = 2;
//        d.gridy = 0;
//        d.weightx = 1;
//        movement_stats_panel.add(value_acceleration_net, d);
//
//        final JTextField value_lift_orientation = createValueTextField();
//        value_lift_orientation.setColumns(16);
//        d.gridx = 3;
//        d.gridy = 0;
//        d.weightx = 1;
//        movement_stats_panel.add(value_lift_orientation, d);
//
//        JLabel label_status = createNameLabel("Status");
//        d.gridx = 0;
//        d.gridy = 1;
//        d.weightx = 1;
//        movement_stats_panel.add(label_status, d);
//
//        JLabel label_velocity = createNameLabel("Velocity");
//        d.gridx = 1;
//        d.gridy = 1;
//        d.weightx = 1;
//        movement_stats_panel.add(label_velocity, d);
//
//        JLabel label_acceleration_net = createNameLabel("Net Acceleration");
//        d.gridx = 2;
//        d.gridy = 1;
//        d.weightx = 1;
//        movement_stats_panel.add(label_acceleration_net, d);
//
//        JLabel label_lift_orientation = createNameLabel("Orientation Lift");
//        d.gridx = 3;
//        d.gridy = 1;
//        d.weightx = 1;
//        movement_stats_panel.add(label_lift_orientation, d);
//        
//        
//        JLabel huc_banner = new JLabel(new ImageIcon(huc_logo));
//        d.gridx = 0;
//        d.gridy = 2;
//        d.gridwidth = 4;
//        movement_stats_panel.add(huc_banner, d);
//        
//
//        //Arduino 3 and 4 magnetic distances
//        JLabel arduino_3_magnetic_distance_label = createNameLabel("Actuator Distances:");
//        arduino_3_magnetic_distance_label.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
////        d.gridx = 0;
////        d.gridy = 2;
////        movement_stats_panel.add(arduino_3_magnetic_distance_label, d);
//
//        JLabel[] label_arduino_3_mag_distance = new JLabel[4];
//        final JTextField[] value_arduino_3_mag_distance = new JTextField[4];
//
//        for (int i = 0; i < 4; i++) {
//            label_arduino_3_mag_distance[i] = createNameLabel(" " + (i + 1) + " ");
//            value_arduino_3_mag_distance[i] = createValueTextField();
//            value_arduino_3_mag_distance[i].setColumns(16);
//            d.weightx = 1;
//
//            d.gridx = i;
//            d.gridy = 3;
//            //movement_stats_panel.add(label_arduino_3_mag_distance[i], d);
//
//            d.gridx = i;
//            d.gridy = 4;
//            //movement_stats_panel.add(value_arduino_3_mag_distance[i], d);
//
//        }
//
//        JLabel arduino_4_magnetic_distance_label = createNameLabel("");
//        //arduino_4_magnetic_distance_label.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
//        d.gridx = 0;
//        d.gridy = 5;
//        //movement_stats_panel.add(arduino_4_magnetic_distance_label, d);
//
//        JLabel[] label_arduino_4_mag_distance = new JLabel[4];
//        final JTextField[] value_arduino_4_mag_distance = new JTextField[4];
//
//        for (int i = 0; i < 4; i++) {
//            label_arduino_4_mag_distance[i] = createNameLabel(" " + (i + 5) + " ");
//            value_arduino_4_mag_distance[i] = createValueTextField();
//            value_arduino_4_mag_distance[i].setColumns(16);
//            d.weightx = 1;
//
//            d.gridx = i;
//            d.gridy = 6;
//            //movement_stats_panel.add(label_arduino_4_mag_distance[i], d);
//
//            d.gridx = i;
//            d.gridy = 7;
//            //movement_stats_panel.add(value_arduino_4_mag_distance[i], d);
//
//        }
//
//        //Panel to display Battery temperatures and voltages
//        JPanel battery_stats_panel = new JPanel(new GridBagLayout());
//        battery_stats_panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        battery_stats_panel.setBackground(Color.white);
//        GridBagConstraints e = new GridBagConstraints();
//        e.weighty = 1;
//
//        JLabel label_battery_stats = createNameLabel("<html><b>Battery Stats:</b></html>");
//        //label_battery_stats.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
//        e.gridx = 0;
//        e.gridy = 0;
//        battery_stats_panel.add(label_battery_stats, e);
//
//        JLabel label_numbers = createNameLabel("Number:");
//        e.gridx = 0;
//        e.gridy = 1;
//        battery_stats_panel.add(label_numbers, e);
//
//        JLabel label_battery_temperature = createNameLabel("Temperature:");
//        e.gridx = 0;
//        e.gridy = 2;
//        battery_stats_panel.add(label_battery_temperature, e);
//
//        JLabel label_battery_voltage = createNameLabel("Voltage:");
//        e.gridx = 0;
//        e.gridy = 3;
//        battery_stats_panel.add(label_battery_voltage, e);
//
//        JLabel[] label_battery_num = new JLabel[8];
//        final JTextField[] value_battery_temp = new JTextField[8];
//        final JTextField[] value_battery_voltage = new JTextField[8];
//
//        int battery_x = 0;
//        for (int i = 0; i < 8; i++) {
//
//            label_battery_num[i] = createNameLabel(" " + (i + 1) + " ");
//            value_battery_temp[i] = createValueTextField();
//            value_battery_temp[i].setColumns(8);
//
//            value_battery_voltage[i] = createValueTextField();
//            value_battery_voltage[i].setColumns(8);
//            e.weightx = 1;
//
////            if (i > 2 && i != 7) {
////                continue;
////            }
//            e.gridx = battery_x + 1;
//            e.gridy = 1;
//            
//            battery_stats_panel.add(label_battery_num[battery_x], e);
//
//            e.gridx = battery_x + 1;
//            e.gridy = 2;
//            battery_stats_panel.add(value_battery_temp[i], e);
//
//            e.gridx = battery_x + 1;
//            e.gridy = 3;
//            battery_stats_panel.add(value_battery_voltage[i], e);
//
//            battery_x++;
//
//        }
//
//        stats_panel.add(indicator_panel, 0);
//        stats_panel.add(movement_stats_panel, 1);
//        stats_panel.add(battery_stats_panel, 2);
//
//        system_log = new JTextArea(100, 30);
//        system_log.setText("**** System Status will be displayed here ****" + "\n");
//        system_log.append("**** Press Start Communication to connect to RPi ****" + "\n\n");
//        Font log_font = new Font(text_label.getName(), Font.PLAIN, 16);
//        system_log.setFont(log_font);
//        system_log.setEditable(false);
//        system_log.setBackground(Color.BLACK);
//        system_log.setForeground(Color.WHITE);
//
//        log_panel.add(new JScrollPane(system_log));
//
//        system_window.add(buttons_panel, BorderLayout.LINE_START);
//        system_window.add(stats_panel, BorderLayout.CENTER);
//        system_window.add(log_panel, BorderLayout.LINE_END);
//        system_window.pack();
//        system_window.setVisible(true);
//
//        // configure the start button and use another thread to listen for data
//        start_button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                system_log.append(timestamp_str + " : Start Communication button clicked\n");
//                try {
//                    udp_conn = new HUC_UDP_Connection();
//                    data_writer = new CSV_File_Writer();
//                    start_button.setEnabled(false);
//                } catch (SocketException ex) {
//                    Logger.getLogger(HUC_GroundStation_GUI.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(HUC_GroundStation_GUI.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//                indicator_label.setIcon(new ImageIcon(connection_status_disconnected));
//
//                watch = new StopWatch();
//                watch.start();
//
//                loop_thread = new Thread() {
//
//                    @Override
//                    public void run() {
//
//                        //System.out.println("Thread running");
//                        try {
//                            long time = 0;
//                            boolean firstTime = true;
//                            counter = 1;
//
//                            timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                            system_log.append(timestamp_str + " : Listening to RPi on port 3000\n");
//
//                            int countSecond = 0;
//                            int status = 0;
//                            int old_status = 0;
//                            double position = 0;
//
//                            short ax;
//                            short ay;
//                            short az;
//                            double anet;
//
//                            double average_ax = 0;
//                            double average_ay = 0;
//                            double average_az = 0;
//
//                            double velocity;
//                            double average_velocity = 0;
//
//                            double[] batteryTemp = new double[8];
//                            double[] batteryVoltage = new double[8];
//
//                            double[] average_batteryTemp = new double[8];
//                            double[] average_batteryTemp_old = new double[8];
//                            double[] average_batteryVoltage = new double[8];
//                            double[] average_batteryVoltage_old = new double[8];
//
//                            for (int i = 0; i < 8; i++) {
//
//                                average_batteryTemp[i] = 0;
//                                average_batteryTemp_old[i] = 25;
//                                average_batteryVoltage[i] = 0;
//                                average_batteryVoltage_old[i] = 16.8;
//                            }
//
//                            String binary_health;
//                            byte[] input;
//                            while (!exit_thread) {
//                                countSecond++;
//                                if (firstTime) {
//                                    //system_log.append("Listening on port 3000\n");
//                                    //system_log.append("Listening.... \n");
//                                    input = udp_conn.listen(0);
//                                    timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                                    system_log.append(timestamp_str + " : Connection with RPi established\n");
//
//                                    rpi_health_status.setIcon(new ImageIcon(green_indicator));
//                                    indicator_label.setIcon(new ImageIcon(connection_status_connected));
//                                    time = watch.getTime(TimeUnit.SECONDS);
//                                    //system_log.append("Timer started\n");
//                                    firstTime = false;
//
//                                } else {
//
//                                    input = udp_conn.listen(5000);
//                                    System.out.println("still connected");
//                                    //rpi_health_status.setIcon(new ImageIcon(green_indicator));
//                                    //indicator_label.setIcon(new ImageIcon(connection_status_connected));
//                                }
//
//                                String[] buffer_data = new String[45];
//
//                                timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                                buffer_data[0] = timestamp_str;
//
//                                //System.out.println(input.length);
//                                int msg_type = new Integer(input[0]);
//                                if (msg_type == 3) {
//
//                                    timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                                    system_log.append(timestamp_str + " : Connection with Arduinos down. Connection with RPi is alive." + "\n");
//                                    binary_health = "00000000";
//                                    updateIndicators(binary_health);
//
//                                    for (int i = 0; i < 8; i++) {
//                                        batteryTemp[i] = 0.0;
//                                        checkAndUpdate(batteryTemp[i], value_battery_temp[i], maxTemperature);
//
//                                        batteryVoltage[i] = 0.0;
//                                        voltageUpdate(batteryVoltage[i], value_battery_voltage[i], thresholdVoltage);
//                                    }
//                                    continue;
//
//                                } else if (msg_type == 1) {
//
//                                    old_status = status;
//                                    status = input[1];
//                                    udp_conn.setStatus((byte) status);
//
//                                    if (status == 1) {
//                                        value_status.setText("Pod Idle");
//                                        if (old_status != status) {
//                                            timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                                            system_log.append(timestamp_str + " : Pod is in Idle State." + "\n");
//                                        }
//
//                                    } else if (status == 2) {
//                                        value_status.setText("Pod Ready for Launch");
//                                        if (old_status != status) {
//                                            timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                                            system_log.append(timestamp_str + " : Pod is Ready for Launch." + "\n");
//                                        }
//                                    }
//                                } else {
//
//                                    //system_log.append((String)watch.getTime(TimeUnit.MILLISECONDS));
//                                    int health = new Integer(input[1]);
//                                    binary_health = Integer.toBinaryString(health);
//                                    //System.out.println(health);
//
//                                    //System.out.println(binary_health);
//                                    binary_health = String.format("%08d", Integer.parseInt(binary_health));
//                                    //System.out.println(binary_health);
//
//                                    updateIndicators(binary_health);
//
//                                    buffer_data[1] = Double.toString(health);
////                                if (counter <= 42) {
////                                    counter++;
////                                    podProgressBar.setValue(counter);
////                                    podProgressBar.setString("Stripes Passed: " + counter + "    Percent Complete: " + ((counter * 100) / 42) + "%");
////                                }
//
////
//                                    int strip = input[2];
//                                    //position = new Double(strip) * 0.3048;    //conversion 1 ft = 0.3048 m
//                                    position = new Double(strip) * 100;
//                                    //System.out.println(position);
//                                    podProgressBar.setValue(strip);
//                                    podProgressBar.setString("Stripes Passed: " + input[2] + "    Percent Complete: " + ((input[2] * 100) / 42) + "%");
//
////                                    //position += 1;
//                                    if (strip >= 1 && strip <= 9) {
//                                        old_status = status;
//                                        status = 3;
//                                        value_status.setText("Pusher Phase");
//                                        if (old_status != status) {
//                                            timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                                            system_log.append(timestamp_str + " : Pod is in Pusher Phase." + "\n");
//                                        }
//                                    } else if (strip > 9) {
//                                        old_status = status;
//                                        status = 5;
//                                        value_status.setText("Braking Phase");
//                                        if (old_status != status) {
//                                            timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                                            system_log.append(timestamp_str + " : Pod is in Braking Phase." + "\n");
//                                        }
//                                    }
//
//                                    buffer_data[2] = Integer.toString(strip);
////
//                                    velocity = new Double(((input[3] & 0xFF) | ((input[4]) & 0xFF) << 8) * 100);
//                                    System.out.println("Velocity-------------------" + velocity);
////                                    average_velocity = average_velocity + velocity;
//                                    buffer_data[3] = Double.toString(velocity);
////
//                                    //Brake data
//                                    buffer_data[4] = Double.toString((input[5] & 0xFF));
//                                    buffer_data[5] = Double.toString((input[6] & 0xFF));
//
//                                    if (input[5] == 0) {
//                                        magnetic_brakes_indicator.setIcon(new ImageIcon(blue_indicator));
//                                    } else if (input[5] == 1) {
//                                        magnetic_brakes_indicator.setIcon(new ImageIcon(green_indicator));
//                                    }
//                                    if (input[6] == 2 || input[6] == 1) {
//                                        friction_brakes_indicator.setIcon(new ImageIcon(green_indicator));
//                                    } else if (input[6] == 0 || input[6] == 4) {
//                                        friction_brakes_indicator.setIcon(new ImageIcon(blue_indicator));
//                                    } else if (input[6] == 3) {
//                                        friction_brakes_indicator.setIcon(new ImageIcon(yellow_indicator));
//                                    }
//
//                                    buffer_data[6] = Double.toString((input[7] & 0xFF));
//                                    buffer_data[7] = Double.toString((input[8] & 0xFF));
//                                    buffer_data[8] = Double.toString((input[9] & 0xFF));
//                                    buffer_data[9] = Double.toString((input[10] & 0xFF));
//
//                                    buffer_data[10] = Double.toString((input[11] & 0xFF));
//                                    buffer_data[11] = Double.toString((input[12] & 0xFF));
//                                    buffer_data[12] = Double.toString((input[13] & 0xFF));
//                                    buffer_data[13] = Double.toString((input[14] & 0xFF));
//
//                                    for (int i = 0; i < 4; i++) {
//                                        value_arduino_3_mag_distance[i].setText((Integer.toString(input[i + 7] & 0xFF)));
//                                    }
//
//                                    for (int i = 0; i < 4; i++) {
//                                        value_arduino_4_mag_distance[i].setText((Integer.toString(input[i + 10] & 0xFF)));
//                                    }
//
//                                    ax = (short) (((input[15] & 0xFF) | (input[16] & 0xFF) << 8));
//                                    ay = (short) (((input[17] & 0xFF) | (input[18] & 0xFF) << 8));
//                                    az = (short) (((input[19] & 0xFF) | (input[20] & 0xFF) << 8));
//
//                                    System.out.println(ax);
////                                    System.out.println(ay);
////                                    System.out.println(az);
////   
//                                    average_ax = average_ax + ax;
//                                    average_ay = average_ay + ay;
//                                    average_az = average_az + az;
//
//                                    anet = (short) Math.sqrt(Math.pow(ax, 2));
//
//                                    //acceleration
//                                    buffer_data[14] = Double.toString(ax);
//                                    buffer_data[15] = Double.toString(ay);
//                                    buffer_data[16] = Double.toString(az);
//                                    buffer_data[17] = Double.toString(anet);
////
//                                    //gyro
//                                    buffer_data[18] = Double.toString(((input[21] & 0xFF) << 8 | (input[22]) & 0xFF) * 100);
//                                    buffer_data[19] = Double.toString(((input[23] & 0xFF) << 8 | (input[24]) & 0xFF) * 100);
//                                    buffer_data[20] = Double.toString(((input[25] & 0xFF) << 8 | (input[26]) & 0xFF) * 100);
//
//                                    //orientation distance
//                                    buffer_data[21] = Double.toString(input[27] & 0xFF);
//                                    buffer_data[22] = Double.toString(input[28] & 0xFF);
//                                    buffer_data[23] = Double.toString(input[29] & 0xFF);
//                                    buffer_data[24] = Double.toString(input[30] & 0xFF);
//                                    buffer_data[25] = Double.toString(input[31] & 0xFF);
//                                    buffer_data[26] = Double.toString(input[32] & 0xFF);
//                                    buffer_data[27] = Double.toString(input[33] & 0xFF);
//                                    buffer_data[28] = Double.toString(input[34] & 0xFF);
//
//                                    for (int i = 0; i < 8; i++) {
//                                        batteryTemp[i] = ((input[i * 2 + 35] & 0xFF) | ((input[i * 2 + 36] & 0xFF)) << 8) / 10.0;
//                                        average_batteryTemp[i] = average_batteryTemp[i] + batteryTemp[i];
//                                        buffer_data[29 + i] = Double.toString(batteryTemp[i]);
//                                    }
//
//                                    for (int i = 0; i < 8; i++) {
//                                        batteryVoltage[i] = (input[i + 51] & 0xFF) / 10.0;
//                                        average_batteryVoltage[i] = average_batteryVoltage[i] + batteryVoltage[i];
//                                        buffer_data[37 + i] = Double.toString(batteryVoltage[i]);
//                                    }
//
//                                    if (countSecond % 5 == 0) {
////                                        Position_Velocity_Series[0].add(position, velocity + 2500);
////                                        Position_Velocity_Series[1].add(position, velocity);
////                                        Position_Velocity_Series[2].add(position, velocity - 2500);
//                                        anet = (double) Math.sqrt(Math.pow((average_ax / 5), 2));
//                                        System.out.println(anet);
//                                        Acceleration_Series[0].add(watch.getTime(TimeUnit.SECONDS) - time, average_ax / 5);
//                                        //Acceleration_Series[1].add(watch.getTime(TimeUnit.SECONDS) - time, average_ay / 5);
//                                        //Acceleration_Series[2].add(watch.getTime(TimeUnit.SECONDS) - time, average_az / 5);
//                                        Acceleration_Series[3].add(watch.getTime(TimeUnit.SECONDS) - time, anet);
//                                        checkAndUpdate(anet, value_acceleration_net, maxAcceleration);
////                                        Position_Series.add(watch.getTime(TimeUnit.SECONDS) - time, position);
//
//                                        checkAndUpdate(average_velocity, value_velocity, maxVelocity);
//
//                                        for (int i = 0; i < 8; i++) {
//
//                                            average_batteryTemp[i] = average_batteryTemp[i] / 5;
//
//                                            //System.out.println("OLd avg temp " + (i+1) + "  " + average_batteryTemp_old[i]);
//                                            System.out.println("New avg temp " + (i + 1) + "  " + average_batteryTemp[i]);
//                                            checkAndUpdate((average_batteryTemp[i]), value_battery_temp[i], maxTemperature);
//
//                                            average_batteryVoltage[i] = average_batteryVoltage[i] / 5;
//                                            if (average_batteryVoltage[i] > 16.7) {
//                                                average_batteryVoltage[i] = 16.7;
//                                            }
//
//                                            //System.out.println("OLd avg voltg" + (i+1) + "  " + average_batteryVoltage_old[i]);
//                                            System.out.println("New avg voltg " + (i + 1) + "  " + average_batteryVoltage[i]);
//                                            voltageUpdate(average_batteryVoltage[i], value_battery_voltage[i], thresholdVoltage);
//
////                                            if (average_batteryTemp[i] >= (average_batteryTemp_old[i] * 97 / 100) && average_batteryTemp[i] <= (average_batteryTemp_old[i] * 103 / 100)) {
////                                                checkAndUpdate((average_batteryTemp[i]), value_battery_temp[i], maxTemperature, minTemperature);
////                                                average_batteryTemp_old[i] = average_batteryTemp[i];
////                                            } else if (average_batteryTemp[i] < (average_batteryTemp_old[i] * 97 / 100)) {
////                                                checkAndUpdate((average_batteryTemp_old[i] * 97 / 100), value_battery_temp[i], maxTemperature, minTemperature);
////                                                average_batteryTemp_old[i] = (average_batteryTemp_old[i] * 97 / 100);
////                                            } else {
////                                                checkAndUpdate((average_batteryTemp_old[i] * 103 / 100), value_battery_temp[i], maxTemperature, minTemperature);
////                                                average_batteryTemp_old[i] = (average_batteryTemp_old[i] * 103 / 100);
////                                            }
////                                            if ((average_batteryVoltage[i]) < average_batteryVoltage_old[i]) {
////                                                checkAndUpdate((average_batteryVoltage[i]), value_battery_temp[i], maxVoltage, minVoltage);
////                                                average_batteryVoltage_old[i] = average_batteryVoltage[i];
////                                            } else {
////                                                checkAndUpdate((average_batteryVoltage_old[i]), value_battery_temp[i], maxVoltage, minVoltage);
////                                            }
//                                        }
//
//                                        countSecond = 0;
//                                        average_ax = 0;
//                                        average_ay = 0;
//                                        average_az = 0;
//
//                                        for (int i = 0; i < 8; i++) {
//                                            average_batteryTemp[i] = 0;
//                                            average_batteryVoltage[i] = 0;
//                                        }
//
//                                    }
////
//                                    data_writer.write(buffer_data);
//                                }
//
//                                if (exit_thread == true) {
//                                    break;
//                                }
////                                    }
//                                try {
//                                    Thread.sleep(200);
//                                } catch (InterruptedException e1) {
//                                    e1.printStackTrace();
//                                }
////                                }
//                            }
//                        } catch (Exception ex) {
//                            Logger.getLogger(HUC_GroundStation_GUI.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//
//                };
//                loop_thread.start();
//            }
//        });
//
//        reset_graphs_button.addActionListener(
//                new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset all values?", "Confirm reset", JOptionPane.YES_NO_OPTION);
//
//                if (confirm == 0) {
//
//                    timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                    system_log.append(timestamp_str + " : Reseting all graphs.\n");
//                    Position_Velocity_Series[0].clear();
//                    Position_Velocity_Series[1].clear();
//                    Acceleration_Series[0].clear();
//                    Acceleration_Series[1].clear();
//                    Acceleration_Series[2].clear();
//                    Acceleration_Series[3].clear();
//                    Position_Series.clear();
//
//                    timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                    system_log.append(timestamp_str + " : All graphs reset\n");
//                }
//            }
//        });
//
//        help_button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent arg0) {
//
//                JFrame frame = new JFrame();
//                HelpWindow window = new HelpWindow(frame);
//                window.pack();
//                window.setVisible(true);
//
//            }
//
//        });
//
//        stop_button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                int confirmed = JOptionPane.showConfirmDialog(null,
//                        "Are you sure you want to exit the program?",
//                        "Exit Program Message Box",
//                        JOptionPane.YES_NO_OPTION);
//
//                if (confirmed == JOptionPane.YES_OPTION) {
//                    system_log.append("Closing program.\n");
//                    system_log.append("Good Bye.\n");
//                    if (!start_button.isEnabled()) {
//                        exit_thread = true;
//                        udp_conn.close();
//                        data_writer.close();
//
//                        graphs_window.dispose();
//                        system_window.dispose();
//                    } else {
//                        graphs_window.dispose();
//                        system_window.dispose();
//                    }
//                }
//            }
//        });
//
//        motor_control_forward.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                system_log.append(timestamp_str + " : Motor control forward button pressed.\n");
//
//                byte[] move_forward = {0x06, 0x0D, 0x0A};
//                udp_conn.sendToRpi(move_forward);
//
//            }
//        });
//
//        motor_control_stop.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                system_log.append(timestamp_str + " : Motor control stop button pressed.\n");
//
//                byte[] motor_stop = {0x07, 0x0D, 0x0A};
//                udp_conn.sendToRpi(motor_stop);
//
//            }
//        });
//
//        motor_control_backward.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                system_log.append(timestamp_str + " : Motor control backward button pressed.\n");
//
//                byte[] move_backward = {0x08, 0x0D, 0x0A};
//                udp_conn.sendToRpi(move_backward);
//
//            }
//        });
//
//        WindowListener listener = new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                graphs_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                system_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                int confirmed = JOptionPane.showConfirmDialog(null,
//                        "Are you sure you want to exit the program?",
//                        "Exit Program Message Box",
//                        JOptionPane.YES_NO_OPTION);
//
//                if (confirmed == JOptionPane.YES_OPTION) {
//                    if (!start_button.isEnabled()) {
//                        exit_thread = true;
//                        udp_conn.close();
//                        data_writer.close();
//                    }
//                } else {
//                    graphs_window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//                    system_window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//                }
//            }
//        };
//
//        stop_pod_button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    int confirm = JOptionPane.showConfirmDialog(system_window, "Are you sure you want to stop the pod?", "Confirm Pod Stop", JOptionPane.YES_NO_OPTION);
//
//                    if (confirm == 0) {
//                        timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                        system_log.append(timestamp_str + " : Pod stop command sent.\n");
//                        timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                        system_log.append(timestamp_str + " : Service Propulsion Controls are now enabled.\n");
//                        byte[] stop_pod_command = {0x01, 0x0D, 0x0A};
//                        motor_control_forward.setEnabled(true);
//                        motor_control_backward.setEnabled(true);
//                        motor_control_stop.setEnabled(true);
//
//                        udp_conn.sendToRpi(stop_pod_command);
//
//                    } else {
//                        timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                        system_log.append(timestamp_str + " : Emergency braking command cancelled\n");
//                    }
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
//
//        retract_button.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                try {
//                    int confirm = JOptionPane.showConfirmDialog(system_window, "Are you sure you want to retract the actuators?", "Confirm Retraction", JOptionPane.YES_NO_OPTION);
//
//                    if (confirm == 0) {
//                        timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                        system_log.append(timestamp_str + " : Retraction initialized.\n");
//                        byte[] retraction_command = {0x02, 0x0D, 0x0A};
//                        udp_conn.sendToRpi(retraction_command);
//                        timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                        system_log.append(timestamp_str + " : Retraction command sent" + ".\n");
//                    } else {
//                        timestamp_str = new SimpleDateFormat("HH:mm:ss:SSS").format(new Timestamp(System.currentTimeMillis()));
//                        system_log.append(timestamp_str + " : Retraction cancelled\n");
//                    }
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
//
//        graphs_window.addWindowListener(listener);
//        system_window.addWindowListener(listener);
//
//    }
//}
