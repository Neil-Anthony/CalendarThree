/*
Name: Neil Miller
Date: 11/16/19
Class CS 140B
Assignment: Calendar 3 (TBD)
Purpose: This program will display a calendar.
For Extra Credit I did.
1. Jagged Array
*/


import java.io.*;
import java.util.Calendar;
import java.util.Scanner;

public class NMillerThirdCalendar {

    public static void main(String[] args) throws FileNotFoundException {

        String[][] yearArray = makeYearArray(2019);
        populateDateArray(yearArray);

        Calendar cal = Calendar.getInstance();
        //cal.set(Calendar.YEAR, 2019);

        String cmd;
        int month = cal.get(Calendar.MONTH) + 1;
        int date = 100;
        int year = cal.get(Calendar.YEAR);

        do {
            Scanner console = new Scanner(System.in);

            System.out.println("Please type a command");
            System.out.println("\t\"e\" to enter a date and display the corresponding calendar");
            System.out.println("\t\"t\" to get today's date and display the calendar");
            System.out.println("\t\"n\" to display the next month");
            System.out.println("\t\"p\" to display the previous month");
            System.out.println("\t\"ev\" to enter an event");
            System.out.println("\t\"fp\" to print a month to a file");
            System.out.println("\t\"q\" to quit the program");

            cmd = console.nextLine().toLowerCase();

            switch (cmd){
                case "e":   month = cmdE(month, date, yearArray);
                    break;
                case "t":   month = cmdT(month, date, yearArray);
                    break;
                case "n":   month++;
                    if (month > 12){
                        month = 1;
                        year++;
                        drawMonth(month, date, year, yearArray);
                        break;
                    } else {
                        date = 101;
                        drawMonth(month, date, year, yearArray);
                        break;
                    }
                case "p":   month--;
                    if (month < 1){
                        month = 12;
                        year--;
                        drawMonth(month, date, year, yearArray);
                        break;
                    } else {
                        date = 100;
                        drawMonth(month,date,year, yearArray);
                        break;
                    }
                case "ev":   eventPlan(yearArray);
                    break;
                case "fp":  filePrint(year, yearArray);
                    break;
            }
        } while (!cmd.equalsIgnoreCase("q"));
    }

    public static int cmdE(int month, int date, String[][] yearArray){
        // Allows the user to enter a date and displays that month with the day highlighted.

        Scanner console = new Scanner(System.in);
        Calendar caltoday = Calendar.getInstance();

        int year = caltoday.get(Calendar.YEAR); // Change to user input in the future.
        boolean validdate = true;

        do {
            System.out.println("\nEnter a date: (mm/dd)");
            String datefromuser = console.nextLine();

            if (datefromuser.length() == 5) {

                char d1 = datefromuser.charAt(0);
                char d2 = datefromuser.charAt(1);
                char d3 = datefromuser.charAt(3);
                char d4 = datefromuser.charAt(4);

                if ((d1 == 48 || d1 == 49)
                        && (Character.isDigit(d2))
                        && (d3 == 48 || d3 == 49 || d3 == 50 || d3 == 51)
                        && (Character.isDigit(d4))) {

                    month = Integer.parseInt(datefromuser.substring(0, 2));
                    date = Integer.parseInt(datefromuser.substring(3, 5));

                    if (date <= lastDayOfMonth(month, year) && date > 0) {
                        drawMonth(month, date, year, yearArray);
                    }
                    return month;

                } else {
                    System.out.println("That is not a valid date.");
                    validdate = false;
                }
            } else {
                System.out.println("That is not a valid date. ");
                validdate = false;
            }
        } while (validdate = true);

        return month;
    }// end cmdE()

    public static int cmdT(int month, int date, String[][] yearArray){
        // Displays the current month.

        Calendar caltoday = Calendar.getInstance();
        month = (caltoday.get(Calendar.MONTH) + 1);
        date = caltoday.get(Calendar.DATE);

        int year = caltoday.get(Calendar.YEAR);

        drawMonth(month, date, year, yearArray);

        return month;
    }// End cmdT()

    public static void drawMonth(int month, int date, int year, String[][] yearArray){
        // Prints a month to console with either 4, 5, or 6 weeks

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);

        int firstdayofmonth = cal.get(Calendar.DAY_OF_WEEK);
        int position = 1;

