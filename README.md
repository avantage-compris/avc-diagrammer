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

[MyFirstDiagram.svg: "Hello World!" in a box (SVG)](src/site/resources/images/MyFirstDiagram.svg)

![MyFirstDiagram.png: "Hello World!" in a box](src/site/resources/rasterized/MyFirstDiagram.png)

Java code: [MyFirstDiagram.java](src/test/java/net/avcompris/tools/diagrammer/sample/MyFirstDiagram.java)

## Samples

  * [AppCenters.svg: App Centers (SVG)](src/site/resources/images/AppCenters.svg)
  
    <!-- ![AppCenters.svg: App Centers](src/site/resources/rasterized/AppCenters.png =100x) -->

    <img alt="AppCenters.svg: App Centers" src="src/site/resources/rasterized/AppCenters.png" width="100px">
  
    Java code: [AppCenters.java](src/test/java/net/avcompris/tools/diagrammer/sample/AppCenters.java)

  * [DefaultArchitecture.svg: Default Architecture (SVG)](src/site/resources/images/DefaultArchitecture.svg)
    
    <!-- ![DefaultArchitecture.svg: App Centers](src/site/resources/rasterized/DefaultArchitecture.png =100x) -->

    <img alt="DefaultArchitecture: Default Architecture" src="src/site/resources/rasterized/DefaultArchitecture.png" width="100px">
  
    Java code: [DefaultArchitecture.java](src/test/java/net/avcompris/tools/diagrammer/sample/DefaultArchitecture.java)
