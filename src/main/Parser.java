package main;

import main.shapes.Group;
import main.shapes.Triangle;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    public Group defaultGroup;
    private final ArrayList<Point> vertices;
    private Group lastGroup;
    private final HashMap<String, Group> groupNames;
    public int ignored;

    public Parser() {
        this.defaultGroup = new Group();
        this.vertices = new ArrayList<>();
        this.ignored = 0;
        this.groupNames = new HashMap<>();
    }

    public static Parser parseString(String string) {
        return parseReader(new StringReader(string));
    }

    public static Parser parseFile(String filename) {
        try {
            return parseReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Parser parseReader(Reader reader) {
        Parser p = new Parser();
        BufferedReader buffer = new BufferedReader(reader);
        String line;
        try {
            while ((line = buffer.readLine()) != null) {
                p.handleLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    public void handleLine(String line) {
        String[] parts = line.split(" ");
        switch (parts[0]) {
            case "v" -> handleVertex(parts);
            case "f" -> handleFace(parts);
            case "g" -> handleGroup(parts);
            default -> this.ignored++;
        }
    }

    public Point getVertex(int num) {
        return this.vertices.get(num - 1);
    }

    public Point getVertex(String num) {
        if (num.contains("/")) {
            return this.vertices.get(Integer.parseInt(num.split("/")[0]) - 1);
        }
        return this.vertices.get(Integer.parseInt(num) - 1);
    }

    public Group getGroup(String name) {
        return groupNames.get(name);
    }

    public void handleVertex(String[] parts) {
        vertices.add(new Point(Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3])));

    }

    public void handleFace(String[] parts) {
        if (parts.length == 4) {
            if (this.lastGroup != null) {
                this.lastGroup.addChild(new Triangle(getVertex(parts[1]), getVertex(parts[2]), getVertex(parts[3])));
            } else {
                this.defaultGroup.addChild(new Triangle(getVertex(parts[1]), getVertex(parts[2]), getVertex(parts[3])));
            }

        } else {
            handleFan(parts);
        }
    }

    public void handleFan(String[] parts) {

        for (int index = 2; index < (parts.length - 1); index++) {
            if(this.lastGroup != null) {
                lastGroup.addChild(new Triangle(getVertex(parts[1]), getVertex(parts[index]), getVertex(parts[index + 1])));
            } else {
                defaultGroup.addChild(new Triangle(getVertex(parts[1]), getVertex(parts[index]), getVertex(parts[index + 1])));

            }
        }
    }

    public void handleGroup(String[] parts) {
        Group g = new Group();
        this.defaultGroup.addChild(g);
        this.lastGroup = g;
        this.groupNames.put(parts[1], g);
    }

}
