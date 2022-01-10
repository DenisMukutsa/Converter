package converter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Main implements IFileConverter {

    public String toBinary(String inputFileName, String outputFileName, String charSet) {

        String line = "";
        char[] buffer = new char[1024];
        String bynares = " ";

        try(FileReader reader = new FileReader(inputFileName)){
            while(reader.read(buffer) != -1){
                line += new String(buffer);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        Charset charset = Charset.forName(charSet);
        byte[] bytes = line.getBytes(charset);

        String[] binary = new String[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            binary[i] = String.format("%8s", Integer.toBinaryString(bytes[i])).replace(" ", "0");
            bynares = bynares + " " + binary[i];
        }

        File file = new File(outputFileName);
        if(!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }


        try(FileWriter writer = new FileWriter(outputFileName, false)){
            writer.write(bynares);
        }catch(IOException e){
            System.out.println(e.getMessage());

        }
        return outputFileName;
    }


    public String toText(String inputFileName, String outputFileName, String charSet) {

        String line = "";
        char[] buffer = new char[1024];

        try(FileReader reader = new FileReader(inputFileName)){
            while(reader.read(buffer) != -1){
                line += new String(buffer);
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }

        line = line.trim();

        String [] stringArray = line.split(" ");
        byte [] textBytes = new byte[stringArray.length];
        for (int i = 0; i < textBytes.length; i++) {
            textBytes[i] = Byte.parseByte(stringArray[i], 2);
        }

        Charset charset = Charset.forName(charSet);
        String result = new String(textBytes, charset);

        File file = new File(outputFileName);
        if(!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        try(FileWriter writer = new FileWriter(outputFileName, false)){
            writer.write(result);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        return outputFileName;
    }

    public double getSum(String fileName) throws ConverterException {

        if(fileName == null) {
            throw new ConverterException("Имя файла не указано.");
        }
        double sum = 0.0;

        try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            if (!input.ready()) throw new ConverterException("File is empty");

            ArrayList<String> buffer = new ArrayList<>();
            while (input.ready())
                buffer.add(input.readLine());

            for (String s : buffer) {
                String[] line = s.split(" ");

                for (int i = 0; i < line.length; i++) {
                    if (line[i].matches("\\d+")) {
                        sum += Double.parseDouble(line[i]);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return sum;

    }

    public static void main(String[] args) {
        Main main = new Main();

        System.out.println("Файл, в котором текст представлен в двоичной системе: " + main.toBinary("C:\\Users\\Денис\\IdeaProjects\\Converter\\src\\converter\\Исходный_файл.txt", "C:\\Users\\Денис\\IdeaProjects\\Converter\\src\\converter\\Байтовый_файл.txt", "ASCII"));

        System.out.println("Файл, в котором текст вновь преобразован в символы: " + main.toText("C:\\Users\\Денис\\IdeaProjects\\Converter\\src\\converter\\Байтовый_файл.txt", "C:\\Users\\Денис\\IdeaProjects\\Converter\\src\\converter\\Файл_с_текстом.txt", "ASCII"));

        try {
            System.out.println("Сумма имеющихся в тексте чисел: " + main.getSum("C:\\Users\\Денис\\IdeaProjects\\Converter\\src\\converter\\Исходный_файл.txt"));
        }
        catch(ConverterException e) {
            System.out.println(e.getMessage());
        }
    }
}

