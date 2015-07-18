# avc-diagrammer

Ad hoc tools to generate SVG diagrams in Java.

## Hello World!

The following program:

    public class MyFirstDiagram extends AvcDiagrammer {

        public static void main(final String... args) {
        
            new MyFirstDiagram().border(1).run(160, 120);
        }
        
        @Override
        protected void body() {
        
            box(10, 10, 100, "#69f", "Hello", "*World!*");
        }
    }
    
will output the following SVG to stdout:

!["Hello World!" in a box](src/site/resources/images/MyFirstDiagram.svg) 