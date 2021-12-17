package test;

import main.Parser;
import main.Point;
import main.shapes.Group;
import main.shapes.Triangle;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Parser")
public class TestParser {

    final String triangles = """
            v -1 1 0
            v -1 0 0
            v 1 0 0
            v 1 1 0
                        
            g FirstGroup
            f 1 2 3
            g SecondGroup
            f 1 3 4
            """;

    @Test
    @DisplayName("Ignoring unrecognized lines")
    void ignoreLines() {
        String file = """
                There was a young lady named Bright 
                who traveled much faster than light. 
                She set out one day 
                in a relative way, 
                and came back the previous night.
                """;
        Parser p = Parser.parseString(file);
        assertEquals(5, p.ignored);
    }

    @Test
    @DisplayName("Vertex records")
    void vertices() {
        String file = """
                v -1 1 0
                v -1.0000 0.5000 0.0000
                v 1 0 0
                v 1 1 0
                """;
        Parser p = Parser.parseString(file);
        assertEquals(new Point(-1, 1, 0), p.getVertex(1));
        assertEquals(new Point(-1, .5, 0), p.getVertex(2));
        assertEquals(new Point(1, 0, 0), p.getVertex(3));
        assertEquals(new Point(1, 1, 0), p.getVertex(4));
    }

    @Test
    @DisplayName("Parsing triangle faces")
    void triangleFaces() {
        String file = """
                v -1 1 0
                v -1 0 0
                v 1 0 0
                v 1 1 0
                              
                f 1 2 3
                f 1 3 4
                """;
        Parser p = Parser.parseString(file);
        Triangle t1 = (Triangle) p.defaultGroup.getChildren().get(0);
        Triangle t2 = (Triangle) p.defaultGroup.getChildren().get(1);
        assertEquals(p.getVertex(1), t1.p1);
        assertEquals(p.getVertex(2), t1.p2);
        assertEquals(p.getVertex(3), t1.p3);
        assertEquals(p.getVertex(1), t2.p1);
        assertEquals(p.getVertex(3), t2.p2);
        assertEquals(p.getVertex(4), t2.p3);
    }

    @Test
    @DisplayName("Triangulating polygons")
    void triangulate() {
        String file = """
                v -1 1 0
                v -1 0 0
                v 1 0 0
                v 1 1 0
                v 0 2 0
                              
                f 1 2 3 4 5
                """;
        Parser p = Parser.parseString(file);
        Triangle t1 = (Triangle) p.defaultGroup.getChildren().get(0);
        Triangle t2 = (Triangle) p.defaultGroup.getChildren().get(1);
        Triangle t3 = (Triangle) p.defaultGroup.getChildren().get(2);
        assertEquals(p.getVertex(1), t1.p1);
        assertEquals(p.getVertex(2), t1.p2);
        assertEquals(p.getVertex(3), t1.p3);
        assertEquals(p.getVertex(1), t2.p1);
        assertEquals(p.getVertex(3), t2.p2);
        assertEquals(p.getVertex(4), t2.p3);
        assertEquals(p.getVertex(1), t3.p1);
        assertEquals(p.getVertex(4), t3.p2);
        assertEquals(p.getVertex(5), t3.p3);
    }

    @Test
    @DisplayName("Triangles in groups")
    void trianglesInGroups() {
        Parser p = Parser.parseString(triangles);
        Group g1 = p.getGroup("FirstGroup");
        Group g2 = p.getGroup("SecondGroup");
        Triangle t1 = (Triangle) g1.getChildren().get(0);
        Triangle t2 = (Triangle) g2.getChildren().get(0);
        assertEquals(p.getVertex(1), t1.p1);
        assertEquals(p.getVertex(2), t1.p2);
        assertEquals(p.getVertex(3), t1.p3);
        assertEquals(p.getVertex(1), t2.p1);
        assertEquals(p.getVertex(3), t2.p2);
        assertEquals(p.getVertex(4), t2.p3);


    }

    @Test
    @DisplayName("Converting an OBJ file to a group")
    void groupOBJ() {
        Parser p = Parser.parseString(triangles);
        assertTrue(p.defaultGroup.getChildren().contains(p.getGroup("FirstGroup")));
        assertTrue(p.defaultGroup.getChildren().contains(p.getGroup("SecondGroup")));
    }
}