        drawLine();
        System.out.println("|    Sunday     |    Monday     |    Tuesday    |   Wednesday   |    Thursday   |     Friday    |    Saturday   |");
        drawLine();

        if (((month == 1 || month == 3  || month == 5 || month == 7 || month == 8 || month == 10
                || month == 12) || (firstdayofmonth == 7 && month != 2)) && firstdayofmonth > 5) {

            for (int week = 1; week <= 6; week++) {
                drawWeek(month, position, firstdayofmonth, year, date, yearArray);
                position += 7;
            }
        } else if (month == 2 && firstdayofmonth == 1 && year % 4 != 0){
            for (int week = 1; week <= 4; week++) {
                drawWeek(month, position, firstdayofmonth, year, date, yearArray);
            }
        } else {
            for (int week = 1; week <= 5; week++){
                drawWeek(month, position, firstdayofmonth, year, date, yearArray);
                position += 7;
            }
        }

        String strMonth = nameOfMonth(month);
        System.out.println("Month: " + strMonth);

        if (date < 50) {
            System.out.println("Day: " + date);
        }
        System.out.println("Year: " + year + "\n");

    }// End drawMonth

    public static void drawWeek(int month, int position, int firstdayofmonth, int year, int date, String[][] yearArray){
        // Draws a Week
        int lastday = lastDayOfMonth(month, year);
        for (int line = 0; line < 12 / 2; line++) {
            if (line == 0) { // First row prints the date.
                drawNumberRow(position, firstdayofmonth, lastday, date);
            } else if ((line == 1)){
                drawEventRow(position, firstdayofmonth, lastday, date, month, yearArray);
            }else{
                for (int i = 1; i <= 7; i++) {
                    System.out.print("|");
                    drawSpaces(15);
                }
                System.out.println("|");
            }
        }
        drawLine();
    }// End drawWeek()

    public static void drawNumberRow(int position, int firstdayofmonth, int lastday, int date){
        // Draws the row of the week that contains the day.

        int offset = firstdayofmonth - 1;
        int saturday = (position - offset + 7);

        for (int today = position - offset; today < saturday; today++) {
            if (today <= 0) {
                System.out.print("|");
                drawSpaces(15);
            } else if (today < 10) {
                System.out.print("| " + today );
                if (today == date){
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
                drawSpaces(12); // Extra space if 1 digit date.
            } else if (today <= lastday) {
                System.out.print("| " + today );
                if (today == date){
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
                drawSpaces(11);
            } else {
                System.out.print("|");
                drawSpaces(15);  //
            }
        }
        System.out.println("|");
    } // end drawNumberRow()

    public static void drawSpaces(int spaces) {
        // Draws spaces

        for (int k = 0; k < spaces; k++) {
            System.out.print(" ");
        }
    }// End drawSpaces

    public static void drawLine() {
        // Draws a line.

        for (int i = 0; i < 16; i++){
            System.out.print("=======");
        }
        System.out.println();
    } // End drawLine

    public static String nameOfMonth(int month){
        // Takes in an integer month (1-12) and returns the name of that month as a string.

        switch (month) {
            case 1:  return "January";
            case 2:  return "February";
            case 3:  return "March";
            case 4:  return "April";
            case 5:  return "May";
            case 6:  return "June";
            case 7:  return "July";
            case 8:  return "August";
            case 9:  return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return "Not a month!";
        }
    } // end nameOfMonth

    public static int lastDayOfMonth(int month, int year) {
        // Takes a month and a year and returns how many days in that month.

        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) &&
                        !(year % 100 == 0))
                        || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default: return 13;
        }
    } // End lastDayOfMonth

    public static void eventPlan(String[][] yearArray)throws FileNotFoundException {
        // Gets a date and an event from the user and adds them to the Events2019 file.

        Scanner console = new Scanner(System.in);

        String filename = "Events2019";
        System.out.println(filename);

        String strmonth;
        String strdate;

        int[] dateFromUser = getDateFromUser();
        int month = dateFromUser[0];
        int date = dateFromUser[1];

        if (month < 10){
            strmonth = "0" + month;
        }else{
            strmonth = Integer.toString(month);
        }
        if (date < 10){
            strdate = "0" + date;
        } else {
            strdate = Integer.toString(date);
        }

        System.out.println("Enter an event:");
        String event = console.nextLine();

        try(FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(strmonth + "/" + strdate + " " + event);
        } catch (IOException e) {

        }

        populateDateArray(yearArray);


    }// End eventPlan

    public static String[][]makeYearArray(int year){
        // Constructs a jagged array representing the days of the given year

        String[][] arr = new String[12][];

        for (int i = 0; i < arr.length; i++) {
            int month = i + 1;
            int daysinmonth = lastDayOfMonth(month, 2019);
            arr[i] = new String[daysinmonth];
        }
        return arr;
    }// end makeYearArray


    public static int[] dateX(String datefromuser){
        // Returns a date from the user

        int month = Integer.parseInt(datefromuser.substring(0, 2));
        int date = Integer.parseInt(datefromuser.substring(3, 5));
        return new int[]{month, date};
    }// end dateX


    public static boolean checkDate(String datefromuser) {
        // Returns true if the date given by the user is valid.

        int month = 100;
        int date = 100;

        if (datefromuser.length() == 5) {

            char d1 = datefromuser.charAt(0);
            char d2 = datefromuser.charAt(1);
            char d3 = datefromuser.charAt(3);
            char d4 = datefromuser.charAt(4);

            if ((d1 == 48 || d1 == 49)
                    && (Character.isDigit(d2) == true)
                    && (d3 == 48 || d3 == 49 || d3 == 50 || d3 == 51)
                    && (Character.isDigit(d4) == true)) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }// End checkDate

    public static int[] getDateFromUser(){
        // Gets a date from the user.

        Scanner console = new Scanner (System.in);
        String datefromuser = "";

        do {
            System.out.println("Enter the date: \"MM/DD\"");
            datefromuser = console.nextLine();
        } while ((!checkDate(datefromuser)));

        int[] dateArray = dateX(datefromuser);
        return dateArray;
    }// End getDateFromUser

    public static String[][] populateDateArray(String[][] arr) throws FileNotFoundException{
        // Puts the contents of the event file into the dateArray[][]

        Scanner eventfile = new Scanner(new File("Events2019"));

        String line;
        String event;
        String firstbit;

        int month = 0;
        int day = 0;

        while (eventfile.hasNextLine()) {

            line = eventfile.nextLine();
            Scanner linescan = new Scanner(line);

            while (linescan.hasNext()) {

                firstbit = linescan.next();
                month = Integer.parseInt(firstbit.substring(0,2));
                day = Integer.parseInt(firstbit.substring(3,5));
                event = linescan.nextLine();

                if (event.charAt(0) == ' '){
                    event = event.substring(1, event.length());
                }
                arr[month -1][day - 1] = event;
            }

        }
        return arr;
    }// End populateDateArray()

    public static void drawEventRow(int position, int firstdayofmonth, int lastday, int date, int month, String[][] yearArray){
        // Draws the row that contains the day number.

        int offset = firstdayofmonth - 1;
        int saturday = (position - offset + 7);

        System.out.print("|");
        for (int today = position - offset; today < saturday; today++) {

            if ((month - 1 >= 0) && (today - 1 < lastday) && (today > 0)){
                if (yearArray[month-1][today-1] != null) {
                    System.out.print(" ");
                    System.out.print(yearArray[month - 1][today - 1]);
                    drawSpaces((15 - yearArray[month - 1][today - 1].length()) - 1);
                } else drawSpaces(15);
            } else {
                drawSpaces(15);
            }
            System.out.print("|");
        }
        System.out.println();
    }// End drawEventRow

    public static void filePrint(int year, String[][] yearArray) throws FileNotFoundException{
        // Gets a date and output file from the user and prints the calendar for that date to the file.

        int[] dateFromUser = getDateFromUser();
        int month = dateFromUser[0];
        int date = dateFromUser[1];

        Scanner console = new Scanner(System.in);

        System.out.println("Please enter the name of the file you would like to save:");
        String fileOutput = console.nextLine();
        System.out.println("File Saved.");
        File f = new File(fileOutput);

        PrintStream output = new PrintStream(f);
        PrintStream consoleOut = System.out;

        System.setOut(output);
        drawMonth(month, date, year, yearArray);

        System.setOut(consoleOut);
        output.close();
    }// End filePrint()

}

/*
NOTES:
Does not know how to handle long events.
Can not do multiple events in a single day.
No longer rolls over to next year because there is no date array for that next year.
 */
