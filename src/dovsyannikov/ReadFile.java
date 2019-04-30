package dovsyannikov;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dovsyannikov.Life.Life;


public class ReadFile {


    static Integer tmpCursorY = 3;
    static Integer tmpCursorX = 0;

    public static void read(String fileName, Charset cs) {

        for (int i = 0; i < 4; i++) {
            Life.add(new ArrayList<>());
        }

        String regex = "\\d{0,}[ob$]";
        Pattern pattern = Pattern.compile(regex);

        String regex2 = "\\d{1,}";
        Pattern pattern2 = Pattern.compile(regex2);

        long start = System.currentTimeMillis();

        try {
            Path path = Paths.get(fileName);
            BufferedReader br = Files.newBufferedReader(path, cs);
            String line;

            while ((line = br.readLine()) != null) {

                if ((line.charAt(0) == '#') || (line.charAt(0) == 'x')) {

                    //пропускаем ненужную строку
                    continue;
                } else if ((Character.isLetterOrDigit(line.charAt(0))) || (line.charAt(0) == '$')) {

                    Integer cntCell;

                    Matcher matcher = pattern.matcher(line);

                    while (matcher.find()) {

                        String tmpLine = line.substring(matcher.start(), matcher.end());
                        Matcher matcher2 = pattern2.matcher(tmpLine);

                        if (matcher2.find()) {

                            cntCell = Integer.parseInt(tmpLine.substring(matcher2.start(), matcher2.end()));

                        } else {

                            cntCell = 1;
                        }

                        fillingField(tmpLine.substring(tmpLine.length() - 1), cntCell);
                    }
                }
            }

            br.close();

        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            for (int i = 0; i < 4; i++) {
                Life.add(new ArrayList<>());
            }

        }

        long finish = System.currentTimeMillis();

        System.out.println("Время чтения: " + (finish - start));
    }

    private static void fillingField(String typeAction, Integer cntCell) {

        switch (typeAction) {

            case "o":

                for (int i = 0; i < cntCell; i++) {
                    Life.get(tmpCursorY).add(new Cell(-tmpCursorY, tmpCursorX + i));
                }
                tmpCursorX += cntCell;
                break;

            case "b":

                tmpCursorX += cntCell;
                break;

            case "$":

                tmpCursorY++;
                tmpCursorX = 0;
                Life.add(new ArrayList<>());
                break;
        }
    }
}