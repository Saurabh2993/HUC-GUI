/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_package;

import com.csvreader.CsvWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author saura
 */
public class CSV_File_Writer {

    static private CsvWriter csvOutput;
    static private String filename;

    public CSV_File_Writer() throws IOException {

        String timestamp_str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(System.currentTimeMillis()));
        filename = "log_" + timestamp_str + ".csv";
        //System.out.println(filename);
        
        csvOutput = new CsvWriter(new FileWriter(filename, true), ',');
        
        csvOutput.write("Time");
        csvOutput.write("Health");
        csvOutput.write("Position");
        csvOutput.write("Velocity");
        csvOutput.write("Magnetic Brake");
        csvOutput.write("Friction Brake");
        csvOutput.write("Ard 3 Mag Dist 1");
        csvOutput.write("Ard 3 Mag Dist 2");
        csvOutput.write("Ard 3 Mag Dist 3");
        csvOutput.write("Ard 3 Mag Dist 4");
        csvOutput.write("Ard 4 Mag Dist 1");
        csvOutput.write("Ard 4 Mag Dist 2");
        csvOutput.write("Ard 4 Mag Dist 3");
        csvOutput.write("Ard 4 Mag Dist 4");
        csvOutput.write("Acc X");
        csvOutput.write("Acc Y");
        csvOutput.write("Acc Z");
        csvOutput.write("Acc Net");
        csvOutput.write("Gyro X");
        csvOutput.write("Gyro Y");
        csvOutput.write("Gyro Z");
        csvOutput.write("Orientation Dist 1");
        csvOutput.write("Orientation Dist 2");
        csvOutput.write("Orientation Dist 3");
        csvOutput.write("Orientation Dist 4");
        csvOutput.write("Orientation Dist 5");
        csvOutput.write("Orientation Dist 6");
        csvOutput.write("Orientation Dist 7");
        csvOutput.write("Orientation Dist 8");
        csvOutput.write("Battery Temp 1");
        csvOutput.write("Battery Temp 2");
        csvOutput.write("Battery Temp 3");
        csvOutput.write("Battery Temp 4");
        csvOutput.write("Battery Temp 5");
        csvOutput.write("Battery Temp 6");
        csvOutput.write("Battery Temp 7");
        csvOutput.write("Battery Temp 8");
        csvOutput.write("Battery Voltage 1");
        csvOutput.write("Battery Voltage 2");
        csvOutput.write("Battery Voltage 3");
        csvOutput.write("Battery Voltage 4");
        csvOutput.write("Battery Voltage 5");
        csvOutput.write("Battery Voltage 6");
        csvOutput.write("Battery Voltage 7");
        csvOutput.write("Battery Voltage 8");
        csvOutput.endRecord();

    }

    void write(String[] buffer_data) throws IOException {
        for (int i = 0; i < buffer_data.length; i++)
            csvOutput.write(buffer_data[i]);
        
        csvOutput.endRecord();
    }

    void close() {
        csvOutput.close();
    }
}
