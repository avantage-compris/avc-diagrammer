# avc-diagrammer

Ad hoc tools in Java that generate SVG diagrams.

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

<!-- !["Hello World!" in a box](src/site/resources/images/MyFirstDiagram.svg) -->
<div>
<img src="src/site/resources/images/MyFirstDiagram.svg"
    alt="&quot;Hello World!&quot; in a box"
    onerror="this.onerror = null; this.src = 'https://raw.githubusercontent.com/avantage-compris/avc-diagrammer/master/' + this.src);">
</div>
