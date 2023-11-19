import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static ArrayList<ArrayList<String>> inputReader(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        String rootNameAndGms = scanner.nextLine();
        String[] arrOfStr = rootNameAndGms.split(" ");
        ArrayList<String> temp = new ArrayList<String>(Arrays.asList(arrOfStr[0], arrOfStr[1]));
        res.add(temp);

        while (scanner.hasNextLine()) {
            arrOfStr = scanner.nextLine().split(" ");
            temp = new ArrayList<String>(Arrays.asList(arrOfStr));
            res.add(temp);
        }
        scanner.close();
        return res;
    }

    public static void main(String[] args) throws IOException {

        String inputName = args[0];
        String outputName = args[1];

        AvlTree tree = new AvlTree(outputName);
        File file = new File(inputName);
        boolean first = true;
        for (ArrayList<String> item : inputReader(file)) {
            if (first) {
                String name = item.get(0);
                float gms = Float.parseFloat(item.get(1));
                tree.insert(name, gms);
                first = false;
            } else if (item.get(0).equals("MEMBER_IN")) {
                String name = item.get(1);
                float gms = Float.parseFloat(item.get(2));
                tree.insert(name, gms);
            } else if (item.get(0).equals("MEMBER_OUT")) {
                String name = item.get(1);
                float gms = Float.parseFloat(item.get(2));
                tree.delete(name, gms);
            } else if (item.get(0).equals("INTEL_TARGET")) {
                tree.intel_targeter(item.get(1), item.get(3), Float.parseFloat(item.get(2)),
                        Float.parseFloat(item.get(4)));
            } else if (item.get(0).equals("INTEL_RANK")) {
                tree.intel_rank(item.get(1), Float.parseFloat(item.get(2)));
            } else if (item.get(0).equals("INTEL_DIVIDE")) {
                tree.intel_divide();
            }
        }
        tree.myWriter.close();
    }
}