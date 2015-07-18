# avc-diagrammer

Ad hoc tools in Java that generate SVG diagrams.

## Hello World!

The following program:

    public class MyFirstDiagram extends AvcDiagrammer {

        public static void main(final String... args) {
        
            new MyFirstDiagram().run(160, 80);
        }
        
        @Override
        protected void body() {
        
            box(10, 10, 100, "#69f", "Hello", "*World!*");
        }
    }
    
will output to stdout the following image (in SVG):

![MyFirstDiagram.png: "Hello World!" in a box](src/site/resources/rasterized/MyFirstDiagram.png)

[MyFirstDiagram.svg: "Hello World!" in a box](src/site/resources/images/MyFirstDiagram.svg)
