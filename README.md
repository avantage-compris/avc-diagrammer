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

![MyFirstDiagram.svg: "Hello World!" in a box](data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHhtbG5zOnhsaW5rPSdodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rJyB3aWR0aD0nMTYwJyBoZWlnaHQ9JzEyMCc+CjxyZWN0IHg9JzAuMCcgeT0nMC4wJyB3aWR0aD0nMTYwLjAnIGhlaWdodD0nMTIwLjAnIHN0cm9rZT0nIzAwMCcgZmlsbD0nbm9uZScvPgo8cGF0aCBkPSdNMTAuMCw1Mi4wCiBoMTAwLjAgbDIwLjAsMTAuMCBoLTEwMC4wIGwtMjAuMCwtMTAuMCB6JwogZmlsbD0nIzU4ZScgc3Ryb2tlPScjMDAwJy8+CjxwYXRoIGQ9J00xMTAuMCw1Mi4wCiB2LTQyLjAgbDIwLjAsMTAuMCB2NDIuMCBsLTIwLjAsLTEwLjAgeicKIGZpbGw9JyM4YmYnIHN0cm9rZT0nIzAwMCcvPgo8cmVjdCBmaWxsPScjOWNmJyBzdHJva2U9JyMwMDAnIHg9JzEwLjAnIHk9JzEwLjAnIHdpZHRoPScxMDAuMCcgaGVpZ2h0PSc0Mi4wJy8+Cjx0ZXh0IHg9JzYwLjAnIHk9JzI0LjAnIGZvbnQtc2l6ZT0nMTQuMCcgZmlsbD0nIzAwMCcgdGV4dC1hbmNob3I9J21pZGRsZSc+SGVsbG88L3RleHQ+Cjx0ZXh0IGZvbnQtd2VpZ2h0PSdib2xkJyB4PSc2MC4wJyB5PSc0NC4wJyBmb250LXNpemU9JzE0LjAnIGZpbGw9JyMwMDAnIHRleHQtYW5jaG9yPSdtaWRkbGUnPldvcmxkITwvdGV4dD4KPC9zdmc+Cg==)
