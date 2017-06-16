package com.ywc.stock.util;

import com.ywc.stock.entity.Stock;
import com.ywc.stock.entity.StockList;
import org.apache.commons.math3.analysis.function.Abs;
import org.apache.commons.math3.analysis.function.Log;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    /**
     * 从.xslx文件读取股票文件,处理之后写到csv中,方便以后读取.
     */
    public static void readXslxToCsv() {
        StockList stockList = new StockList();
        System.out.println("readXlsx");
        int numOfRow, numOfColumn;
        Date date = null;
        Row row;
        Cell cell;
        File exelFile = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "sh.xlsx");
        if (exelFile.isFile() && exelFile.exists()) {
            System.out.println(exelFile.getPath() + " is file");
        }
        try {
            FileInputStream fip = new FileInputStream(exelFile);
            System.out.println(4);
            XSSFWorkbook workbook = new XSSFWorkbook(fip);
            System.out.println(5);
            XSSFSheet sheet = workbook.getSheetAt(0);
            System.out.println(6);
            Iterator<Row> rowIterator = sheet.iterator();
            Iterator<Cell> cellIterator;

            for (int i = 1; rowIterator.hasNext(); i++) {
                row = (XSSFRow) rowIterator.next();
                cellIterator = row.cellIterator();
                for (int j = 1; cellIterator.hasNext(); j++) {
                    cell = cellIterator.next();
                    if (i == 1 && j % 2 == 0) {
                        String wincode = cell.getStringCellValue();
                        Stock stock = new Stock();
                        stockList.add(stock);
                        stock.setWincode(wincode);
                    } else if (i == 1 && j % 2 != 0) {
                        continue;
                    } else if (i == 3) {
                        break;
                    } else if (i == 2 && j % 2 == 0) {
                        String name = cell.getStringCellValue();
                        stockList.get(j / 2 - 1).setName(name);
                    } else if (i == 2 && j % 2 != 0) {
                        continue;
                    } else {
                        if (j == 1) {
                            date = cell.getDateCellValue();
                        } else if (j % 2 == 0) {
                            if (cell.getNumericCellValue() != 0) {
                                stockList.get(j / 2 - 1).add((date), cell.getNumericCellValue());
                            } else {
                                stockList.get(j / 2 - 1).add((date), 0);
                            }
                        }
                    }
                }
            }
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (EncryptedDocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int numOfStocks = stockList.size();
        System.out.println("read xls over. the num of stocks is :" + numOfStocks);

        System.out.println("打印读取到的股票");
        for (Stock stock : stockList) {
            System.out.println(stock.getWincode() + "," + stock.getName() + "," + stock.getDateArrayList().get(0) + ","
                    + stock.getClosePriceArrayList().get(0));
        }

        // 输出到csv
        File file = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "sh.csv");
        if (file.exists()) {
            System.out.println("已经存在sh.csv,将会删除");
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        outputTOCsv(stockList, file);

    }

    public static void outputTOCsv(StockList stockList, File file) {
        try {
            if (stockList == null || stockList.size() == 0) {
                System.out.println("stockList的size为0或者为null");
                return;
            }
            ArrayList<Date> dateArrayList = stockList.get(0).getDateArrayList();
            FileWriter fileWriter = new FileWriter(file);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 写日期
            for (int i = 0; i < dateArrayList.size(); i++) {
                fileWriter.write(sdf.format(dateArrayList.get(i)));
                if (i != dateArrayList.size() - 1) {
                    fileWriter.write(",");
                }
            }
            fileWriter.write("\r\n");
            for (Stock stock : stockList) {
                fileWriter.write(stock.getName() + "," + stock.getWincode() + "\r\n");
                ArrayList<Double> closePriceList = stock.getClosePriceArrayList();
                for (int i = 0; i < closePriceList.size(); i++) {
                    fileWriter.write(closePriceList.get(i).toString());
                    if (i != closePriceList.size() - 1) {
                        fileWriter.write(",");
                    }
                }
                fileWriter.write("\r\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static StockList readCsvToStockList() {
        StockList stockList = new StockList();
        File file = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "sh.csv");
        ArrayList<Date> datesList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 读日期
            if (scanner.hasNextLine()) {
                String datesString = scanner.nextLine();
                String[] strings = datesString.split(",");
                for (String s : strings) {
                    try {
                        datesList.add(sdf.parse(s));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            // 读名称,代号和收盘价
            for (; scanner.hasNextLine(); ) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    continue;
                }
                Stock stock = new Stock();
                String[] strings1 = line.split(",");
                if (strings1.length != 2) {
                    System.out.println(strings1 + "长度不为2!");
                    return null;
                }
                stock.setName(strings1[0]);
                stock.setWincode(strings1[1]);
                stock.setDateArrayList(datesList);
                if (!scanner.hasNextLine()) {
                    System.out.println("文件格式有误!");
                    System.out.println(strings1);
                }
                String[] strings2 = scanner.nextLine().split(",");
                ArrayList<Double> closePriceList = new ArrayList<>();

                for (String s : strings2) {
                    closePriceList.add(Double.parseDouble(s));
                }
                if (closePriceList.size() != datesList.size()) {
                    System.out.println("数据有误!日期和收盘价个数不相等.");
                    return null;
                } else {
                    stock.setClosePriceArrayList(closePriceList);
                }
                stockList.add(stock);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // printStockListBriefly(stockList);
        return stockList;
    }

    public static void printStockListBriefly(StockList stockList) {
        System.out.println("stockList:");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Stock stock : stockList) {
            System.out.println(stock.getId() + "," + stock.getWincode() + "," + stock.getName());
        }
    }

    public static void printStockListInDetail(StockList stockList) {
        System.out.println("stockList:");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Stock stock : stockList) {
            System.out.println(stock.getId() + "," + stock.getWincode() + "," + stock.getName());
            for (Date date : stock.getDateArrayList()) {
                System.out.print(sdf.format(date) + ",");
            }
            System.out.println();
            for (Double d : stock.getClosePriceArrayList()) {
                System.out.print(d + ",");
            }
            System.out.println();
        }
    }

    /**
     * 建立股票索引
     */
    public static void buildIndex() {
        StockList stockList = Utils.readCsvToStockList();
        File file = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "index.csv");
        if (file.exists()) {
            System.out.println("index.csv已存在,将删除.");
            file.delete();
        }
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            int index = 1;
            for (Stock stock : stockList) {
                fileWriter.write(index + "," + stock.getWincode() + "\r\n");
                index++;
            }
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void addIndex(StockList stockList) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        File file = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "index.csv");
        if (!file.exists()) {
            System.out.println("index.csv不存在!");
            return;
        }
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            for (; scanner.hasNextLine(); ) {
                String[] strings = scanner.nextLine().split(",");
                map.put(strings[1], Integer.parseInt(strings[0]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (Stock stock : stockList) {
            stock.setId(map.get(stock.getWincode()).intValue());
        }
    }

    /**
     * 将股票的价格矩阵写到文件
     */
    public static void writePriceMatrixToCsv() {
        StockList stockList = Utils.readCsvToStockList();
        stockList.completeDefaultValue();
        stockList.addIndex();
        int numOfStocks = stockList.size();
        int numOfPrices = stockList.get(0).getClosePriceArrayList().size();
        double[][] matrix = new double[numOfPrices][numOfStocks];
        for (int j = 0; j < numOfStocks; j++) {
            if (stockList.get(j).getId() != j + 1) {
                System.out.println("索引数据有误!");
                return;
            }
            List<Double> closePriceList = stockList.get(j).getClosePriceArrayList();
            for (int i = 0; i < closePriceList.size(); i++) {
                matrix[i][j] = closePriceList.get(i).doubleValue();
            }
        }
        File file = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "priceMatrix.csv");
        if (file.exists()) {
            System.out.println("priceMatrix.csv已存在,将删除.");
            file.delete();
        }
        try {
            file.createNewFile();
            Utils.writeMatrixToFile(matrix, file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 将矩阵写入文件
     *
     * @param matrix
     * @param file
     */
    public static void writeMatrixToFile(double[][] matrix, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(matrix.length + "," + matrix[0].length + "\r\n");
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    fileWriter.write(matrix[i][j] + "");
                    if (j != matrix[i].length - 1) {
                        fileWriter.write(",");
                    }
                }

                fileWriter.write("\r\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 将矩阵写入文件,第一行是矩阵的维数
     *
     * @param matrix1
     * @param file
     */
    public static void writeMatrixToFile(RealMatrix matrix1, File file) {
        double[][] matrix = matrix1.getData();
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(matrix.length + "," + matrix[0].length + "\r\n");
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    fileWriter.write(matrix[i][j] + "");
                    if (j != matrix[i].length - 1) {
                        fileWriter.write(",");
                    }
                }

                fileWriter.write("\r\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static double[][] readPriceMatrix() {
        File file = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "priceMatrix.csv");
        try {
            Scanner scanner = new Scanner(file);
            String[] strings1 = scanner.nextLine().split(",");
            double[][] matrix = new double[Integer.valueOf(strings1[0])][Integer.valueOf(strings1[1])];
            for (int i = 0; i < matrix.length && scanner.hasNextLine(); i++) {
                String[] strings2 = scanner.nextLine().split(",");
                if (strings2.length != matrix[0].length) {
                    System.out.println("读入矩阵出现异常:长度:" + strings2.length + ",行号:" + i);
                }

                for (int j = 0; j < matrix[0].length; j++) {
                    matrix[i][j] = Double.valueOf(strings2[j]);
                }
            }
            // Utils.printMatrix(matrix);
            return matrix;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static double[][] readMatrix(File file) {
        try {
            Scanner scanner = new Scanner(file);
            String[] strings1 = scanner.nextLine().split(",");
            double[][] matrix = new double[Integer.valueOf(strings1[0])][Integer.valueOf(strings1[1])];
            for (int i = 0; i < matrix.length && scanner.hasNextLine(); i++) {
                String[] strings2 = scanner.nextLine().split(",");
                if (strings2.length != matrix[0].length) {
                    System.out.println("读入矩阵出现异常:长度:" + strings2.length + ",行号:" + i);
                }

                for (int j = 0; j < matrix[0].length; j++) {
                    matrix[i][j] = Double.valueOf(strings2[j]);
                }
            }
            // Utils.printMatrix(matrix);
            return matrix;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读对数收益率
     *
     * @return
     */
    public static double[][] readlogYieldMatrix() {
        File file = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "pearsonCorrelationMatrix.csv");
        try {
            Scanner scanner = new Scanner(file);
            String[] strings1 = scanner.nextLine().split(",");
            double[][] matrix = new double[Integer.valueOf(strings1[0])][Integer.valueOf(strings1[1])];
            for (int i = 0; i < matrix.length && scanner.hasNextLine(); i++) {
                String[] strings2 = scanner.nextLine().split(",");
                if (strings2.length != matrix[0].length) {
                    System.out.println("读入矩阵出现异常:长度:" + strings2.length + ",行号:" + i);
                }

                for (int j = 0; j < matrix[0].length; j++) {
                    matrix[i][j] = Double.valueOf(strings2[j]);
                }
            }
            // Utils.printMatrix(matrix);
            return matrix;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打印矩阵
     *
     * @param matrix
     */
    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j]);
                if (j != matrix[0].length - 1) {
                    System.out.print(",");
                }
            }
            System.out.println();
        }
    }

    public static void printMatrix(RealMatrix matrix1) {
        double[][] matrix = matrix1.getData();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j]);
                if (j != matrix[0].length - 1) {
                    System.out.print(",");
                }
            }
            System.out.println();
        }
    }

    /**
     * 将股票的对数收益率矩阵写到文件
     */
    public static void writeLogarithmicYieldeMatrixToCsv() {
        StockList stockList = Utils.readCsvToStockList();
        stockList.completeDefaultValue();
        stockList.addIndex();
        int numOfStocks = stockList.size();
        int numOfPrices = stockList.get(0).getClosePriceArrayList().size();
        double[][] matrix = new double[numOfPrices][numOfStocks];
        for (int j = 0; j < numOfStocks; j++) {
            if (stockList.get(j).getId() != j + 1) {
                System.out.println("索引数据有误!");
                return;
            }
            List<Double> closePriceList = stockList.get(j).getClosePriceArrayList();
            for (int i = 0; i < closePriceList.size(); i++) {
                matrix[i][j] = closePriceList.get(i).doubleValue();
            }
        }
        File file = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "logYieldMatrix.csv");
        if (file.exists()) {
            System.out.println("logYieldMatrix.csv已存在,将删除.");
            file.delete();
        }
        try {
            file.createNewFile();
            Utils.writeMatrixToFile(convertPriceMatrixTologyieldMatrix(matrix), file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 将价格矩阵转化成对数收益率矩阵
     *
     * @param priceMatrix
     * @return
     */
    public static double[][] convertPriceMatrixTologyieldMatrix(double[][] priceMatrix) {
        double[][] logMatrix = new double[priceMatrix.length - 1][priceMatrix[0].length];
        Log log = new Log();
        for (int i = 0; i < logMatrix.length; i++) {
            for (int j = 0; j < logMatrix[0].length; j++) {
                if (priceMatrix[i][j] == 0 || priceMatrix[i + 1][j] == 0) {
                    System.out.println("股价为0,无法计算对数收益率,默认置零.");
                    logMatrix[i][j] = 0;
                } else {
                    logMatrix[i][j] = log.value(priceMatrix[i + 1][j] / priceMatrix[i][j]);
                }
            }
        }
        printMatrix(logMatrix);
        return logMatrix;
    }

    /**
     * 使用阈值过滤矩阵
     */
    public static double[][] filterMatrixWithThreshold(double[][] matrix, double threshold) {
        double[][] filteredMatrix = new double[matrix.length][matrix[0].length];
        Abs abs = new Abs();
        for (int i = 0; i < filteredMatrix.length; i++) {
            for (int j = 0; j < filteredMatrix[0].length; j++) {
                filteredMatrix[i][j] = abs.value(matrix[i][j]) >= threshold ? 1 : 0;
            }
        }
        return filteredMatrix;
    }

    /**
     * 将id边文件转化成name文件
     */
    public static void convertIdFileToNameFile(File file) {
        String newPath = file.getParentFile().getParentFile().getParent() + "/name/" + file.getName();
        File newFile = new File(newPath);
        if (newFile.exists()) {
            System.out.println(newFile.getName() + "已经存在,将删除.");
            newFile.delete();
        }
        try {
            IndexUtil indexUtil = new IndexUtil();
            newFile.createNewFile();
            FileWriter fileWriter = new FileWriter(newFile);
            Scanner scanner = new Scanner(file);
            String firstLine = scanner.nextLine();
            fileWriter.write(firstLine + "\r\n");
            for (; scanner.hasNextLine(); ) {
                if (firstLine.equals("source,target,weight")) {// 边文件
                    String[] strings = scanner.nextLine().split(",");
                    fileWriter.write(indexUtil.findNameById(Integer.valueOf(strings[0]).intValue()) + ","
                            + indexUtil.findNameById(Integer.valueOf(strings[1]).intValue()) + "," + strings[2]
                            + "\r\n");
                }
            }
            scanner.close();
            fileWriter.close();
            System.out.println(file.getName() + "已创建.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将id文件转化成name文件
     */
    public static void convertIdFilesToNameFiles(File dir) {
        if (!dir.isDirectory()) {
            System.out.println(dir.getParent() + "不是目录.");
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            convertIdFileToNameFile(file);
        }
    }

    /**
     * 将id社团文件转化成name文件
     */
    public static void convertConmmunityIdFileToNameFile(File file) {
        String newPath = file.getParentFile().getParentFile().getPath() + "/name/" + file.getName();
        File newFile = new File(newPath);
        if (newFile.exists()) {
            System.out.println(newFile.getName() + "已经存在,将删除.");
            newFile.delete();
        }
        try {
            IndexUtil indexUtil = new IndexUtil();
            newFile.createNewFile();
            FileWriter fileWriter = new FileWriter(newFile);
            Scanner scanner = new Scanner(file);
            String firstLine = scanner.nextLine();
            fileWriter.write(firstLine + "\r\n");
            for (; scanner.hasNextLine(); ) {
                if (firstLine.equals("id,modularity_class")) {// 社团文件
                    String[] strings = scanner.nextLine().split(",");
                    fileWriter.write(
                            indexUtil.findNameById(Integer.valueOf(strings[0]).intValue()) + "," + strings[1] + "\r\n");
                }
            }
            scanner.close();
            fileWriter.close();
            System.out.println(file.getName() + "已创建.");
        } catch (IOException e) {
            System.out.println(newFile + " 创建失败.");
            e.printStackTrace();
        }
    }

    /**
     * 将目录中的id社团文件转化成name文件
     */
    public static void convertCommunityIdFilesToNameFiles(File dir) {
        if (!dir.isDirectory()) {
            System.out.println(dir.getParent() + "不是目录.");
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            convertConmmunityIdFileToNameFile(file);
        }
    }

    public static void convertCluToCsv(File file) {
        String fileName = file.getName();
        File file2 = new File(file.getParentFile().getParent() + "/csv/" + fileName.replaceAll(".clu", ".csv"));
        if (file2.exists()) {
            System.out.println(file2.getName() + "已存在,将删除.");
            file2.delete();
        }
        try {
            file2.createNewFile();
            FileWriter fileWriter = new FileWriter(file2);
            Scanner scanner = new Scanner(file);
            scanner.nextLine();
            scanner.nextLine();
            fileWriter.write("id,modularity_class\r\n");
            for (; scanner.hasNextLine(); ) {
                String[] strings = scanner.nextLine().split(" ");
                fileWriter.write(strings[0] + "," + strings[1] + "\r\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void convertClusToCsvs(File dir) {
        if (!dir.isDirectory()) {
            System.out.println(dir.getParent() + "不是目录.");
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            convertCluToCsv(file);
        }
    }

    /**
     * 对社团文件按社团id排序,如果传入的是目录则对目录下所有的文件排序
     */
    public static void sortCommunityCsvByCommunityId(File csv) {
        if (!csv.exists()) {
            System.out.println(csv.getPath() + "不存在");
            return;
        }

        if (csv.isDirectory()) {
            File[] files = csv.listFiles();
            for (File file : files) {
                sortCommunityCsvByCommunityId(file);
            }
        } else if (csv.isFile()) {
            FileInputStream fileInputStream = null;
            BufferedReader bufferedReader = null;
            List<CE> ceList = new ArrayList<>();
            String head = null;
            try {
                fileInputStream = new FileInputStream(csv);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line;

                head = bufferedReader.readLine();
                for (; (line = bufferedReader.readLine()) != null; ) {
                    String[] strings = line.split(",");
                    ceList.add(new CE(Integer.valueOf(strings[0]), Integer.valueOf(strings[1])));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bufferedReader.close();
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(ceList);

            BufferedWriter bufferedWriter = null;
            try {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv)));
                bufferedWriter.write(head + "\r\n");
                for (int i = 0; i < ceList.size(); i++) {
                    bufferedWriter.write(ceList.get(i).getId() + "," + ceList.get(i).getCommunityNumber() + "\r\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class CE implements Comparable<CE> {
    private int id;
    private int communityNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommunityNumber() {
        return communityNumber;
    }

    public void setCommunityNumber(int communityNumber) {
        this.communityNumber = communityNumber;
    }

    public CE() {

    }

    public CE(int id, int communityNumber) {
        super();
        this.id = id;
        this.communityNumber = communityNumber;
    }

    @Override
    public int compareTo(CE o) {

        return this.communityNumber - o.getCommunityNumber();
    }

}