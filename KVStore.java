import java.util.*;
import java.io.*;
import java.lang.*;

public class KVStore {

    private Map<String,Integer> map;
    private Stack<Map<String,Integer>> history;

    public KVStore() {
        map = new HashMap<String,Integer>();
        history = new Stack<>();
    }
    
    public void execute(String inputFileName) throws Exception {
        FileInputStream file = new FileInputStream(inputFileName);
        // reader for input file
        BufferedReader br = new BufferedReader(new InputStreamReader(file));
        // writer for output file
        BufferedWriter writer = new BufferedWriter(new FileWriter("./out.txt"));
        String line;
        String res;
        try {
            while ((line = br.readLine()) != null) {
                res = parse(line);      
                if (res != null) { // if expecting a result
                    writer.write(res);
                    writer.newLine();
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        } finally {
            br.close(); writer.flush();
            writer.close();
        }
    }

    // parse each line of input file, and return output string if it is expected for the command
    private String parse(String line) throws Exception {
        String[] tokens = line.split(" ");
        Integer res;
        switch(tokens[0]) {
            case "SET":
                set(tokens);
                return null;
            case "GET":
                res = get(tokens);
                return (res == null) ? "NULL" : String.valueOf(res);
            case "UNSET":
                unset(tokens);
                return null;
            case "NUMWITHVALUE":
                res = numWithValue(tokens);
                return String.valueOf(res);
            case "BEGIN":
                begin();
                break;
            case "ROLLBACK":
                return rollback();
            case "COMMIT":
                commit();
                break;
            case "END":
                System.exit(0);
                break;
            default:
                System.out.println("Invalid arguments: " + tokens[0]);
                System.exit(0);
        }
        return null;
    }

    private void begin() {
        Map<String,Integer> block = new HashMap<>();
        history.push(block);
    }
    private void set(String[] tokens) {
        verifyArgLen(tokens, 3);
        String key = tokens[1];
        //might throw exception
        int newval = Integer.parseInt(tokens[2]);
        // record for future rollback
        Integer oldval = map.get(key);
        if (!history.isEmpty()) {
            Map<String,Integer> block = history.peek();
            if (!block.containsKey(key)) block.put(key, oldval);
        }
        map.put(key, newval);
    }

    private Integer get(String[] tokens) {
        verifyArgLen(tokens, 2);
        return map.get(tokens[1]);
    }
    
    private void unset(String[] tokens) {
        verifyArgLen(tokens, 2);
        String key = tokens[1];
        Integer oldval = map.get(key);
        if (!history.isEmpty()) {
            Map<String,Integer> block = history.peek();
            if (!block.containsKey(key)) block.put(key, oldval);
        }
        map.remove(key); 
    }

    private String rollback() {
        if (history.isEmpty()) return "INVALID ROLLBACK";
        Map<String,Integer> b = history.pop();
        for (String key : b.keySet()) {
            Integer v = b.get(key);
            map.put(key, v);
        }
        return null;
    }

    private void commit() {
        history.clear();
    }

    private int numWithValue(String[] tokens) {
        verifyArgLen(tokens, 2);
        int val = Integer.valueOf(tokens[1]); 
        int count = 0;
        for (String key: map.keySet()) {
            if (map.get(key) == val) count++;
        }
        return count;
    }

    // private helper function to verify length of arguments
    private static void verifyArgLen(String[] tokens, int len) {
        if (tokens.length != len) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) System.exit(1);
        String inputFile = args[0];
        KVStore store = new KVStore();
        store.execute(inputFile);
    }

}
